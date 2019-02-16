package io.vertx.rexsvcmonitor.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.rexsvcmonitor.model.Service;
import io.vertx.rexsvcmonitor.persistence.implementation.PersistenceAdapterImpl;
import io.vertx.rexsvcmonitor.persistence.interfaces.PersistenceAdapter;

import java.util.Map;

public class OperationsManager extends AbstractVerticle {
  private PersistenceAdapter persistenceAdapter = new PersistenceAdapterImpl();

  @Override
  public void start(Future<Void> startFuture) throws Exception {

    Router router = Router.router(vertx);
    router.get("/services").handler(this::getAllServices);
    router.get("/services/:id").handler(this::getServiceById);
    router.route("/services*").handler(BodyHandler.create());
    router.post("/services").handler(this::addService);
    router.delete("/services/:id").handler(this::deleteServiceById);

    vertx
      .createHttpServer()
      .requestHandler(router::accept)
      .listen(
        config().getInteger("http.port", 8080),
        result -> {
          if (result.succeeded()) {
            startFuture.complete();
          } else {
            startFuture.fail(result.cause());
          }
        }
      );
  }

  private void getServiceById(RoutingContext routingContext) {
    Service service = persistenceAdapter.getServiceByID(Integer.parseInt(routingContext.request().getParam("id")));

    routingContext.response()
      .setStatusCode(200)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(service));
  }

  private void getAllServices(RoutingContext routingContext) {
    Map<Integer, Service> serviceMap = persistenceAdapter.getAllServices();

    routingContext.response()
      .setStatusCode(200)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(serviceMap.values()));
  }

  private void addService(RoutingContext routingContext) {
    Service service = Json.decodeValue(routingContext.getBodyAsString(),
      Service.class);

    persistenceAdapter.createService(service, routingContext.vertx());

    routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(service));
  }

  private void deleteServiceById(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    if(id != null) {
      persistenceAdapter.deleteServiceById(Integer.parseInt(id), routingContext.vertx());
      routingContext.response().setStatusCode(200).end();
    } else {
      routingContext.response().setStatusCode(400).end();
    }
  }
}
