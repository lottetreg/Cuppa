package io.github.lottetreg.Cuppa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTTPServer {
  public static void main(String[] args) {
    try {
      // TODO: if no port number, catch error and display message
      Server.start(Integer.parseInt(args[0]), getRoutes());
    } catch (NumberFormatException e) {
      System.out.println("Invalid port number: " + args[0]);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static List<Routable> getRoutes() { // controllerNames could be instances of controllers instead?
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
        new Route("/get_with_body", "HEAD", "", ""), // need this to pass acceptance tests >:(
        new Route("/time", "GET", ExampleController.class, "time")
    ));
  }
}
