package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RouterTest {

  static class MockRoute extends BaseRoute {
    MockRoute(String path, String method) {
      super(path, method);
    }

    public Response getResponse(HTTPRequest request) {
      return new Response(200);
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

    Response response = new Router(routes).route(request);

    assertEquals(200, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals(new HashMap<>(), response.getHeaders());
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
