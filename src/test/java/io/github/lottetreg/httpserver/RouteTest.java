package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.instanceOf;
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

  private HTTPRequest emptyRequest = new HTTPRequest(
      new HTTPInitialLine("GET / HTTP/1.0"),
      new HTTPHeaders());

  private Route newRouteForController(String controller) {
    return new Route("", "", controller, "");
  }

  public static class Controller implements Controllable {
    public Controller(HTTPRequest request) {
    }

    public Response call(String actionName) {
      return new Response(200);
    }
  }

  @Test
  public void itReturnsAResponseFromCallingTheController() {
    ;
    Route route = newRouteForController("RouteTest$Controller");

    Response response = route.getResponse(this.emptyRequest);

    assertEquals(200, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals(new HashMap<>(), response.getHeaders());
  }

  @Test
  public void itThrowsAnExceptionIfTheControllerIsMissing() {
    exceptionRule.expect(Route.MissingController.class);
    exceptionRule.expectMessage("io.github.lottetreg.httpserver.MissingController");

    Route route = newRouteForController("MissingController");

    route.getResponse(this.emptyRequest);
  }

  public static class MissingConstructor implements Controllable {
    public Response call(String actionName) {
      return new Response(200);
    }
  }

  @Test
  public void itThrowsAnExceptionIfTheControllerConstructorIsMissing() {
    exceptionRule.expect(Route.MissingControllerConstructor.class);
    exceptionRule.expectMessage("io.github.lottetreg.httpserver.RouteTest$MissingConstructor");

    Route route = newRouteForController("RouteTest$MissingConstructor");

    route.getResponse(this.emptyRequest);
  }

  public static class BrokenConstructor implements Controllable {
    public BrokenConstructor(HTTPRequest request) {
      throw new RuntimeException();
    }

    public Response call(String actionName) {
      return new Response(200);
    }
  }

  @Test
  public void itThrowsAnExceptionIfTheControllerConstructorThrowsAnException() {
    exceptionRule.expect(Route.FailedToInstantiateController.class);
    exceptionRule.expectCause(instanceOf(InvocationTargetException.class));
    exceptionRule.expectMessage("io.github.lottetreg.httpserver.RouteTest$BrokenConstructor");

    Route route = newRouteForController("RouteTest$BrokenConstructor");

    route.getResponse(this.emptyRequest);
  }

  public static class MissingResourceController implements Controllable {
    public MissingResourceController(HTTPRequest request) {
    }

    public Response call(String actionName) {
      throw new Controllable.MissingResource("/missing.html", new Throwable());
    }
  }

  @Test
  public void itThrowsAnExceptionIfCallingTheControllerThrowsAMissingResourceException() {
    exceptionRule.expect(Route.MissingResource.class);
    exceptionRule.expectMessage("/missing.html");

    Route route = newRouteForController("RouteTest$MissingResourceController");

    route.getResponse(this.emptyRequest);
  }
}
