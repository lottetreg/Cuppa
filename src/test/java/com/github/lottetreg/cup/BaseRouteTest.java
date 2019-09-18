package com.github.lottetreg.cup;

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
    public Response getResponse(Request request) {
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
}
