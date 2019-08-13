package io.github.lottetreg.httpserver.controllers;

import io.github.lottetreg.httpserver.HTTPRequest;

public class BaseController {
  public HTTPRequest request;

  public BaseController(HTTPRequest request) {
    this.request = request;
  }
}
