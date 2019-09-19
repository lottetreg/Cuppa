package com.github.lottetreg.cup;

import com.github.lottetreg.saucer.HttpRequest;
import java.util.HashMap;

class HttpRequestHelpers {
  static HttpRequest buildHttpRequest(String method, String path) {
    return new HttpRequest(
        method,
        path,
        "HTTP/1.0",
        new HashMap<>(),
        ""
    );
  }
}
