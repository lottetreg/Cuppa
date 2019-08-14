package io.github.lottetreg.httpserver.controllers;

import io.github.lottetreg.httpserver.FileHelpers;
import io.github.lottetreg.httpserver.HTTPRequest;

import java.util.HashMap;
import java.util.OptionalInt;

public class BaseController {
  public HTTPRequest request;
  public OptionalInt statusCode;
  public HashMap<String, String> headers;

  public BaseController(HTTPRequest request) {
    this.request = request;
    this.statusCode = OptionalInt.empty();
    this.headers = new HashMap<>();
  }

  // in call(): method.getReturnType()==Void.Type to check for void methods,
  // can also check for String vs byte[], throw exception if return type not allowed?

  protected byte[] readFile(String filePath) {
    return FileHelpers.readFile(filePath);
  }

  protected String getContentType(String filePath) {
    return FileHelpers.getContentType(filePath);
  }
}
