package com.github.lottetreg.cup;

import java.util.HashMap;

public interface Respondable {
  int getStatusCode();

  HashMap<String, String> getHeaders();

  byte[] getBody();
}
