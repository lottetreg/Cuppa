package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;

public class BaseControllerTest {
  public class TestController extends BaseController {
    TestController(HTTPRequest request) {
      super(request);
    }

    public void empty() {
    }

    public String hello() {
      return "Hello!";
    }

    public Path index() {
      return Path.of("/src/test/java/io/github/lottetreg/httpserver/support/index.html");
    }

    public Path missingFile() {
      return Path.of("/missing.html");
    }

    public void error() {
      throw new RuntimeException("Something went wrong");
    }
  }

  private HTTPRequest emptyRequest = new HTTPRequest(
      new HTTPInitialLine("GET / HTTP/1.0"),
      new HTTPHeaders());

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void callReturnsAResponseFromAnActionThatReturnsNothing() {
    TestController controller = new TestController(this.emptyRequest);

    Response response = controller.call("empty");

    assertEquals(200, response.getStatusCode());
    assertEquals("", new String(response.getBody()));
    assertEquals(new HashMap<>(), response.getHeaders());
  }

  @Test
  public void callReturnsAResponseFromAnActionThatReturnsAString() {
    TestController controller = new TestController(this.emptyRequest);

    Response response = controller.call("hello");

    assertEquals(200, response.getStatusCode());
    assertEquals("Hello!", new String(response.getBody()));
    assertEquals(new HashMap<>(Map.of("Content-Type", "text/plain")), response.getHeaders());
  }

  @Test
  public void callReturnsAResponseFromAnActionThatReturnsAPath() {
    TestController controller = new TestController(this.emptyRequest);

    Response response = controller.call("index");

    assertEquals(200, response.getStatusCode());
    assertEquals("<h1>Hello, World!</h1>\n", new String(response.getBody()));
    assertEquals(new HashMap<>(Map.of("Content-Type", "text/html")), response.getHeaders());
  }

  @Test
  public void callThrowsAnExceptionIfTheActionIsMissing() {
    TestController controller = new TestController(this.emptyRequest);

    exceptionRule.expect(Controllable.MissingControllerAction.class);
    exceptionRule.expectCause(instanceOf(NoSuchMethodException.class));
    exceptionRule.expectMessage("missingAction");

    controller.call("missingAction");
  }

  @Test
  public void callThrowsAnExceptionIfTheActionThrowsAnException() {
    TestController controller = new TestController(this.emptyRequest);

    exceptionRule.expect(Controllable.FailedToInvokeControllerAction.class);
    exceptionRule.expectCause(instanceOf(InvocationTargetException.class));
    exceptionRule.expectMessage("error");

    controller.call("error");
  }

  @Test
  public void callThrowsAnExceptionIfTheFileIsMissing() {
    TestController controller = new TestController(this.emptyRequest);

    exceptionRule.expect(Controllable.MissingResource.class);
    exceptionRule.expectCause(instanceOf(FileHelpers.MissingFile.class));
    exceptionRule.expectMessage("/missing.html");

    controller.call("missingFile");
  }
}
