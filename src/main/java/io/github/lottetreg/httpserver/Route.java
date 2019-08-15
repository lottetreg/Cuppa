package io.github.lottetreg.httpserver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Route extends BaseRoute {
  private String controllerName;
  private String actionName;

  Route(String path, String method, String controllerName, String actionName) {
    super(path, method);
    this.controllerName = controllerName;
    this.actionName = actionName;
  }

  public Response getResponse(HTTPRequest request) {
    String controllerName = getCompleteControllerName();
    String actionName = getActionName();

    try {
      Class<?> controllerClass = Class.forName(controllerName);
      Constructor<?> constructor = controllerClass.getConstructor(HTTPRequest.class);
      BaseController controller = (BaseController) constructor.newInstance(request);

      return controller.call(actionName);

    } catch (ClassNotFoundException e) {
      throw new MissingController(controllerName, e);

    } catch (NoSuchMethodException e) {
      throw new RuntimeException("Can't find constructor for BaseController");

    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Failed to create new instance of BaseController");
    }
  }

  public String getControllerName() {
    return this.controllerName;
  }

  public String getActionName() {
    return this.actionName;
  }

  public String getCompleteControllerName() {
    return getClass().getPackageName() + "." + getControllerName();
  }

  static class MissingController extends RuntimeException {
    MissingController(String controller, Throwable cause) {
      super(controller, cause);
    }
  }

  static class MissingControllerAction extends RuntimeException {
    MissingControllerAction(String action, String controller) {
      super("Could not find " + action + " in " + controller);
    }
  }

  static class FailedControllerAction extends RuntimeException {
    FailedControllerAction(String action, String controller, Throwable cause) {
      super("Failed to complete " + action + " in " + controller, cause);
    }
  }
}
