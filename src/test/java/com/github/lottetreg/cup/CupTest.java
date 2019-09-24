package com.github.lottetreg.cup;

import com.github.lottetreg.saucer.HttpRequest;
import com.github.lottetreg.saucer.HttpResponse;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class CupTest {
  @Ignore
  class MockApp implements Callable {
    public Response call(Request request) {
      return new Response(
          200,
          Map.of("Header-1", "something"),
          "hello".getBytes()
      );
    }
  }

  private HttpRequest defaultRequest() {
    return new HttpRequest(
        "GET",
        "/",
        "HTTP/1.0",
        new HashMap<>(),
        ""
    );
  }

  @Test
  public void itGetsAResponseFromTheApp() {
    Cup cup = new Cup(new MockApp());

    HttpResponse httpResponse = cup.respond(defaultRequest());

    assertEquals(200, httpResponse.getStatusCode());
    assertEquals("something", httpResponse.getHeaders().get("Header-1"));
    assertEquals("hello", new String(httpResponse.getBody()));
  }
}
