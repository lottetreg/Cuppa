package io.github.lottetreg.Cuppa;

interface Controllable {
  Response call(String actionName);

  class MissingControllerAction extends RuntimeException {
    MissingControllerAction(String action, Throwable cause) {
      super(action, cause);
    }
  }

  class FailedToInvokeControllerAction extends RuntimeException {
    FailedToInvokeControllerAction(String action, Throwable cause) {
      super(action, cause);
    }
  }

  class MissingResource extends RuntimeException {
    MissingResource(String resourcePath, Throwable cause) {
      super(resourcePath, cause);
    }
  }

  class MissingTemplateData extends RuntimeException {
    MissingTemplateData(String missingData, Throwable cause) {
      super(missingData, cause);
    }
  }
}
