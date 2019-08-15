package io.github.lottetreg.httpserver.controllers;

import io.github.lottetreg.httpserver.FileHelpers;
import io.github.lottetreg.httpserver.HTTPRequest;
import io.github.lottetreg.httpserver.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
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

  public Response call(String actionName)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

    Method action = getClass().getMethod(actionName);
    Object result = action.invoke(this);
    // if statusCode is empty, set to 200, use below, set code to 400 or 500 too??
    Response response;

    if (action.getReturnType() == String.class) {
      String body = (String) result;

      this.headers.put("Content-Type", "text/plain");

      response = new Response(200, body.getBytes(), this.headers);

    } else if (action.getReturnType() == Path.class) {
      Path filePath = (Path) result;
      byte[] fileContent = FileHelpers.readFile(filePath);

      this.headers.put("Content-Type", FileHelpers.getContentType(filePath));

      response = new Response(200, fileContent, this.headers);

    } else if (action.getReturnType() == void.class) {
      response = new Response(200, this.headers);

    } else {
      //    throw exception if return type not allowed?
      response = new Response(200);
    }

    return response;
  }
}
