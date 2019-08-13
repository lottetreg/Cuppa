package io.github.lottetreg.httpserver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Route extends BaseRoute {
  private String controllerName;
  private String actionName;
  private String controllersPackage;

  Route(String path, String method, String controllerName, String actionName) {
    super(path, method);
    this.controllerName = controllerName;
    this.actionName = actionName;
    this.controllersPackage = "io.github.lottetreg.httpserver.controllers";
  }

  Route(String path, String method, String controllerName, String actionName, String controllersPackage) {
    super(path, method);
    this.controllerName = controllerName;
    this.actionName = actionName;
    this.controllersPackage = controllersPackage;
  }

  public HTTPResponse getResponse(HTTPRequest request) {
    String controllerName = this.controllersPackage + "." + getControllerName();
    String actionName = getActionName();

    try {
      Class<?> controllerClass = Class.forName(controllerName);
      Constructor<?> constructor = controllerClass.getConstructor(HTTPRequest.class);
      Object controller = constructor.newInstance(request);
      Method action = controllerClass.getMethod(actionName);

      return (HTTPResponse) action.invoke(controller);

    } catch (ClassNotFoundException e) {
      throw new MissingController(controllerName);
    } catch (NoSuchMethodException e) {
      throw new MissingControllerAction(actionName, controllerName);
    } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
      throw new RuntimeException(e);
    }
  }

  public String getControllerName() {
    return this.controllerName;
  }

  public String getActionName() {
    return this.actionName;
  }

  static class MissingController extends RuntimeException {
    MissingController(String controller) {
      super("Could not find " + controller);
    }
  }

  static class MissingControllerAction extends RuntimeException {
    MissingControllerAction(String action, String controller) {
      super("Could not find " + action + " in " + controller);
    }
  }
}
