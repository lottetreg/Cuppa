package io.github.lottetreg.httpserver;

import java.util.Arrays;
import java.util.List;

public class Server {
  public void start(ServerSocket serverSocket) {
    Connection connection;
    while ((connection = serverSocket.acceptConnection()) != null) {
      HTTPRequest request = new Reader().read(connection);
      HTTPResponse response = new Router(getRoutes()).route(request);
      new Writer().write(connection, response.toString());
      connection.close();
    }
  }

  private List<Routable> getRoutes() {
    return Arrays.asList(
        new Route("/simple_get", "GET", "ExampleController", "empty"),
        new Route("/simple_get", "HEAD", "ExampleController", "empty"),
        new Route("/get_with_body", "HEAD", "ExampleController", "empty"),
        new Route("/get_with_body", "OPTIONS", "ExampleController", "empty"),
        new Route("/echo_body", "POST", "ExampleController", "echo"),
        new Route("/method_options", "GET", "ExampleController", "empty"),
        new Route("/method_options", "HEAD", "ExampleController", "empty"),
        new Route("/method_options", "OPTIONS", "ExampleController", "empty"),
        new Route("/method_options2", "GET", "ExampleController", "empty"),
        new Route("/method_options2", "HEAD", "ExampleController", "empty"),
        new Route("/method_options2", "OPTIONS", "ExampleController", "empty"),
        new Route("/method_options2", "PUT", "ExampleController", "empty"),
        new Route("/method_options2", "POST", "ExampleController", "empty"),
        new Redirect("/redirect", "GET", "/simple_get")
    );
  }
}
