package io.github.lottetreg.httpserver;

public abstract class BaseRoute implements Routable {
  protected String path;
  protected String method;

  BaseRoute(String path, String method) {
    this.path = path;
    this.method = method;

    Routable.store(this);
  }

  public String getPath() {
    return this.path;
  }

  public String getMethod() {
    return this.method;
  }

  public Boolean hasPath(String path) {
    return getPath().equals(path);
  }

  public Boolean hasMethod(String method) {
    return getMethod().equals(method);
  }

  public String getAllowedMethods() {
    return Routable.getAllowedMethods(getPath());
  }
}
