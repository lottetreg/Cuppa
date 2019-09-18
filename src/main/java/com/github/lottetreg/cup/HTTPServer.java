package com.github.lottetreg.cup;

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

public class HTTPServer {
  public static void main(String[] args) {
    try {
      new Cup().serve(5000, getAllRoutes(getRoutes()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static List<Responsive> getRoutes() {
    return new ArrayList<>(Arrays.asList(
        new Route("/simple_get", "GET", ExampleController.class, "empty"),
        new Route("/echo_body", "POST", ExampleController.class, "echo"),
        new Route("/method_options", "GET", ExampleController.class, "empty"),
        new Route("/method_options2", "GET", ExampleController.class, "empty"),
        new Route("/method_options2", "PUT", ExampleController.class, "empty"),
        new Route("/method_options2", "POST", ExampleController.class, "empty"),
        new Route("/pickles", "GET", ExampleController.class, "pickles"),
        new Route("/pickles_with_header", "GET", ExampleController.class, "picklesWithHeader"),
        new Redirect("/redirect", "GET", "/simple_get"),
        new Route("/get_with_body", "HEAD", ExampleController.class, ""),
        new Route("/time", "GET", ExampleController.class, "time")
    ));
  }

  private static List<Responsive> getAllRoutes(List<Responsive> routes) {
    routes.addAll(defaultRoutes());
    routes.addAll(resourcesForCurrentDirectory());

    return routes;
  }

  private static List<Responsive> defaultRoutes() {
    return Arrays.asList(
        new Resource("/", "GET", "/index.html")
    );
  }

  private static List<Responsive> resourcesForCurrentDirectory() {
    List<Responsive> resources = new ArrayList();

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
