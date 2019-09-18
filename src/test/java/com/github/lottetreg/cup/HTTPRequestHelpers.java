//package io.github.lottetreg.cup;
//
//import RequestStringBuilder;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//
//public class HTTPRequestHelpers {
//  static HTTPRequest buildHTTPRequest(String method, String path) throws IOException {
//    String request = new RequestStringBuilder()
//        .setMethod(method)
//        .setPath(path)
//        .build();
//
//    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());
//    ParsedRequest parsedRequest = new ParsedRequest(inputStream);
//
//    return new HTTPRequest(parsedRequest);
//  }
//}
