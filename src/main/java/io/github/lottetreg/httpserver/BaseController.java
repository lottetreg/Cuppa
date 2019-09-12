package io.github.lottetreg.httpserver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;

import static io.github.lottetreg.httpserver.FileHelpers.getContentType;
import static io.github.lottetreg.httpserver.FileHelpers.readFile;

class BaseController implements Controllable {
  private HTTPRequest request;
  private HashMap<String, String> headers;
  private HashMap<String, String> data;

  BaseController(HTTPRequest request) {
    this.request = request;
    this.headers = new HashMap<>();
    this.data = new HashMap<>();
  }

  HTTPRequest getRequest() {
    return this.request;
  }

  void addHeader(String key, String value) {
    this.headers.put(key, value);
  }

  void addData(String key, String value) {
    this.data.put(key, value);
  }

  public Response call(String actionName) {
    try {
      Method action = getClass().getMethod(actionName);
      Object result = action.invoke(this);

      byte[] body = new byte[]{};

      if (result instanceof String) {
        addHeader("Content-Type", "text/plain");
        body = ((String) result).getBytes();

      } else if (result instanceof Template) {
        Template template = (Template) result;
        addHeader("Content-Type", getContentType(template.getPath()));
        body = template.render(this.data);

      } else if (result instanceof Path) {
        Path filePath = (Path) result;
        addHeader("Content-Type", getContentType(filePath));
        body = readFile(filePath);
      }

      return new Response(200, body, this.headers);

    } catch (NoSuchMethodException e) {
      throw new MissingControllerAction(actionName, e);

    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new FailedToInvokeControllerAction(actionName, e);

    } catch (FileHelpers.MissingFile e) {
      throw new MissingResource(e.getMessage(), e);

    } catch (TemplateRenderer.MissingContextKey e) {
      throw new MissingTemplateData(e.getMessage(), e);
    }
  }
}
