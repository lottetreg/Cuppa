package io.github.lottetreg.httpserver;

public class Resource extends BaseRoute {
  String resourcePath;

  Resource(String path, String method, String resourcePath) {
    super(path, method);
    this.resourcePath = resourcePath;
  }

  public HTTPResponse getResponse(HTTPRequest request) {
    try {
      String contentType = FileHelpers.getContentType(getResourcePath());
      byte[] fileContents = FileHelpers.readFile(getResourcePath());

      return new HTTPResponse.Builder(200)
          .setBody(fileContents).build()
          .addHeader("Content-Type", contentType);

    } catch (FileHelpers.MissingFile e) {
      throw new MissingResource(getResourcePath(), e);

    } catch (FileHelpers.FailedToReadFromFile | FileHelpers.FailedToGetContentType e) {
      throw new FailedToGetResponse(getPath(), getMethod(), e);
    }
  }

  public String getResourcePath() {
    return this.resourcePath;
  }
}
