package io.github.lottetreg.httpserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PositionableStreamReader {
  private PositionableInputStream inputStream;

  PositionableStreamReader(PositionableInputStream inputStream) {
    this.inputStream = inputStream;
  }

  public StringData readFirstLine() throws IOException {
    this.inputStream.setPosition(0);

    return new StringData(this.inputStream.readLine(), this.inputStream.getPosition());
  }

  public ListData readToEmptyLine(int startingPos) throws IOException {
    this.inputStream.setPosition(startingPos);

    List<String> headerLines = new ArrayList();
    String line;
    while ((line = this.inputStream.readLine()) != null && !line.isEmpty()) {
      headerLines.add(line);
    }

    return new ListData(headerLines, this.inputStream.getPosition());
  }

  public StringData readNBytes(int startingPos, int contentLength) throws IOException {
    this.inputStream.setPosition(startingPos);

    byte[] buffer = new byte[contentLength];
    this.inputStream.read(buffer, 0, contentLength);

    return new StringData(new String(buffer), this.inputStream.getPosition());
  }

  public class StringData {
    public String value;
    public int endPosition;

    StringData(String value, int endPosition) {
      this.value = value;
      this.endPosition = endPosition;
    }
  }

  public class ListData {
    public List<String> value;
    public int endPosition;

    ListData(List<String> value, int endPosition) {
      this.value = value;
      this.endPosition = endPosition;
    }
  }
}

