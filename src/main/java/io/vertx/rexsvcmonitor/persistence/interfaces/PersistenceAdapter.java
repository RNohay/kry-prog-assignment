package io.vertx.rexsvcmonitor.persistence.interfaces;

import io.vertx.core.Vertx;
import io.vertx.rexsvcmonitor.model.Service;
import io.vertx.rexsvcmonitor.model.ServiceStatus;

import java.util.Map;

public interface PersistenceAdapter {

  Service getServiceByID(int id);

  Map<Integer, Service> getAllServices();

  void createService(Service service, Vertx vertx);

  void updateServiceStatus(int id, ServiceStatus serviceStatus, Vertx vertx);

  void deleteServiceById(int id, Vertx vertx);
}
