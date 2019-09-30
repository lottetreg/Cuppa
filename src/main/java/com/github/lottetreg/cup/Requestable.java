package com.github.lottetreg.cup;

import java.util.HashMap;

public interface Requestable {
  String getMethod();

  String getPath();

  HashMap<String, String> getHeaders();

  String getBody();
}
