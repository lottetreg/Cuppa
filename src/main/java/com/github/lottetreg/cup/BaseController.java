package com.github.lottetreg.cup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;

import static com.github.lottetreg.cup.FileHelpers.getContentType;
import static com.github.lottetreg.cup.FileHelpers.readFile;

public class BaseController implements Controllable {
  private Request request;
  private HashMap<String, String> headers = new HashMap<>();
  private HashMap<String, String> data = new HashMap<>();

  public Request getRequest() {
    return this.request;
  }

  public void addHeader(String key, String value) {
    this.headers.put(key, value);
  }

  public void addData(String key, String value) {
    this.data.put(key, value);
  }

  public Controllable setRequest(Request request) {
    this.request = request;
    return this;
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
