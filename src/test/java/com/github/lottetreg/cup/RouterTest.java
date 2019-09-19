package com.github.lottetreg.cup;

import com.github.lottetreg.saucer.HttpRequest;
import com.github.lottetreg.saucer.HttpResponse;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class RouterTest {

  HttpRequest buildHttpRequest(String method, String path) {
    return new HttpRequest(
        method,
        path,
        "HTTP/1.0",
        new HashMap<>(),
        ""
    );
  }

  static class MockRoute extends BaseRoute {
    MockRoute(String method, String path) {
      super(method, path);
    }

    public Response getResponse(Request request) {
      return new Response(
          200,
          Map.of("Some-Header", "some value"),
          "hello".getBytes()
      );
    }
  }

  @Test
  public void itReturnsTheResponseFromTheMatchingRoute() {
    List<Responsive> routes = Collections.singletonList(new MockRoute("GET", "/"));
    HttpRequest request = buildHttpRequest("GET", "/");

    HttpResponse response = new Router(routes).route(request);

    assertEquals(200, response.getStatusCode());
    assertEquals("hello", new String(response.getBody()));
    assertEquals("some value", response.getHeaders().get("Some-Header"));
  }


  @Test
  public void itReturns404IfThereIsNoRouteWithMatchingPath() {
    List<Responsive> routes = new ArrayList<>();
    HttpRequest request = buildHttpRequest("GET", "/no_match");

    HttpResponse response = new Router(routes).route(request);

    assertEquals(404, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals(new HashMap<>(), response.getHeaders());
  }

  @Test
  public void itReturnsAnEmpty200IfItReceivesAHeadRequest() {
    List<Responsive> routes = Collections.singletonList(new MockRoute("GET", "/"));
    HttpRequest request = buildHttpRequest("HEAD", "/");

    HttpResponse response = new Router(routes).route(request);

    assertEquals(200, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals(new HashMap<>(), response.getHeaders());
  }

  @Test
  public void itReturnsA200WithAllowedMethodsIfItReceivesAnOptionsRequest() {
    List<Responsive> routes = Collections.singletonList(new MockRoute("GET", "/"));
    HttpRequest request = buildHttpRequest("OPTIONS", "/");

    HttpResponse response = new Router(routes).route(request);

    assertEquals(200, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals("GET, HEAD, OPTIONS", response.getHeaders().get("Allow"));
  }

  @Test
  public void itReturns405WithAllowedMethodsIfThereIsNoMatchingMethodForTheRoute() {
    List<Responsive> routes = Arrays.asList(
        new MockRoute("GET", "/"),
        new MockRoute("DELETE", "/"));
    HttpRequest request = buildHttpRequest("POST", "/");

    HttpResponse response = new Router(routes).route(request);

    assertEquals(405, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals("GET, DELETE, HEAD, OPTIONS", response.getHeaders().get("Allow"));
  }
}
