package io.github.lottetreg.Cuppa;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Route extends BaseRoute {
  private Class controller;
  private String controllerName;
  private String actionName;

  public Route(String path, String method, String controllerName, String actionName) {
    super(path, method);
    this.controllerName = controllerName;
    this.actionName = actionName;
  }

  public Route(String path, String method, Class controller, String actionName) {
    super(path, method);
    this.controller = controller;
    this.actionName = actionName;
  }

  public Response getResponse(HTTPRequest request) {
    String controllerName = getCompleteControllerName();
    String actionName = getActionName();

    try {
      Constructor<?> constructor = this.controller.getConstructor();
      Controllable controller = ((Controllable) constructor.newInstance()).setRequest(request);

      return controller.call(actionName);

    } catch (NoSuchMethodException e) {
      throw new MissingControllerConstructor(controllerName, e);

    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new FailedToInstantiateController(controllerName, e);

    } catch (Controllable.MissingResource e) {
      throw new MissingResource(e.getMessage(), e);
    }
  }

//  public Response getResponse(HTTPRequest request) {
//    String controllerName = getCompleteControllerName();
//    String actionName = getActionName();
//
//    try {
//      Class<?> controllerClass = Class.forName(controllerName);
//      Constructor<?> constructor = controllerClass.getConstructor();
//      Controllable controller = ((Controllable) constructor.newInstance()).setRequest(request);
//
//      return controller.call(actionName);
//
//    } catch (ClassNotFoundException e) {
//      throw new MissingController(controllerName, e);
//
//    } catch (NoSuchMethodException e) {
//      throw new MissingControllerConstructor(controllerName, e);
//
//    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
//      throw new FailedToInstantiateController(controllerName, e);
//
//    } catch (Controllable.MissingResource e) {
//      throw new MissingResource(e.getMessage(), e);
//    }
//  }

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

  static class MissingControllerConstructor extends RuntimeException {
    MissingControllerConstructor(String controller, Throwable cause) {
      super(controller, cause);
    }
  }

  static class FailedToInstantiateController extends RuntimeException {
    FailedToInstantiateController(String controller, Throwable cause) {
      super(controller, cause);
    }
  }
}
