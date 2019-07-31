package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.Socket;

public class ServerSocketTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itThrowsAnExceptionIfItFailsToAcceptTheConnection() throws IOException {
    MockServerSocket serverSocket = new MockServerSocket();

    exceptionRule.expect(ServerSocket.FailedToAcceptConnection.class);
    exceptionRule.expectMessage("Failed to accept connection");

    new ServerSocket(serverSocket).acceptConnection();
  }

  private class MockServerSocket extends java.net.ServerSocket {
    MockServerSocket() throws IOException {
    }

    public Socket accept() throws IOException {
      throw new IOException(new Throwable());
    }
  }
}
