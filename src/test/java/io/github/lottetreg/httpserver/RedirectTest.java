package io.github.lottetreg.httpserver;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class RedirectTest {
  @Test
  public void itHasAPath() {
    Redirect redirect = new Redirect("/", "", "");
    assertEquals("/", redirect.getPath());
  }

  @Test
  public void itHasAMethod() {
    Redirect redirect = new Redirect("", "GET", "");
    assertEquals("GET", redirect.getMethod());
  }

  @Test
  public void itHasARedirectPath() {
    Redirect redirect = new Redirect("", "", "/redirect");
    assertEquals("/redirect", redirect.getRedirectPath());
  }

  @Test
  public void itReturns301ResponseWithTheRedirectPathInTheHeaders() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders(Arrays.asList("Host: www.example.com")));

    Redirect redirect = new Redirect("", "", "/some_other_path");

    Response response = redirect.getResponse(request);

    assertEquals(301, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals("http://www.example.com/some_other_path", response.getHeaders().get("Location"));
  }
}
