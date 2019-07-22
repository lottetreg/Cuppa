package io.github.lottetreg.httpserver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PositionableInputStream extends BufferedInputStream {
  private int newLine = 10;
  private int carriageReturn = 13;

  PositionableInputStream(InputStream inputStream) {
    super(inputStream);
    fillBufferAndResetPosition();
  }

  private void fillBufferAndResetPosition() {
    try {
      read();
      setPosition(0);
    } catch (IOException e) {
      throw new CreationFailedException(e);
    }
  }

  public int getPosition() {
    return this.pos;
  }

  public void setPosition(int position) {
    this.pos = position;
  }

  public String readLine() throws IOException {
    List<Character> line = new ArrayList();

    int currentCharacter;
    int lastCharacter = 0;
    while(!endOfLine(currentCharacter = read(), lastCharacter)) {
      if(currentCharacter != newLine && currentCharacter != carriageReturn) {
        line.add((char)currentCharacter);
      }

      lastCharacter = currentCharacter;
    }

    return line.stream()
            .map(String::valueOf)
            .collect(Collectors.joining());
  }

  private Boolean endOfLine(int currentCharacter, int lastCharacter) {
    return (isCRLF(currentCharacter, lastCharacter) || endOfStream(currentCharacter));
  }

  private Boolean isCRLF(int currentCharacter, int lastCharacter) {
    return (lastCharacter == carriageReturn && currentCharacter == newLine);
  }

  private Boolean endOfStream(int character) {
    return character == -1;
  }

  static class CreationFailedException extends RuntimeException {
    CreationFailedException(Throwable cause) {
      super("Failed to create new PositionableInputStream", cause);
    }
  }
}
