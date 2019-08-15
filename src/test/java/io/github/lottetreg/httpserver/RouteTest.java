package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
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

  public static class Controller extends BaseController {
    public Controller(HTTPRequest request) {
      super(request);
    }

    public void empty() {}

    public String echo() {
      return this.request.getBody();
    }

    public void missingResource() {
      throw new FileHelpers.MissingFile("/missing.html", new Throwable());
    }

    public void failure() {
      throw new RuntimeException(new Throwable());
    }
  }

  @Test
  public void itReturnsAResponseFromTheAppropriateControllerAction() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());

    Route route = new Route("", "", "RouteTest$Controller", "empty");

    Response response = route.getResponse(request);

    assertEquals(200, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals(new HashMap<>(), response.getHeaders());
  }

  @Test
  public void itReturnsAResponseFromTheAppropriateControllerActionAgain() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("POST / HTTP/1.0"),
        new HTTPHeaders(),
        "some body to love");


    Route route = new Route("", "", "RouteTest$Controller", "echo");

    Response response = route.getResponse(request);

    assertEquals(200, response.getStatusCode());
    assertEquals("some body to love", new String(response.getBody()));
    assertEquals(new HashMap<>(Map.of("Content-Type", "text/plain")), response.getHeaders());
  }

  @Test
  public void itThrowsAnExceptionIfTheControllerIsMissing() {
    HTTPRequest request = new HTTPRequest(
        new HTTPInitialLine("GET / HTTP/1.0"),
        new HTTPHeaders());

    exceptionRule.expect(Route.MissingController.class);
    exceptionRule.expectMessage("io.github.lottetreg.httpserver.MissingController");

    Route route = new Route("", "", "MissingController", "");

    route.getResponse(request);
  }

//  @Test
//  public void itThrowsAnExceptionIfTheControllerActionIsMissing() {
//    HTTPRequest request = new HTTPRequest(
//        new HTTPInitialLine("GET / HTTP/1.0"),
//        new HTTPHeaders());
//
//    exceptionRule.expect(Route.MissingControllerAction.class);
//    exceptionRule.expectMessage(
//        "io.github.lottetreg.httpserver.RouteTest$Controller");
//
//    Route route = new Route("", "", "RouteTest$Controller", "missingAction");
//
//    route.getResponse(request);
//  }
//
//  @Test
//  public void itThrowsAnExceptionIfTheActionFailsBecauseAResourceIsMissing() {
//    HTTPRequest request = new HTTPRequest(
//        new HTTPInitialLine("GET / HTTP/1.0"),
//        new HTTPHeaders());
//
//    exceptionRule.expect(Routable.MissingResource.class);
//    exceptionRule.expectMessage("Could not find /missing.html");
//
//    Route route = new Route("", "", "RouteTest$Controller", "missingResource");
//
//    route.getResponse(request);
//  }
//
//  @Test
//  public void itThrowsAnExceptionIfTheActionFailsForAnyOtherReason() {
//    HTTPRequest request = new HTTPRequest(
//        new HTTPInitialLine("GET / HTTP/1.0"),
//        new HTTPHeaders());
//
//    exceptionRule.expect(Route.FailedControllerAction.class);
//    exceptionRule.expectMessage("Failed to complete failure in io.github.lottetreg.httpserver.RouteTest$Controller");
//
//    Route route = new Route("", "", "RouteTest$Controller", "failure");
//
//    route.getResponse(request);
//  }
}
