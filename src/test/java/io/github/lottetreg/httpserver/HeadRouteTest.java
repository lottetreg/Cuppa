package io.github.lottetreg.httpserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HeadRouteTest {
  @Test
  public void itHasAPath() {
    HeadRoute route = new HeadRoute("/");

    assertEquals("/", route.getPath());
  }

  @Test
  public void itsMethodIsHEAD() {
    HeadRoute route = new HeadRoute("/");

    assertEquals("HEAD", route.getMethod());
  }

  @Test
  public void itReturnsAnEmpty200Response() {
    HTTPRequest emptyRequest = new HTTPRequest(new HTTPInitialLine("GET / HTTP/1.0"));

    HeadRoute route = new HeadRoute("/");

    Response response = route.getResponse(emptyRequest);

    assertEquals(200, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
  }
}
