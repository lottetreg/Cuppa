package io.github.lottetreg.Cuppa;

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
      ParsedRequest parsedRequest = new ParsedRequest(connection.getInputStream(), new Out());
      HTTPRequest request = new HTTPRequest(parsedRequest, new Out());

      try {
        response = router.route(request);

      } catch (Router.NoMatchingPath | Routable.MissingResource e) {
        e.printStackTrace();
        response = new Response(404);

      } catch (Router.NoMatchingMethodForPath e) {
        e.printStackTrace();
        response = new Response(405, Map.of(
            "Allow", router.getAllowedMethods(request.getPath())));
      }

    } catch (ParsedRequest.BadRequest e) {
      e.printStackTrace();
      response = new Response(400);

    } catch (Throwable e) {
      e.printStackTrace();

    } finally {
      HTTPResponse httpResponse = createHTTPResponse(response);
      writeToConnection(httpResponse.toBytes());
    }
  }

  private HTTPResponse createHTTPResponse(Response response) {
    return new HTTPResponse.Builder(response.getStatusCode())
        .setHeaders(response.getHeaders())
        .setBody(response.getBody())
        .build();
  }

  private void writeToConnection(byte[] response) {
    try {
      new Writer().write(this.connection, response);
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      this.connection.close();
    }
  }
}
