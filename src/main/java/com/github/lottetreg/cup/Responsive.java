package com.github.lottetreg.cup;

public interface Responsive {
  String getPath();

  String getMethod();

  Response getResponse(Request request);
}
