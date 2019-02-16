package io.vertx.rexsvcmonitor;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.rexsvcmonitor.model.Service;
import io.vertx.rexsvcmonitor.persistence.implementation.PersistenceAdapterImpl;
import io.vertx.rexsvcmonitor.persistence.interfaces.PersistenceAdapter;
import io.vertx.rexsvcmonitor.verticles.BackgroundPoller;
import io.vertx.rexsvcmonitor.verticles.OperationsManager;

import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainVerticle extends AbstractVerticle {

  public static final AtomicInteger COUNTER = new AtomicInteger();

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    // initialize ID counter
    COUNTER.set(0);
    PersistenceAdapter persistenceAdapter = new PersistenceAdapterImpl();
    Map<Integer, Service> serviceMap = persistenceAdapter.getAllServices();
    for(Map.Entry<Integer, Service> entry : serviceMap.entrySet()) {
      if(entry.getKey().intValue() > COUNTER.get()) {
        COUNTER.set(entry.getKey().intValue() + 1);
      }
    }

    ServerSocket socket = new ServerSocket(0);
    int port = socket.getLocalPort();
    socket.close();

    DeploymentOptions options = new DeploymentOptions()
      .setConfig(new JsonObject().put("http.port", port)
      );

    System.out.println("Deploying in port " + port);

    vertx.deployVerticle(OperationsManager.class.getName(), options);
    vertx.deployVerticle(new BackgroundPoller());
  }
}
