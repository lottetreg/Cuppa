package io.github.lottetreg.httpserver;

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class BaseRoute implements Routable {
  protected String path;
  protected String method;
  static public ArrayList<BaseRoute> routes = new ArrayList<>();

  BaseRoute(String path, String method) {
    this.path = path;
    this.method = method;

    routes.add(this);
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

  static public String getAllowedMethods(String path) {
    return routes.stream()
        .filter(route -> route.hasPath(path))
        .map(Routable::getMethod)
        .collect(Collectors.joining(", "));
  }
}
