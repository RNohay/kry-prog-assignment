package io.vertx.rexsvcmonitor.persistence.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.rexsvcmonitor.model.Service;
import io.vertx.rexsvcmonitor.model.ServiceStatus;
import io.vertx.rexsvcmonitor.persistence.interfaces.PersistenceAdapter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistenceAdapterImpl implements PersistenceAdapter {

  private final String dataFilePath = "./data.json";

  @Override
  public Service getServiceByID(int id) {
    Map<Integer, Service> serviceMap = getAllServices();
    return serviceMap.get(id);
  }

  @Override
  public Map<Integer, Service> getAllServices() {
    Gson gson = new Gson();
    List<Service> serviceList = new ArrayList<>();
    Map<Integer, Service> serviceMap = new HashMap<>();
    try {
      JsonReader reader = new JsonReader(new FileReader(dataFilePath));
      serviceList = gson.fromJson(reader, new TypeToken<List<Service>>(){}.getType());
    } catch (FileNotFoundException e) {
      System.out.println("No data file found.");
    }

    if(serviceList != null) {
      for(Service service : serviceList) {
        serviceMap.put(service.getId(), service);
      }
    }
    return serviceMap;
  }

  @Override
  public void createService(Service service, Vertx vertx) {
    Map<Integer, Service> serviceMap = getAllServices();
    serviceMap.put(service.getId(), service);

    saveDataToFile(serviceMap, vertx);
  }

  @Override
  public void updateServiceStatus(int id, ServiceStatus serviceStatus, Vertx vertx) {
    Map<Integer, Service> serviceMap = getAllServices();
    serviceMap.get(id).setStatus(serviceStatus);
    serviceMap.get(id).setLastChecked(Instant.now().getEpochSecond());

    saveDataToFile(serviceMap, vertx);
  }

  @Override
  public void deleteServiceById(int id, Vertx vertx) {
    Map<Integer, Service> serviceMap = getAllServices();
    serviceMap.remove(id);

    saveDataToFile(serviceMap, vertx);
  }

  private void saveDataToFile(Map<Integer, Service> serviceMap, Vertx vertx) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String dataJson = gson.toJson(serviceMap.values());

    vertx.fileSystem().writeFile(dataFilePath, Buffer.buffer(dataJson), result -> {
      if (!result.succeeded()) {
        System.out.println("Error writing on data file");
      }
    });
  }

}
