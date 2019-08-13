package io.github.lottetreg.httpserver;

public class Redirect extends BaseRoute {
  private String redirectPath;

  Redirect(String path, String method, String redirectPath) {
    super(path, method);
    this.redirectPath = redirectPath;
  }

  public HTTPResponse getResponse(HTTPRequest request) {
    String URI = "http://" + request.getHeader("Host") + getRedirectPath();

    return new HTTPResponse.Builder(301).build()
        .addHeader("Location", URI);
  }

  public String getRedirectPath() {
    return this.redirectPath;
  }
}
