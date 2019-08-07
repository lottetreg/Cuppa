package io.github.lottetreg.httpserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection implements Connectionable {
  private Socket socket;

  public Connection(Socket socket) {
    this.socket = socket;
  }

  public InputStream getInputStream() {
    try {
      return this.socket.getInputStream();
    } catch(Exception e) {
      throw new FailedToGetInputStreamException(e);
    }
  }

  public OutputStream getOutputStream() {
    try {
      return this.socket.getOutputStream();
    } catch(Exception e) {
      throw new FailedToGetOutputStreamException(e);
    }
  }

  public void close() {
    try {
      this.socket.close();
    } catch(Exception e) {
      throw new FailedToCloseConnection(e);
    }
  }

  static class FailedToGetInputStreamException extends RuntimeException {
    FailedToGetInputStreamException(Throwable cause) {
      super("Failed to get the input stream of the connection", cause);
    }
  }

  static class FailedToGetOutputStreamException extends RuntimeException {
    FailedToGetOutputStreamException(Throwable cause) {
      super("Failed to get the output stream of the connection", cause);
    }
  }

  static class FailedToCloseConnection extends RuntimeException {
    FailedToCloseConnection(Throwable cause) {
      super("Failed to close connection", cause);
    }
  }
}
