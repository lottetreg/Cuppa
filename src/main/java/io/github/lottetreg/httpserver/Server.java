package io.github.lottetreg.httpserver;

public class Server {
  public void start(ServerSocket serverSocket) {
    Connection connection = serverSocket.acceptConnection();

    new Reader().read(connection);
    new Writer().write(connection, "HTTP/1.0 200 OK");
  }
}
