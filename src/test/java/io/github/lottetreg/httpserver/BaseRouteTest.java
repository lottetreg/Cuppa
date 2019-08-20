package io.github.lottetreg.httpserver;

import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BaseRouteTest {
  public class TestRoute extends BaseRoute {
    TestRoute(String path, String method) {
      super(path, method);
    }

    @Override
    public Response getResponse(HTTPRequest request) {
      return null;
    }
  }

  @BeforeClass
  public static void initialCleanUp() {
    BaseRoute.clearRoutes();
  }

  @After
  public void clearRoutes() {
    BaseRoute.clearRoutes();
  }

  @Test
  public void itReturnsTheAllowedMethods() {
    new TestRoute("/", "GET");
    new TestRoute("/", "POST");

    String allowedMethods = BaseRoute.getAllowedMethods("/");

    List<String> splitAllowedMethods = List.of(allowedMethods.split(", "));

    assertEquals(2, splitAllowedMethods.size());
    assertEquals(new HashSet<>(List.of("GET", "POST")), new HashSet<>(splitAllowedMethods));
  }

  @Test
  public void itDoesNotReturnAllowedMethodsForDifferentPaths() {
    new TestRoute("/", "GET");
    new TestRoute("/something_else", "POST");

    String allowedMethods = BaseRoute.getAllowedMethods("/");

    List<String> splitAllowedMethods = List.of(allowedMethods.split(", "));

    assertEquals(1, splitAllowedMethods.size());
    assertEquals(new HashSet<>(List.of("GET")), new HashSet<>(splitAllowedMethods));
  }

  @Test
  public void itDoesNotReturnRedundantAllowedMethods() {
    new TestRoute("/", "GET");
    new TestRoute("/", "GET");

    String allowedMethods = BaseRoute.getAllowedMethods("/");

    List<String> splitAllowedMethods = List.of(allowedMethods.split(", "));

    assertEquals(1, splitAllowedMethods.size());
    assertEquals(new HashSet<>(List.of("GET")), new HashSet<>(splitAllowedMethods));
  }

  @Test
  public void itReturnsAnEmptyStringIfThereAreNoAllowedMethods() {
    new TestRoute("/", "GET");
    new TestRoute("/", "POST");

    String allowedMethods = BaseRoute.getAllowedMethods("/something_else");

    assertEquals("", allowedMethods);
  }
}
