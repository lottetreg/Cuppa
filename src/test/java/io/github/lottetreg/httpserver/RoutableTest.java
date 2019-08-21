package io.github.lottetreg.httpserver;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RoutableTest {

  public class TestRoute implements Routable {
    protected String path;
    protected String method;

    TestRoute(String path, String method) {
      this.path = path;
      this.method = method;
    }

    public String getPath() {
      return this.path;
    }

    public String getMethod() {
      return this.method;
    }

    public Boolean hasPath(String path) {
      return getPath().equals(path);
    }

    public Boolean hasMethod(String method) {
      return getMethod().equals(method);
    }

    public String getAllowedMethods() {
      return null;
    }

    public Response getResponse(HTTPRequest request) {
      return null;
    }
  }

  @BeforeClass
  public static void clearRoutesOnStart() {
    Routable.clearStore();
  }

  @After
  public void clearRoutes() {
    Routable.clearStore();
  }

  @Test
  public void itReturnsTheAllowedMethods() {
    Routable.store(new TestRoute("/", "GET"));
    Routable.store(new TestRoute("/", "POST"));

    String allowedMethods = Routable.getAllowedMethods("/");

    List<String> splitAllowedMethods = List.of(allowedMethods.split(", "));

    assertEquals(2, splitAllowedMethods.size());
    assertEquals(new HashSet<>(List.of("GET", "POST")), new HashSet<>(splitAllowedMethods));
  }

  @Test
  public void itDoesNotReturnAllowedMethodsForDifferentPaths() {
    Routable.store(new TestRoute("/", "GET"));
    Routable.store(new TestRoute("/something_else", "POST"));

    String allowedMethods = Routable.getAllowedMethods("/");

    List<String> splitAllowedMethods = List.of(allowedMethods.split(", "));

    assertEquals(1, splitAllowedMethods.size());
    assertEquals(new HashSet<>(List.of("GET")), new HashSet<>(splitAllowedMethods));
  }

  @Test
  public void itDoesNotReturnRedundantAllowedMethods() {
    Routable.store(new TestRoute("/", "GET"));
    Routable.store(new TestRoute("/", "GET"));

    String allowedMethods = Routable.getAllowedMethods("/");

    List<String> splitAllowedMethods = List.of(allowedMethods.split(", "));

    assertEquals(1, splitAllowedMethods.size());
    assertEquals(new HashSet<>(List.of("GET")), new HashSet<>(splitAllowedMethods));
  }

  @Test
  public void itReturnsAnEmptyStringIfThereAreNoAllowedMethods() {
    Routable.store(new TestRoute("/", "GET"));
    Routable.store(new TestRoute("/", "POST"));

    String allowedMethods = Routable.getAllowedMethods("/something_else");

    assertEquals("", allowedMethods);
  }

  @Test
  public void itReturnsAnEmptyStringIfThereAreNoStoredRoutables() {
    assertEquals("", Routable.getAllowedMethods("/"));
  }
}
