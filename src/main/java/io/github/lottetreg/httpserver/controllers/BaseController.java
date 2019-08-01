package io.github.lottetreg.httpserver.controllers;

import io.github.lottetreg.httpserver.FileHelpers;
import io.github.lottetreg.httpserver.HTTPRequest;

public class BaseController {
  public HTTPRequest request;

  public BaseController(HTTPRequest request) {
    this.request = request;
  }

  protected byte[] readFile(String filePath) {
    return FileHelpers.readFile(filePath);
  }

  protected String getContentType(String filePath) {
    return FileHelpers.getContentType(filePath);
  }
}
