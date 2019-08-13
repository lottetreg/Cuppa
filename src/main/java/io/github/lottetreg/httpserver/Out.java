package io.github.lottetreg.httpserver;

public class Out implements Outable {
  public void println(String message) {
    System.out.println(message);
  }
}
