package io.github.lottetreg.httpserver;

import java.net.Socket;

public class ServerSocket {
  private java.net.ServerSocket serverSocket;

  ServerSocket(java.net.ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public Connection acceptConnection() {
    try {
      Socket socket = this.serverSocket.accept();
      return new Connection(socket);
    } catch (Exception e) {
      throw new FailedToAcceptConnection(e);
    }
  }

  static class FailedToAcceptConnection extends RuntimeException {
    FailedToAcceptConnection(Throwable cause) {
      super("Failed to accept connection", cause);
    }
  }
}
