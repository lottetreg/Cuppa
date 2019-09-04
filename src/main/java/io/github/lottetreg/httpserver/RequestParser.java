package io.github.lottetreg.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
  public String method;
  public String path;
  public String version;
  public HashMap<String, String> headers;

  RequestParser(InputStream request) throws IOException {
    StreamReader streamReader = new StreamReader(request);
    String initialLine = streamReader.readLine();
    Matcher parsedInitialLine = parseInitialLine(initialLine);

    try {
      this.method = parsedInitialLine.group("method");
      this.path = parsedInitialLine.group("path");
      this.version = parsedInitialLine.group("version");
    } catch (IllegalStateException e) {
      throw new BadRequest("Incorrectly formatted initial line: " + initialLine, e);
    }

    this.headers = parseHeaders(streamReader.readLinesUntilEmptyLine());
  }

  private Matcher parseInitialLine(String initialLine) {
    Pattern pattern = Pattern.compile("^(?<method>\\S+)\\s+(?<path>\\S+)\\s+(?<version>\\S+)$");
    Matcher matcher = pattern.matcher(initialLine);
    matcher.find();

    return matcher;
  }

  private HashMap<String, String> parseHeaders(List<String> headers) {
    HashMap<String, String> validHeaders = new HashMap<>();

    for (String header : headers) {
      Pattern pattern = Pattern.compile("^(?<key>\\S+):\\s*(?<value>\\S+)");
      Matcher matcher = pattern.matcher(header);
      matcher.find();

      try {
        validHeaders.put(matcher.group("key"), matcher.group("value"));
      } catch (IllegalStateException e) {
      }
    }

    return validHeaders;
  }

  public static class BadRequest extends RuntimeException {
    BadRequest(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
