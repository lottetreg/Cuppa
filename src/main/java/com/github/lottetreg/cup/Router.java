package com.github.lottetreg.cup;

import com.github.lottetreg.saucer.HttpRequest;
import com.github.lottetreg.saucer.HttpResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Router {
  private List<Responsive> routes;

  Router(List<Responsive> routes) {
    this.routes = routes;
  }

  public HttpResponse route(HttpRequest httpRequest) {
    Request request = new Request(
        httpRequest.getMethod(),
        httpRequest.getPath(),
        httpRequest.getHeaders(),
        httpRequest.getBody()
    );

    String requestPath = request.getPath();

    if (noRoutesMatchPath(requestPath)) {
      return createHttpResponse(new Response(404));
    }

    String requestMethod = request.getMethod();

    switch (requestMethod) {
      case "HEAD":
        return createHttpResponse(new Response(200));

      case "OPTIONS":
        return createHttpResponse(new Response(200, Map.of("Allow", getAllowedMethods(requestPath))));

      default:
        Optional<Responsive> optionalRoute = routesWithMatchingPath(requestPath)
            .filter(route -> route.getMethod().equals(requestMethod))
            .findFirst();

        if (optionalRoute.isPresent()) {
          return createHttpResponse(optionalRoute.get().getResponse(request));
        } else {
          return createHttpResponse(new Response(405, Map.of("Allow", getAllowedMethods(requestPath))));
        }
    }
  }

  private String getAllowedMethods(String path) {
    Stream<String> allowedMethods = this.routes.stream()
        .filter(route -> route.getPath().equals(path))
        .map(Responsive::getMethod);

    return Stream.concat(allowedMethods, Stream.of("HEAD", "OPTIONS"))
        .distinct()
        .collect(Collectors.joining(", "));
  }

  private Stream<Responsive> routesWithMatchingPath(String path) {
    return this.routes.stream()
        .filter(route -> route.getPath().equals(path));
  }

  private Boolean noRoutesMatchPath(String path) {
    return this.routes.stream()
        .noneMatch(route -> route.getPath().equals(path));
  }

  private HttpResponse createHttpResponse(Response response) {
    return new HttpResponse.Builder(response.getStatusCode())
        .setHeaders(response.getHeaders())
        .setBody(response.getBody())
        .build();
  }
}
