package io.vertx.rexsvcmonitor.model;

public enum ServiceStatus {
  OK("OK"),
  FAIL("FAIL");

  private final String value;

  ServiceStatus(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static ServiceStatus fromValue(String text) {
    for (ServiceStatus b : ServiceStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
