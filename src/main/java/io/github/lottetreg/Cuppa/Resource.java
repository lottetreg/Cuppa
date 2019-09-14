package io.github.lottetreg.Cuppa;

import java.nio.file.Path;
import java.util.Map;

import static io.github.lottetreg.Cuppa.FileHelpers.getContentType;
import static io.github.lottetreg.Cuppa.FileHelpers.readFile;

public class Resource extends BaseRoute {
  String resourcePath;

  public Resource(String path, String method, String resourcePath) {
    super(path, method);
    this.resourcePath = resourcePath;
  }

  public Response getResponse(HTTPRequest request) {
    try {
      String contentType = getContentType(Path.of(getResourcePath()));
      byte[] fileContents = readFile(Path.of(getResourcePath()));

      return new Response(200, fileContents, Map.of("Content-Type", contentType));

    } catch (FileHelpers.MissingFile e) {
      throw new MissingResource(getResourcePath(), e);
    }
  }

  public String getResourcePath() {
    return this.resourcePath;
  }
}
