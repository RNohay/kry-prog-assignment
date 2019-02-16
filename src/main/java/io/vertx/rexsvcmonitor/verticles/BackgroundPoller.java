package io.vertx.rexsvcmonitor.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.dns.AddressResolverOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.rexsvcmonitor.model.Service;
import io.vertx.rexsvcmonitor.model.ServiceStatus;
import io.vertx.rexsvcmonitor.persistence.implementation.PersistenceAdapterImpl;
import io.vertx.rexsvcmonitor.persistence.interfaces.PersistenceAdapter;

import java.net.URI;
import java.util.Map;

public class BackgroundPoller extends AbstractVerticle {
  private PersistenceAdapter persistenceAdapter = new PersistenceAdapterImpl();

  @Override
  public void start(Future<Void> startFuture) throws Exception {

    // IT managed environment may have hard time on domain searching
    Vertx vertx = Vertx.vertx(new VertxOptions().
      setAddressResolverOptions(
        new AddressResolverOptions().
          addServer("8.8.8.8").
          addServer("8.8.4.4"))
    );

    vertx.setPeriodic(20000, id -> {
      Map<Integer, Service> serviceMap = persistenceAdapter.getAllServices();
      for(Map.Entry<Integer, Service> entry : serviceMap.entrySet()) {
        HttpClient httpClient = vertx.createHttpClient( new HttpClientOptions().setMaxRedirects(7));

        try {
          URI uri = new URI(entry.getValue().getUrl());
          System.out.println("Trying to get:\n" + uri.getHost() + uri.getPath() );
          httpClient.getNow(uri.getHost(), uri.getPath(), response -> {
            System.out.println("Service " + entry.getKey() + " responded with status code" + response.statusCode());
            if(response.statusCode() == 200) {
              persistenceAdapter.updateServiceStatus(entry.getKey(), ServiceStatus.OK, vertx);
            } else {
              persistenceAdapter.updateServiceStatus(entry.getKey(), ServiceStatus.FAIL, vertx);
            }
          });

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}
