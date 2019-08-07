package io.github.lottetreg.httpserver;

public class Server {
  public void start(ServerSocket serverSocket) {
    Connection connection;
    while((connection = serverSocket.acceptConnection()) != null) {
      HTTPRequest request = new Reader().read(connection);
      new Writer().write(connection, "HTTP/1.0 200 OK\r\n\r\n" + request.body);
      connection.close();
    }
  }
}
