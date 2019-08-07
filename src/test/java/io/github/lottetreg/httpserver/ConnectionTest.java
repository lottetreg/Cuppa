package io.github.lottetreg.httpserver;

import org.junit.Test;
import org.junit.Rule;

import static org.junit.Assert.assertEquals;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.net.Socket;

public class ConnectionTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void getInputStreamReturnsTheSocketsInputStream() throws IOException {
    class MockSocket extends Socket {
      private ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {});

      @Override
      public InputStream getInputStream() {
        return this.inputStream;
      }
    }

    Socket socket = new MockSocket();
    Connection connection = new Connection(socket);

    assertEquals(socket.getInputStream(), connection.getInputStream());
  }

  @Test
  public void getOutputStreamReturnsTheSocketsOutputStream() throws IOException {
    class MockSocket extends Socket {
      private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();;

      @Override
      public OutputStream getOutputStream() {
        return this.outputStream;
      }
    }

    Socket socket = new MockSocket();
    Connection connection = new Connection(socket);

    assertEquals(socket.getOutputStream(), connection.getOutputStream());
  }

  @Test
  public void getInputStreamThrowsAnException() {
    class MockSocket extends Socket {
      @Override
      public InputStream getInputStream() throws IOException {
        throw new IOException();
      }
    }

    Connection connection = new Connection(new MockSocket());

    exceptionRule.expect(Connection.FailedToGetInputStreamException.class);
    exceptionRule.expectMessage("Failed to get the input stream of the connection");

    connection.getInputStream();
  }

  @Test
  public void getOutputStreamThrowsAnException() {
    class MockSocket extends Socket {
      @Override
      public OutputStream getOutputStream() throws IOException {
        throw new IOException();
      }
    }

    Connection connection = new Connection(new MockSocket());

    exceptionRule.expect(Connection.FailedToGetOutputStreamException.class);
    exceptionRule.expectMessage("Failed to get the output stream of the connection");

    connection.getOutputStream();
  }

  @Test
  public void itClosesTheSocket() {
    Socket socket = new Socket();
    Connection connection = new Connection(socket);

    connection.close();

    assertEquals(true, socket.isClosed());
  }

  @Test
  public void closeThrowsAnExceptionIfItFailsToCloseTheSocket() {
    class MockSocket extends Socket {
      @Override
      public void close() throws IOException {
        throw new IOException();
      }
    }

    Connection connection = new Connection(new MockSocket());

    exceptionRule.expect(Connection.FailedToCloseConnection.class);
    exceptionRule.expectMessage("Failed to close connection");

    connection.close();
  }
}
