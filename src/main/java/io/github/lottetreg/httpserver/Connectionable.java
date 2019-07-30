package io.github.lottetreg.httpserver;

import java.io.InputStream;
import java.io.OutputStream;

public interface Connectionable {
  InputStream getInputStream();
  OutputStream getOutputStream();
}
