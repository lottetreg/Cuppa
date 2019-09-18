package com.github.lottetreg.cup;

public abstract class BaseRoute implements Responsive {
  protected String path;
  protected String method;

  BaseRoute(String path, String method) {
    this.path = path;
    this.method = method;
  }

  public String getPath() {
    return this.path;
  }

  public String getMethod() {
    return this.method;
  }
}
