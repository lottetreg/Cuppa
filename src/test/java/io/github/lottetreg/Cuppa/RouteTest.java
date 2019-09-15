//package io.github.lottetreg.Cuppa;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.util.HashMap;
//
//import static junit.framework.TestCase.assertTrue;
//import static org.hamcrest.CoreMatchers.instanceOf;
//import static org.junit.Assert.*;
//
//public class RouteTest {
//  @Rule
//  public ExpectedException exceptionRule = ExpectedException.none();
//
//  @Test
//  public void itHasAPath() {
//    Route route = new Route("/", "", "", "");
//    assertEquals("/", route.getPath());
//  }
//
//  @Test
//  public void itHasAMethod() {
//    Route route = new Route("", "GET", "", "");
//    assertEquals("GET", route.getMethod());
//  }
//
//  @Test
//  public void itHasAControllerName() {
//    Route route = new Route("", "", "ControllerName", "");
//    assertEquals("ControllerName", route.getControllerName());
//  }
//
//  @Test
//  public void itHasAnActionName() {
//    Route route = new Route("", "", "", "ActionName");
//    assertEquals("ActionName", route.getActionName());
//  }
//
//  @Test
//  public void itReturnsTrueIfItHasAPath() {
//    Route route = new Route("/", "", "", "");
//    assertTrue(route.hasPath("/"));
//  }
//
//  @Test
//  public void itReturnsFalseIfItDoesNotHaveAPath() {
//    Route route = new Route("/", "", "", "");
//    assertFalse(route.hasPath("/missing"));
//  }
//
//  @Test
//  public void itReturnsTrueIfItHasAMethod() {
//    Route route = new Route("", "GET", "", "");
//    assertTrue(route.hasMethod("GET"));
//  }
//
//  @Test
//  public void itReturnsFalseIfItDoesNotHaveAMethod() {
//    Route route = new Route("", "GET", "", "");
//    assertFalse(route.hasMethod("POST"));
//  }
//
//  private HTTPRequest emptyRequest() throws IOException {
//    return RequestHelpers.buildHTTPRequest("GET", "/");
//  }
//
//  private Route newRouteForController(String controller) {
//    return new Route("", "", controller, "");
//  }
//
//  public static class Controller implements Controllable {
//    HTTPRequest request;
//
//    public Controllable setRequest(HTTPRequest request) {
//      this.request = request;
//      return this;
//    }
//
//    public Response call(String actionName) {
//      return new Response(200);
//    }
//  }
//
//  @Test
//  public void itReturnsAResponseFromCallingTheController() throws IOException {
//    Route route = newRouteForController("RouteTest$Controller");
//
//    Response response = route.getResponse(emptyRequest());
//
//    assertEquals(200, response.getStatusCode());
//    assertEquals("", new String(response.getBody()));
//    assertEquals(new HashMap<>(), response.getHeaders());
//  }
//
//  @Test
//  public void itThrowsAnExceptionIfTheControllerIsMissing() throws IOException {
//    exceptionRule.expect(Route.MissingController.class);
//    exceptionRule.expectMessage("io.github.lottetreg.Cuppa.MissingController");
//
//    Route route = newRouteForController("MissingController");
//
//    route.getResponse(emptyRequest());
//  }
//
//  public static class BrokenConstructor implements Controllable {
//    HTTPRequest request;
//
//    public BrokenConstructor() {
//      throw new RuntimeException();
//    }
//
//    public Controllable setRequest(HTTPRequest request) {
//      this.request = request;
//      return this;
//    }
//
//    public Response call(String actionName) {
//      return new Response(200);
//    }
//  }
//
//  @Test
//  public void itThrowsAnExceptionIfTheControllerConstructorThrowsAnException() throws IOException {
//    exceptionRule.expect(Route.FailedToInstantiateController.class);
//    exceptionRule.expectCause(instanceOf(InvocationTargetException.class));
//    exceptionRule.expectMessage("io.github.lottetreg.Cuppa.RouteTest$BrokenConstructor");
//
//    Route route = newRouteForController("RouteTest$BrokenConstructor");
//
//    route.getResponse(emptyRequest());
//  }
//
//  public static class MissingResourceController implements Controllable {
//    HTTPRequest request;
//
//    public Controllable setRequest(HTTPRequest request) {
//      this.request = request;
//      return this;
//    }
//
//    public Response call(String actionName) {
//      throw new Controllable.MissingResource("/missing.html", new Throwable());
//    }
//  }
//
//  @Test
//  public void itThrowsAnExceptionIfCallingTheControllerThrowsAMissingResourceException() throws IOException {
//    exceptionRule.expect(Route.MissingResource.class);
//    exceptionRule.expectMessage("/missing.html");
//
//    Route route = newRouteForController("RouteTest$MissingResourceController");
//
//    route.getResponse(emptyRequest());
//  }
//}
