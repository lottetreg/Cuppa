package io.github.lottetreg.httpserver;

import java.io.IOException;
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
    } catch(IOException e) {
      throw new FailedToGetOutputStreamException(e);
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
}
