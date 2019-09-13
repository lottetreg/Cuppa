package io.github.lottetreg.Cuppa;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

  @Test
  public void itHasAPath() {
    TestRoute route = new TestRoute("/", "");
    assertEquals("/", route.getPath());
  }

  @Test
  public void itHasAMethod() {
    TestRoute route = new TestRoute("", "GET");
    assertEquals("GET", route.getMethod());
  }

  @Test
  public void itReturnsTrueIfItHasAPath() {
    TestRoute route = new TestRoute("/", "");
    assertTrue(route.hasPath("/"));
  }

  @Test
  public void itReturnsFalseIfItDoesNotHaveAPath() {
    TestRoute route = new TestRoute("/", "");
    assertFalse(route.hasPath("/missing"));
  }

  @Test
  public void itReturnsTrueIfItHasAMethod() {
    TestRoute route = new TestRoute("", "GET");
    assertTrue(route.hasMethod("GET"));
  }

  @Test
  public void itReturnsFalseIfItDoesNotHaveAMethod() {
    TestRoute route = new TestRoute("", "GET");
    assertFalse(route.hasMethod("POST"));
  }
}
