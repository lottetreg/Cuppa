package io.github.lottetreg.Cuppa;

public class HTTPServer {
  public static void main(String[] args) {
    try {
      new Server().start(new ServerSocket(new java.net.ServerSocket(5000)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
