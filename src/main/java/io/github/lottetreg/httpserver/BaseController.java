package io.github.lottetreg.httpserver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;

public class BaseController {
  public HTTPRequest request;
  public HashMap<String, String> headers;

  public BaseController(HTTPRequest request) {
    this.request = request;
    this.headers = new HashMap<>();
  }

  public Response call(String actionName) throws MissingResource, FailedControllerAction {
    try {
      Method action = getClass().getMethod(actionName);
      Object result = action.invoke(this);

      byte[] body = new byte[]{};

      if (result instanceof String) {
        this.headers.put("Content-Type", "text/plain");
        body = ((String) result).getBytes();

      } else if (result instanceof Path) {
        Path filePath = (Path) result;
        this.headers.put("Content-Type", FileHelpers.getContentType(filePath));
        body = FileHelpers.readFile(filePath);
      }

      return new Response(200, body, this.headers);

    } catch (NoSuchMethodException e) {
      throw new RuntimeException("Can't find action in controller");

    } catch (InvocationTargetException e) {
      throw wrappedActionInvocationException(e.getCause());

    } catch (IllegalAccessException e) {
      throw new RuntimeException("Action needs to be accessible");
    }
  }

  private RuntimeException wrappedActionInvocationException(Throwable cause) {
    if (cause instanceof FileHelpers.MissingFile) {
      return new MissingResource(cause.getMessage(), cause);
    } else {
      return new FailedControllerAction(cause);
    }
  }

  class MissingResource extends RuntimeException {
    MissingResource(String resourcePath, Throwable cause) {
      super(resourcePath, cause);
    }
  }

  static class FailedControllerAction extends RuntimeException {
    FailedControllerAction(Throwable cause) {
      super(cause);
    }
  }
}
