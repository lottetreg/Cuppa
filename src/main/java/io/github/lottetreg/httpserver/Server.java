package io.github.lottetreg.httpserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class Server {
  public void start(ServerSocket serverSocket) {
    Connection connection;
    List<Routable> routes = getRoutes();

    while ((connection = serverSocket.acceptConnection()) != null) {
      HTTPResponse response;
      try {
        HTTPRequest request = new Reader().read(connection);
        response = new Router(routes).route(request);
      } catch (Exception e) { // Throwable? http://wiki.c2.com/?DontCatchRuntimeExceptions
        e.printStackTrace();
        response = new HTTPResponse.Builder(500).build();
      }

      try {
        new Writer().write(connection, response.toBytes());
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        connection.close();
      }
    }
  }

  private List<Routable> getRoutes() {
    List<Routable> customRoutes = new ArrayList<>(Arrays.asList(
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
        new Route("/pickles", "GET", "ExampleController", "pickles"),
        new Redirect("/redirect", "GET", "/simple_get")
    ));

    customRoutes.addAll(defaultRoutes());
    customRoutes.addAll(resourcesForCurrentDirectory());

    return customRoutes;
  }

  private List<Routable> defaultRoutes() {
    return Arrays.asList(
        new Resource("/", "GET", "/index.html")
    );
  }

  private List<Routable> resourcesForCurrentDirectory() {
    List<Routable> resources = new ArrayList();

    BiPredicate<Path, BasicFileAttributes> isRegularFile =
        (path, attrs) -> attrs.isRegularFile();

    Path currentDir = Paths.get(".");
    try (Stream<Path> stream =
             Files.find(currentDir, Integer.MAX_VALUE, isRegularFile)) {

      stream.forEach(path -> {
        String resourcePath = "/" + Path.of(".").relativize(path).toString();
        Resource resource = new Resource(resourcePath, "GET", resourcePath);
        resources.add(resource);
      });

    } catch (IOException e) {
      e.printStackTrace();
    }

    return resources;
  }
}
