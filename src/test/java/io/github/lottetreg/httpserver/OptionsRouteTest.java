package io.github.lottetreg.httpserver;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OptionsRouteTest {
  @BeforeClass
  public static void clearRoutesOnStart() {
    Routable.clearStoredRoutables();
  }

  @After
  public void clearRoutes() {
    Routable.clearStoredRoutables();
  }

  @Test
  public void itHasAPath() {
    OptionsRoute route = new OptionsRoute("/");

    assertEquals("/", route.getPath());
  }

  @Test
  public void itsMethodIsOPTIONS() {
    OptionsRoute route = new OptionsRoute("/");

    assertEquals("OPTIONS", route.getMethod());
  }

  @Test
  public void itReturnsAnEmpty200ResponseWithTheAllowedMethods() {
    HTTPRequest emptyRequest = new HTTPRequest(new HTTPInitialLine("GET / HTTP/1.0"));

    new Route("/", "GET", "", "");
    OptionsRoute optionsRoute = new OptionsRoute("/");

    Response response = optionsRoute.getResponse(emptyRequest);

    assertEquals(200, response.getStatusCode());
    assertEquals("GET, OPTIONS", response.getHeaders().get("Allow"));
    assertEquals("", new String(response.getBody()));
  }
}
