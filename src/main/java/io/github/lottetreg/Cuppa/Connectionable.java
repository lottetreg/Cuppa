package io.github.lottetreg.Cuppa;

import java.io.InputStream;
import java.io.OutputStream;

public interface Connectionable {
  InputStream getInputStream();

  OutputStream getOutputStream();
}
