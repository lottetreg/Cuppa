package io.github.lottetreg.httpserver;

public class HTTPInitialLine {
  public String method;
  public String URI;
  public String HTTPVersion;
  private String whiteSpace = "\\s";

  HTTPInitialLine(String initialLine) {
      String[] splitInitialLine = initialLine.split(whiteSpace);

      if(splitInitialLine.length != 3) {
        throw new IncorrectlyFormattedInitialLineException(initialLine);
      }

      this.method = getMethod(splitInitialLine);
      this.URI = getURI(splitInitialLine);
      this.HTTPVersion = getHTTPVersion(splitInitialLine);
  }

  private String getMethod(String[] splitInitialLine) {
    return splitInitialLine[0];
  }

  private String getURI(String[] splitInitialLine) {
    return splitInitialLine[1];
  }

  private String getHTTPVersion(String[] splitInitialLine) {
    return splitInitialLine[2];
  }

  static class IncorrectlyFormattedInitialLineException extends RuntimeException {
    IncorrectlyFormattedInitialLineException(String initialLine) {
      super("Initial line is incorrectly formatted: " + initialLine);
    }
  }
}
