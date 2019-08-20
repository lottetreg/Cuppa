package io.github.lottetreg.httpserver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Router {
  private List<Routable> routes;

  Router(List<Routable> routes) {
    this.routes = routes;
  }

  public Response route(HTTPRequest request) {
    Routable route = findMatchingRoute(request);
    return route.getResponse(request);
  }

  private Routable findMatchingRoute(HTTPRequest request) {
    String requestPath = request.getURI();
    String requestMethod = request.getMethod();

    if (noRoutesMatchPath(requestPath)) {
      throw new NoMatchingPath(requestPath);
    }

    Optional<Routable> matchingRoute = routesWithMatchingPath(requestPath)
        .filter(route -> route.hasMethod(requestMethod))
        .findFirst();

    return matchingRoute.orElseThrow(() ->
        new NoMatchingMethodForPath(requestMethod, requestPath));
  }

  private Stream<Routable> routesWithMatchingPath(String path) {
    return this.routes.stream()
        .filter(route -> route.hasPath(path));
  }

  private Boolean noRoutesMatchPath(String path) {
    return this.routes.stream()
        .noneMatch(route -> route.hasPath(path));
  }

  static class NoMatchingPath extends RuntimeException {
    NoMatchingPath(String path) {
      super(path);
    }
  }

  static class NoMatchingMethodForPath extends RuntimeException {
    NoMatchingMethodForPath(String method, String path) {
      super(method + " " + path);
    }
  }
}
