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
      Response response = new Response(500);

      try {
        Router router = new Router(routes);
        HTTPRequest request = new Reader().read(connection);

        try {
          response = router.route(request);

        } catch (Router.NoMatchingPath | Routable.MissingResource e) {
          e.printStackTrace();
          response = new Response(404);

        } catch (Router.NoMatchingMethodForPath e) {
          e.printStackTrace();
          response = new Response(405, Map.of(
              "Allow", Routable.getAllowedMethods(request.getURI())));
        }

      } catch (Throwable e) {
        e.printStackTrace();

      } finally {
        HTTPResponse httpResponse = createHTTPResponse(response);
        writeToConnection(connection, httpResponse.toBytes());
      }
    }
  }

  private HTTPResponse createHTTPResponse(Response response) {
    return new HTTPResponse.Builder(response.getStatusCode())
        .setBody(response.getBody())
        .setHeaders(new HTTPHeaders(response.getHeaders()))
        .build();
  }

  private void writeToConnection(Connection connection, byte[] response) {
    try {
      new Writer().write(connection, response);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      connection.close();
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
        new HeadRoute("/get_with_body") // have to keep this to pass acceptance tests
    ));

    routes.addAll(defaultRoutes());
    routes.addAll(resourcesForCurrentDirectory());
    routes.forEach(Routable::store);
    routes.addAll(createDefaultRoutesForEachPath(Routable.getAllPaths()));

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

  private List<Routable> createDefaultRoutesForEachPath(List<String> paths) {
    List<Routable> routes = new ArrayList();

    paths.stream().forEach(path -> {
      routes.add(new OptionsRoute(path));
      routes.add(new HeadRoute(path));
    });

    return routes;
  }
}
