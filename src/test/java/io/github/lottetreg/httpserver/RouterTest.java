package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

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
  public void itReturnsAnExceptionIfThereIsNoRouteWithMatchingPath() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET /no_match HTTP/1.0"),
        new HTTPHeaders());

    List<Routable> routes = Arrays.asList(new MockRoute("", ""));

    exceptionRule.expect(Router.NoMatchingPath.class);
    exceptionRule.expectMessage("/no_match");

    new Router(routes).route(request);
  }

  @Test
  public void itReturnsAnExceptionIfThereIsNoMatchingMethodForTheRoute() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("POST / HTTP/1.0"),
        new HTTPHeaders());

    List<Routable> routes = Arrays.asList(
        new MockRoute("/", "GET"),
        new MockRoute("/", "OPTIONS"));

    exceptionRule.expect(Router.NoMatchingMethodForPath.class);
    exceptionRule.expectMessage("POST /");

    new Router(routes).route(request);
  }
}
