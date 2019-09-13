package io.github.lottetreg.Cuppa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class StreamReader {
  private BufferedReader reader;

  StreamReader(InputStream inputStream) {
    this.reader = new BufferedReader(new InputStreamReader(inputStream));
  }

  public String readLine() throws IOException {
    return this.reader.readLine();
  }

  public List<String> readLinesUntilEmptyLine() throws IOException {
    List<String> lines = new ArrayList();
    String line;
    while ((line = readLine()) != null && !line.isEmpty()) {
      lines.add(line);
    }

    return lines;
  }

  public String readNChars(int length) throws IOException {
    char[] buffer = new char[length];
    this.reader.read(buffer, 0, length);

    return new String(buffer);
  }
}
