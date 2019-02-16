package io.vertx.rexsvcmonitor.model;

import io.vertx.rexsvcmonitor.MainVerticle;

public class Service {

  private final int id;

  private String name;

  private String url;

  private ServiceStatus status;

  private long lastChecked;

  public Service(int id, String name, String url, ServiceStatus status, long lastChecked) {
    this.id = id;
    this.name = name;
    this.url = url;
    this.status = status;
    this.lastChecked = lastChecked;
  }

  public Service(String name, String url, ServiceStatus status, long lastChecked) {
    this.id = MainVerticle.COUNTER.getAndIncrement();
    this.name = name;
    this.url = url;
    this.status = status;
    this.lastChecked = lastChecked;
  }

  public Service() {
    this.id = MainVerticle.COUNTER.getAndIncrement();
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public ServiceStatus getStatus() {
    return status;
  }

  public void setStatus(ServiceStatus status) {
    this.status = status;
  }

  public long getLastChecked() {
    return lastChecked;
  }

  public void setLastChecked(long lastChecked) {
    this.lastChecked = lastChecked;
  }

  @Override
  public String toString() {
    return "Service{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", url='" + url + '\'' +
      ", status=" + status +
      "}\n";
  }
}
