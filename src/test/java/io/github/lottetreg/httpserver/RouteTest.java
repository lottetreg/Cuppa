package io.github.lottetreg.httpserver;

import io.github.lottetreg.httpserver.controllers.BaseController;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class RouteTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itHasAPath() {
    Route route = new Route("/", "", "", "");
    assertEquals("/", route.getPath());
  }

  @Test
  public void itHasAMethod() {
    Route route = new Route("", "GET", "", "");
    assertEquals("GET", route.getMethod());
  }

  @Test
  public void itHasAControllerName() {
    Route route = new Route("", "", "ControllerName", "");
    assertEquals("ControllerName", route.getControllerName());
  }

  @Test
  public void itHasAnActionName() {
    Route route = new Route("", "", "", "ActionName");
    assertEquals("ActionName", route.getActionName());
  }

  @Test
  public void itReturnsTrueIfItHasAPath() {
    Route route = new Route("/", "", "", "");
    assertTrue(route.hasPath("/"));
  }

  @Test
  public void itReturnsFalseIfItDoesNotHaveAPath() {
    Route route = new Route("/", "", "", "");
    assertFalse(route.hasPath("/missing"));
  }

  @Test
  public void itReturnsTrueIfItHasAMethod() {
    Route route = new Route("", "GET", "", "");
    assertTrue(route.hasMethod("GET"));
  }

  @Test
  public void itReturnsFalseIfItDoesNotHaveAMethod() {
    Route route = new Route("", "GET", "", "");
    assertFalse(route.hasMethod("POST"));
  }

  static class Controller extends BaseController {
    public Controller(HTTPRequest request) {
      super(request);
    }

    public HTTPResponse empty() {
      return new HTTPResponse.Builder(200).build();
    }

    public HTTPResponse echo() {
      return new HTTPResponse.Builder(200)
          .setBody(this.request.getBody())
          .build();
    }

    public HTTPResponse missingResource() {
      throw new FileHelpers.MissingFile("/missing.html", new Throwable());
    }

    public HTTPResponse failure() {
      throw new RuntimeException(new Throwable());
    }
  }

  private String controllersPackage = "io.github.lottetreg.httpserver";

  @Test
  public void itReturnsAResponseFromTheAppropriateControllerAction() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());

    Route route = new Route("", "", "RouteTest$Controller", "empty", this.controllersPackage);

    HTTPResponse response = route.getResponse(request);

    assertEquals("HTTP/1.0 200 OK\r\n\r\n", response.toString());
  }

  @Test
  public void itReturnsAResponseFromTheAppropriateControllerActionAgain() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("POST / HTTP/1.0"),
        new HTTPHeaders(),
        "some body to love");


    Route route = new Route("", "", "RouteTest$Controller", "echo", this.controllersPackage);

    HTTPResponse response = route.getResponse(request);

    assertEquals("HTTP/1.0 200 OK\r\n\r\nsome body to love", response.toString());
  }

  @Test
  public void itReturnsA404ResponseIfTheViewIsMissing() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());

    Route route = new Route("", "", "RouteTest$Controller", "empty", this.controllersPackage);

    HTTPResponse response = route.getResponse(request);

    assertEquals("HTTP/1.0 200 OK\r\n\r\n", response.toString());
  }

  @Test
  public void itThrowsAnExceptionIfTheControllerIsMissing() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());

    exceptionRule.expect(Route.MissingController.class);
    exceptionRule.expectMessage("Could not find io.github.lottetreg.httpserver.MissingController");

    Route route = new Route("", "", "MissingController", "", this.controllersPackage);

    route.getResponse(request);
  }

  @Test
  public void itThrowsAnExceptionIfTheControllerActionIsMissing() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());

    exceptionRule.expect(Route.MissingControllerAction.class);
    exceptionRule.expectMessage(
        "Could not find missingAction in io.github.lottetreg.httpserver.RouteTest$Controller");

    Route route = new Route("", "", "RouteTest$Controller", "missingAction", this.controllersPackage);

    route.getResponse(request);
  }

  @Test
  public void itThrowsAnExceptionIfTheActionFailsBecauseAResourceIsMissing() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());

    exceptionRule.expect(Routable.MissingResource.class);
    exceptionRule.expectMessage("Could not find /missing.html");

    Route route = new Route("", "", "RouteTest$Controller", "missingResource", this.controllersPackage);

    route.getResponse(request);
  }

  @Test
  public void itThrowsAnExceptionIfTheActionFailsForAnyOtherReason() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());

    exceptionRule.expect(Route.FailedControllerAction.class);
    exceptionRule.expectMessage("Failed to complete failure in io.github.lottetreg.httpserver.RouteTest$Controller");

    Route route = new Route("", "", "RouteTest$Controller", "failure", this.controllersPackage);

    route.getResponse(request);
  }
}
