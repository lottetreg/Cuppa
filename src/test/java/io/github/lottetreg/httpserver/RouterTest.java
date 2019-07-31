package io.github.lottetreg.httpserver;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
public class RouterTest {

  static class MockRoute extends BaseRoute {
    MockRoute(String path, String method) {
      super(path, method);
    }

    public HTTPResponse getResponse(HTTPRequest request) {
      return new HTTPResponse.Builder(200).build();
    }
  }

  static class MockOut implements Outable {
    public String message;

    public void println(String message) {
      this.message = message;
    }
  }

  @Test
  public void itReturnsTheResponseFromTheMatchingRoute() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());

    List<Routable> routes = Arrays.asList(new MockRoute("/", "GET"));

    HTTPResponse response = new Router(routes).route(request);

    assertEquals("HTTP/1.0 200 OK\r\n\r\n", response.toString());
  }

  @Test
  public void itIncludesAllowedMethodsInTheHeadersForOPTIONS() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("OPTIONS / HTTP/1.0"),
        new HTTPHeaders());

    List<Routable> routes = Arrays.asList(
        new MockRoute("/", "GET"),
        new MockRoute("/", "OPTIONS"));

    HTTPResponse response = new Router(routes).route(request);

    List<String> allowedMethods = Arrays.asList(response.getHeaders()
        .getHeader("Allow")
        .split(", "));

    assertTrue(allowedMethods.stream()
        .allMatch(method -> method.equals("GET") || method.equals("OPTIONS")));
  }

  @Test
  public void itReturns404ResponseAndLogsTheErrorIfThereIsNoRouteWithMatchingPath() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET /no_match HTTP/1.0"),
        new HTTPHeaders());

    List<Routable> routes = Arrays.asList(new MockRoute("", ""));

    Outable out = new MockOut();
    HTTPResponse response = new Router(routes, out).route(request);

    assertEquals("HTTP/1.0 404 Not Found\r\n\r\n", response.toString());
    assertEquals("No path matching /no_match", ((MockOut) out).message);
  }

  @Test
  public void itReturns405ResponseAndLogsTheErrorIfThereIsNoMatchingMethodForTheRoute() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("POST / HTTP/1.0"),
        new HTTPHeaders());

    List<Routable> routes = Arrays.asList(
        new MockRoute("/", "GET"),
        new MockRoute("/", "OPTIONS"));

    Outable out = new MockOut();
    HTTPResponse response = new Router(routes, out).route(request);

    List<String> allowedMethods = Arrays.asList(response.getHeaders()
        .getHeader("Allow")
        .split(", "));

    assertTrue(allowedMethods.contains("GET"));
    assertTrue(allowedMethods.contains("OPTIONS"));
    assertEquals(405, response.getStatusCode());
    assertEquals("No method POST for path /", ((MockOut) out).message);
  }

  @Test
  public void itReturns500ResponseAndLogsTheErrorForAnyOtherExceptions() {
    class BrokenRoute extends BaseRoute {
      BrokenRoute(String path, String method) {
        super(path, method);
      }

      public HTTPResponse getResponse(HTTPRequest request) {
        throw new RuntimeException("Something went wrong");
      }
    }

    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());

    List<Routable> routes = Arrays.asList(new BrokenRoute("/", "GET"));

    Outable out = new MockOut();
    HTTPResponse response = new Router(routes, out).route(request);

    assertEquals("HTTP/1.0 500 Internal Server Error\r\n\r\n", response.toString());
    assertEquals("Something went wrong", ((MockOut) out).message);
  }
}
