package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.Socket;

import static junit.framework.TestCase.assertTrue;

public class ServerSocketTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void theServerSocketReceivesAccept() throws IOException {
    class MockServerSocket extends java.net.ServerSocket {
      private boolean receivedAccept;

      MockServerSocket() throws IOException {
      }

      public Socket accept() {
        this.receivedAccept = true;
        return new Socket();
      }
    }

    MockServerSocket mockServerSocket = new MockServerSocket();
    new ServerSocket(mockServerSocket).acceptConnection();

    assertTrue(mockServerSocket.receivedAccept);
  }
}
