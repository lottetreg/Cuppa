package io.github.lottetreg.Cuppa;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class Server {
  public static void start(int portNumber, List<Routable> customRoutes) throws IOException {
    ServerSocket serverSocket = new ServerSocket(new java.net.ServerSocket(portNumber));
    List<Routable> routes = getAllRoutes(customRoutes);
    Connection connection;

    while ((connection = serverSocket.acceptConnection()) != null) {
      Thread thread = new Thread(new RunnableServer(connection, routes));
      thread.start();
    }
  }

  private static List<Routable> getAllRoutes(List<Routable> routes) {
    routes.addAll(defaultRoutes());
    routes.addAll(resourcesForCurrentDirectory());

    return routes;
  }

  private static List<Routable> defaultRoutes() {
    return Arrays.asList(
        new Resource("/", "GET", "/index.html")
    );
  }

  private static List<Routable> resourcesForCurrentDirectory() {
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
