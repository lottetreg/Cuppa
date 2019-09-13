package io.github.lottetreg.Cuppa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTTPServer {
  public static void main(String[] args) {
    try {
      Server.start(5000, getRoutes());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static List<Routable> getRoutes() {
    return new ArrayList<>(Arrays.asList(
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
  }
}
