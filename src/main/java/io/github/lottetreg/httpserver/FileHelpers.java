package io.github.lottetreg.httpserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class FileHelpers {
  public static byte[] readFile(String filePath) {
    try {
      String currentDir = ".";
      String completeFilePath = currentDir + filePath;

      return Files.readAllBytes(Path.of(completeFilePath));

    } catch (NoSuchFileException e) {
      throw new MissingFile(filePath, e);

    } catch (IOException e) {
      throw new FailedToReadFromFile(filePath, e);
    }
  }

  public static String getContentType(String filePath) {
    try {
      Path path = new File(filePath).toPath();

      return Files.probeContentType(path);

    } catch (IOException e) {
      throw new FailedToGetContentType(filePath, e);
    }
  }

  public static class MissingFile extends RuntimeException {
    public MissingFile(String filePath, Throwable cause) {
      super(filePath, cause);
    }
  }

  public static class FailedToReadFromFile extends RuntimeException {
    public FailedToReadFromFile(String filePath, Throwable cause) {
      super(filePath, cause);
    }
  }

  public static class FailedToGetContentType extends RuntimeException {
    public FailedToGetContentType(String filePath, Throwable cause) {
      super(filePath, cause);
    }
  }
}
