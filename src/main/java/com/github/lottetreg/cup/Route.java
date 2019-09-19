package com.github.lottetreg.cup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Route extends BaseRoute {
  private Class controller;
  private String actionName;

  public Route(String method, String path, Class controller, String actionName) {
    super(method, path);
    this.controller = controller;
    this.actionName = actionName;
  }

  public Response getResponse(Request request) {
    String controllerName = getControllerName();
    String actionName = getActionName();

    try {
      Constructor<?> constructor = this.controller.getConstructor();
      Controllable controller = ((Controllable) constructor.newInstance()).setRequest(request);

      return controller.call(actionName); // define new Response(200..) in here instead of BaseController

    } catch (NoSuchMethodException e) {
      throw new MissingControllerConstructor(controllerName, e);

    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new FailedToInstantiateController(controllerName, e);

    } catch (Controllable.MissingResource e) {
      throw new MissingResource(e.getMessage(), e);
    }
  }

  private String getControllerName() {
    return this.controller.getSimpleName();
  }

  private String getActionName() {
    return this.actionName;
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
