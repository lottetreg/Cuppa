package io.github.lottetreg.httpserver;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Router {
  private List<Routable> routes;

  Router(List<Routable> routes) {
    this.routes = routes;
  }

  public Response route(HTTPRequest request)
      throws NoMatchingPath, NoMatchingMethodForPath, Routable.MissingResource, Routable.FailedToGetResponse { // do this? Wrap and throw?

    Routable route = findMatchingRoute(request);

    if (route.getMethod().equals("OPTIONS")) {
      return new Response(200, Map.of("Allow", getAllowedMethods(request)));
    } else {
      return route.getResponse(request);
    }
  }

  public String getAllowedMethods(HTTPRequest request) {
    return routesWithMatchingPath(request.getURI())
        .map(Routable::getMethod)
        .collect(Collectors.joining(", "));
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
