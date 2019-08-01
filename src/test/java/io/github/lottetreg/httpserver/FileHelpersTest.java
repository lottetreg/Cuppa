package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class FileHelpersTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  private String pathFor(String fileName) {
    String supportDirectoryPath = "/src/test/java/io/github/lottetreg/httpserver/support";
    return supportDirectoryPath + fileName;
  }

  @Test
  public void readFileReadsAFileInTheCurrentDirectoryIntoAByteArray() {
    String filePath = pathFor("/index.html");

    byte[] fileContents = FileHelpers.readFile(filePath);

    assertEquals("<h1>Hello, World!</h1>\n", new String(fileContents));
  }

  @Test
  public void readFileThrowsAnExceptionIfTheFileIsMissing() {
    String filePath = pathFor("/missing.html");

    exceptionRule.expect(FileHelpers.MissingFile.class);
    exceptionRule.expectMessage("/missing.html");

    FileHelpers.readFile(filePath);
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAPlainTextLFile() {
    assertEquals("text/plain", FileHelpers.getContentType("/index.txt"));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAnHTMLFile() {
    assertEquals("text/html", FileHelpers.getContentType("/index.html"));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAJPEGFile() {
    assertEquals("image/jpeg", FileHelpers.getContentType("/pickles.jpg"));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAPNGFile() {
    assertEquals("image/png", FileHelpers.getContentType("/pickles.png"));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAGIFFile() {
    assertEquals("image/gif", FileHelpers.getContentType("/pickles.gif"));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAnMP3File() {
    assertEquals("audio/mpeg", FileHelpers.getContentType("/pickles.mp3"));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAnMP4File() {
    assertEquals("video/mp4", FileHelpers.getContentType("/pickles.mp4"));
  }
}
