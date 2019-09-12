package io.github.lottetreg.httpserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class Server {
  public void start(ServerSocket serverSocket) {
    Connection connection;
    List<Routable> routes = getRoutes();

    while ((connection = serverSocket.acceptConnection()) != null) {
      Thread thread = new Thread(new RunnableServer(connection, routes));
      thread.start();
    }
  }

  private List<Routable> getRoutes() {
    List<Routable> routes = new ArrayList<>(Arrays.asList(
        new Route("/simple_get", "GET", "ExampleController", "empty"),
        new Route("/echo_body", "POST", "ExampleController", "echo"),
        new Route("/method_options", "GET", "ExampleController", "empty"),
        new Route("/method_options2", "GET", "ExampleController", "empty"),
        new Route("/method_options2", "PUT", "ExampleController", "empty"),
        new Route("/method_options2", "POST", "ExampleController", "empty"),
        new Route("/pickles", "GET", "ExampleController", "pickles"),
        new Route("/pickles_with_header", "GET", "ExampleController", "picklesWithHeader"),
        new Redirect("/redirect", "GET", "/simple_get"),
        new Route("/get_with_body", "HEAD", "", ""), // need this to pass acceptance tests >:(
        new Route("/time", "GET", "ExampleController", "time")
    ));

    routes.addAll(defaultRoutes());
    routes.addAll(resourcesForCurrentDirectory());

    return routes;
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
