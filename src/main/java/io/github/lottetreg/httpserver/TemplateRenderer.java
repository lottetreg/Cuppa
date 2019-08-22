package io.github.lottetreg.httpserver;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateRenderer {
  static String render(Map<String, String> context, String stringTemplate) {
    Pattern pattern = Pattern.compile("<%=\\s+(\\w+)\\s+%>");
    Matcher matcher = pattern.matcher(stringTemplate);

    return matcher.replaceAll(matchResult -> {
      return context.getOrDefault(matchResult.group(1), "");
    });
  }
}
