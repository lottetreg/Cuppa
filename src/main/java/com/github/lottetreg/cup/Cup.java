package com.github.lottetreg.cup;

import com.github.lottetreg.saucer.Saucer;

import java.io.IOException;
import java.util.*;

public class Cup {
  public void serve(int portNumber, List<Responsive> routes) throws IOException {
    new Saucer().start(portNumber, new Router(routes));
  }
}
