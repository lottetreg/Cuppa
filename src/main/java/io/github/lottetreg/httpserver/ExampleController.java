package io.github.lottetreg.httpserver;

import java.nio.file.Path;

public class ExampleController extends BaseController {
  public ExampleController(HTTPRequest request) {
    super(request);
  }

  public void empty() {}

  public String echo() {
    return this.request.getBody();
  }

  public Path pickles() {
    return Path.of("/pickles.jpg");
  }

  public Path picklesWithHeader() {
    this.headers.put("Some-Header", "HI"); // addHeader()
    return Path.of("/pickles.jpg");
  }
}
