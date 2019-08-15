package io.github.lottetreg.httpserver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;

import static io.github.lottetreg.httpserver.FileHelpers.getContentType;
import static io.github.lottetreg.httpserver.FileHelpers.readFile;

public class BaseController {
  private HTTPRequest request;
  private HashMap<String, String> headers;

  public BaseController(HTTPRequest request) {
    this.request = request;
    this.headers = new HashMap<>();
  }

  HTTPRequest getRequest() {
    return this.request;
  }

  void addHeader(String key, String value) {
    this.headers.put(key, value);
  }

  Response call(String actionName) {
    try {
      Method action = getClass().getMethod(actionName);
      Object result = action.invoke(this);

      byte[] body = new byte[]{};

      if (result instanceof String) {
        addHeader("Content-Type", "text/plain");
        body = ((String) result).getBytes();

      } else if (result instanceof Path) {
        Path filePath = (Path) result;
        addHeader("Content-Type", getContentType(filePath));
        body = readFile(filePath);
      }

      return new Response(200, body, this.headers);

    } catch (NoSuchMethodException e) {
      throw new MissingControllerAction(actionName, e);

    } catch (InvocationTargetException e) {
      throw wrappedActionInvocationException(actionName, e.getCause());

    } catch (IllegalAccessException e) {
      throw new InaccessibleControllerAction(actionName, e);
    }
  }

  private RuntimeException wrappedActionInvocationException(String action, Throwable cause) {
    return cause instanceof FileHelpers.MissingFile
        ? new MissingResource(cause.getMessage(), cause)
        : new ControllerActionFailed(action, cause);
  }

  static class MissingControllerAction extends RuntimeException {
    MissingControllerAction(String action, Throwable cause) {
      super(action, cause);
    }
  }

  static class InaccessibleControllerAction extends RuntimeException {
    InaccessibleControllerAction(String action, Throwable cause) {
      super(action, cause);
    }
  }

  class MissingResource extends RuntimeException {
    MissingResource(String resourcePath, Throwable cause) {
      super(resourcePath, cause);
    }
  }

  static class ControllerActionFailed extends RuntimeException {
    ControllerActionFailed(String action, Throwable cause) {
      super(action, cause);
    }
  }
}
