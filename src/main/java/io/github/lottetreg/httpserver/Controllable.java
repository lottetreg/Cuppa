package io.github.lottetreg.httpserver;

interface Controllable {
  Response call(String actionName);

  class MissingControllerAction extends RuntimeException {
    MissingControllerAction(String action, Throwable cause) {
      super(action, cause);
    }
  }

  class InaccessibleControllerAction extends RuntimeException {
    InaccessibleControllerAction(String action, Throwable cause) {
      super(action, cause);
    }
  }

  class MissingResource extends RuntimeException {
    MissingResource(String resourcePath, Throwable cause) {
      super(resourcePath, cause);
    }
  }

  class ControllerActionFailed extends RuntimeException {
    ControllerActionFailed(String action, Throwable cause) {
      super(action, cause);
    }
  }
}
