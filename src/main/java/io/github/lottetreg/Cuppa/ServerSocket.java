package io.github.lottetreg.Cuppa;

import java.io.IOException;
import java.net.Socket;

public class ServerSocket {
  private java.net.ServerSocket serverSocket;

  ServerSocket(java.net.ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public Connection acceptConnection() throws IOException {
    Socket socket = this.serverSocket.accept();
    return new Connection(socket);
  }
}
