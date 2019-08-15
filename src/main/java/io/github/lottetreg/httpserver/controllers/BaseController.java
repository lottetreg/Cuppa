package io.github.lottetreg.httpserver.controllers;

import io.github.lottetreg.httpserver.FileHelpers;
import io.github.lottetreg.httpserver.HTTPRequest;
import io.github.lottetreg.httpserver.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    // if statusCode is empty, set to 200, use below
    Response response;

    if (action.getReturnType() == String.class) {
      String body = (String) result;
      response = new Response(200, body.getBytes(), this.headers);

    } else if (action.getReturnType() == void.class) {
      response = new Response(200, this.headers);

    } else if (action.getReturnType() == byte[].class) {
      response = new Response(200, (byte[]) result, this.headers);

    } else {
      //    throw exception if return type not allowed?
      response = new Response(200);
    }

    return response;
//    throw exception if return type not allowed?
  }

  protected byte[] readFile(String filePath) {
    return FileHelpers.readFile(filePath);
  }

  protected String getContentType(String filePath) {
    return FileHelpers.getContentType(filePath);
  }
}
