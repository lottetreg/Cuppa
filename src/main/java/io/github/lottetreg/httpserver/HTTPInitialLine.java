package io.github.lottetreg.httpserver;

public class HTTPInitialLine {
  public String method;
  public String URI;
  public String HTTPVersion;
  private String whiteSpace = "\\s";

  HTTPInitialLine(String initialLine) {
    String[] splitInitialLine = initialLine.split(whiteSpace);

    if (splitInitialLine.length != 3) {
      throw new IncorrectlyFormattedInitialLine(initialLine);
    }

    this.method = splitInitialLine[0];
    this.URI = splitInitialLine[1];
    this.HTTPVersion = splitInitialLine[2];
  }

  public String getMethod() {
    return this.method;
  }

  public String getURI() {
    return this.URI;
  }

  public String getHTTPVersion() {
    return this.HTTPVersion;
  }

  static class IncorrectlyFormattedInitialLine extends RuntimeException {
    IncorrectlyFormattedInitialLine(String initialLine) {
      super("Initial line is incorrectly formatted: " + initialLine);
    }
  }
}
