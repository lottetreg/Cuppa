package io.github.lottetreg.httpserver;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TemplateTest {
  @Test
  public void itRendersItself() {
    Template template = new Template("/src/test/java/io/github/lottetreg/httpserver/support/embedded_data.html");
    Map<String, String> context = Map.of("name", "Pickles");

    assertEquals("<h1>Hello, Pickles!</h1>\n", new String(template.render(context)));
  }
}
