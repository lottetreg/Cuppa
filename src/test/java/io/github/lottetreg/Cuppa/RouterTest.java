//package io.github.lottetreg.Cuppa;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class RouterTest {
//
//  static class MockRoute extends BaseRoute {
//    MockRoute(String path, String method) {
//      super(path, method);
//    }
//
//    public Response getResponse(Request request) {
//      return new Response(200);
//    }
//  }
//
//  @Rule
//  public ExpectedException exceptionRule = ExpectedException.none();
//
//  @Test
//  public void itReturnsTheResponseFromTheMatchingRoute() throws IOException {
//    com.github.lottetreg.saucer.HttpRequest request = HTTPRequestHelpers.buildHTTPRequest("GET", "/");
//
//    List<Responsive> routes = Arrays.asList(new MockRoute("/", "GET"));
//
//    Response response = new Router(routes).route(request);
//
//    assertEquals(200, response.getStatusCode());
//    assertEquals("", new String(response.getBody()));
//    assertEquals(new HashMap<>(), response.getHeaders());
//  }
//
//  @Test
//  public void itReturnsAnExceptionIfThereIsNoRouteWithMatchingPath() throws IOException {
//    com.github.lottetreg.saucer.HttpRequest request = HTTPRequestHelpers.buildHTTPRequest("GET", "/no_match");
//
//    List<Responsive> routes = Arrays.asList(new MockRoute("", ""));
//
//    exceptionRule.expect(Router.NoMatchingPath.class);
//    exceptionRule.expectMessage("/no_match");
//
//    new Router(routes).route(request);
//  }
//
//  @Test
//  public void itReturnsAnExceptionIfThereIsNoMatchingMethodForTheRoute() throws IOException {
//    com.github.lottetreg.saucer.HttpRequest request = HTTPRequestHelpers.buildHTTPRequest("POST", "/");
//
//    List<Responsive> routes = Arrays.asList(
//        new MockRoute("/", "GET"),
//        new MockRoute("/", "OPTIONS"));
//
//    exceptionRule.expect(Router.NoMatchingMethodForPath.class);
//    exceptionRule.expectMessage("POST /");
//
//    new Router(routes).route(request);
//  }
//}
