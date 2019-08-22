package io.github.lottetreg.httpserver;

import java.util.List;
import java.util.Map;

public class RunnableServer implements Runnable {
  private Connection connection;
  private List<Routable> routes;

  RunnableServer(Connection connection, List<Routable> routes) {
    this.connection = connection;
    this.routes = routes;
  }

  @Override
  public void run() {
    Response response = new Response(500);

    try {
      Router router = new Router(this.routes);
      HTTPRequest request = new Reader().read(this.connection);

      try {
        response = router.route(request);

      } catch (Router.NoMatchingPath | Routable.MissingResource e) {
        e.printStackTrace();
        response = new Response(404);

      } catch (Router.NoMatchingMethodForPath e) {
        e.printStackTrace();
        response = new Response(405, Map.of(
            "Allow", router.getAllowedMethods(request.getURI())));
      }

    } catch (Throwable e) {
      e.printStackTrace();

    } finally {
      HTTPResponse httpResponse = createHTTPResponse(response);
      writeToConnection(httpResponse.toBytes());
    }
  }

  private HTTPResponse createHTTPResponse(Response response) {
    return new HTTPResponse.Builder(response.getStatusCode())
        .setBody(response.getBody())
        .setHeaders(new HTTPHeaders(response.getHeaders()))
        .build();
  }

  private void writeToConnection(byte[] response) {
    try {
      new Writer().write(this.connection, response);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      this.connection.close();
    }
  }
}
