package com.github.lottetreg.cup;

import com.github.lottetreg.saucer.HttpRequest;
import com.github.lottetreg.saucer.HttpResponse;
import com.github.lottetreg.saucer.Respondable;
import com.github.lottetreg.saucer.Saucer;

import java.io.IOException;

public class Cup implements Respondable {
  private Callable app;

  public Cup(Callable app) {
    this.app = app;
  }

  public void serve(int portNumber) throws IOException {
    new Saucer().start(portNumber, this);
  }

  public HttpResponse respond(HttpRequest httpRequest) {
    Request request = new Request(
        httpRequest.getMethod(),
        httpRequest.getPath(),
        httpRequest.getHeaders(),
        httpRequest.getBody()
    );

    Response response = this.app.call(request);

    return new HttpResponse.Builder(response.getStatusCode())
        .setHeaders(response.getHeaders())
        .setBody(response.getBody())
        .build();
  }
}
