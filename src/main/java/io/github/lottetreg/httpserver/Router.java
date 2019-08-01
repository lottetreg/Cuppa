package io.github.lottetreg.httpserver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Router {
  private List<Routable> routes;
  private Outable out;

  Router(List<Routable> routes) {
    this.routes = routes;
    this.out = new Out();
  }

  Router(List<Routable> routes, Outable out) {
    this.routes = routes;
    this.out = out;
  }

  public HTTPResponse route(HTTPRequest request) {
    try {
      Routable route = findMatchingRoute(request);

      if(route.getMethod().equals("OPTIONS")) {
        return new HTTPResponse.Builder(200).build()
            .addHeader("Allow", getAllowedMethods(request));
      } else {
        return route.getResponse(request);
      }

    } catch (NoMatchingPath | Routable.MissingResource e) {
      this.out.println(e.getMessage());
      return new HTTPResponse.Builder(404).build();

    } catch (NoMatchingMethodForPath e) {
      this.out.println(e.getMessage());
      return new HTTPResponse.Builder(405).build()
          .addHeader("Allow", getAllowedMethods(request));

    } catch (Exception e) {
      this.out.println(e.getMessage());
      return new HTTPResponse.Builder(500).build();
    }
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

  private String getAllowedMethods(HTTPRequest request) {
    return routesWithMatchingPath(request.getURI())
        .map(Routable::getMethod)
        .collect(Collectors.joining(", "));
  }

  static class NoMatchingPath extends RuntimeException {
    NoMatchingPath(String path) {
      super("No path matching " + path);
    }
  }

  static class NoMatchingMethodForPath extends RuntimeException {
    NoMatchingMethodForPath(String method, String path) {
      super("No method " + method + " for path " + path);
    }
  }
}
