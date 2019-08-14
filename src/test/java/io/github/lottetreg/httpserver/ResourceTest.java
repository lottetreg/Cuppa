package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class ResourceTest {
  private HTTPRequest emptyHTTPRequest() {
    return new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());
  }

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itHasAPath() {
    Resource resource = new Resource("/", "", "");
    assertEquals("/", resource.getPath());
  }

  @Test
  public void itHasAMethod() {
    Resource resource = new Resource("", "GET", "");
    assertEquals("GET", resource.getMethod());
  }

  @Test
  public void itHasAResourcePath() {
    Resource resource = new Resource("", "", "/");
    assertEquals("/", resource.getResourcePath());
  }

  @Test
  public void itReturnsA200ResponseWithTheResource() {
    Resource resource = new Resource("", "", "/src/test/java/io/github/lottetreg/httpserver/support/index.html");

    Response response = resource.getResponse(emptyHTTPRequest());

    assertEquals(200, response.getStatusCode());
    assertEquals("<h1>Hello, World!</h1>\n", new String(response.getBody()));
    assertEquals("text/html", response.getHeaders().get("Content-Type"));
  }

  @Test
  public void itThrowsAnExceptionIfTheResourceIsMissing() {
    Resource resource = new Resource("", "", "/missing_resource.html");

    exceptionRule.expect(Resource.MissingResource.class);
    exceptionRule.expectMessage("Could not find /missing_resource.html");

    resource.getResponse(emptyHTTPRequest());
  }
}
