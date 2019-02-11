package io.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadmeDotMdTest {
  private static final String COMMENT_LINE_START = "[//]: # (";
  private static final String COMMENT_LINE_END = ")";
  private static final String CODE_REQUIRES = "requires-";
  private static final String CODE_THROWS = "throws-";
  private static final String CODE_BLOCK_STARTS = "```java";
  private static final String CODE_BLOCK_ENDS = "```";

  @Test
  void verifyExamplesOnReadmeDotMdCanBeCompiled() throws IOException {
    final Logger logger = Logger.getAnonymousLogger();

    final URL readmeDotMdLocation = ClassLoader.getSystemResource("README.md");
    Assumptions.assumeTrue(readmeDotMdLocation != null, "README.md not found.");

    logger.info("Parsing code blocks from README.md");
    final Map<String, String> codeBlocks = readReadmeDotMdCodeBlocks(readmeDotMdLocation);

    logger.info("Found " + codeBlocks.size() + " blocks, starting to compile.");
    assertAllCodeBlocksCanBeCompiled(codeBlocks);

    logger.info("Successfully compiled all code blocks.");
  }

  private Map<String, String> readReadmeDotMdCodeBlocks(final URL readmeDotMdLocation) throws IOException {
    final StringBuilder sb = new StringBuilder();
    boolean codeBlock = false;
    String codeHeader = null;
    int codeBlockNumber = 0;
    String checkedExceptions = null;

    final List<String> lines = new BufferedReader(new InputStreamReader(readmeDotMdLocation.openStream()))
        .lines().collect(Collectors.toList());
    final Map<String, String> codeBlocks = new LinkedHashMap<>();
    for (final String line : lines) {
      if (line.startsWith(COMMENT_LINE_START)) {
        codeHeader = line.replace(COMMENT_LINE_START, "").replace(COMMENT_LINE_END, "");
      } else if (line.equals(CODE_BLOCK_STARTS)) {
        codeBlock = true;
        if (codeHeader != null && codeHeader.startsWith(CODE_REQUIRES)) {
          final String requiredBlock = codeHeader.replace(CODE_REQUIRES, "");
          sb.append(codeBlocks.get(requiredBlock));
          codeHeader = null;
        }
        if (codeHeader != null && codeHeader.startsWith(CODE_THROWS)) {
          sb.append("try {");
          checkedExceptions = codeHeader.replace(CODE_THROWS, "");
          codeHeader = null;
        }
        if (codeHeader == null) {
          codeHeader = String.valueOf(codeBlockNumber);
          codeBlockNumber++;
        }
      } else if (line.equals(CODE_BLOCK_ENDS) && codeHeader != null) {
        if (checkedExceptions != null) {
          sb.append("} catch(").append(checkedExceptions).append(" e) {/* Ignoring on purpose */}");
        }
        codeBlocks.put(codeHeader, sb.toString());
        codeBlock = false;
        codeHeader = null;
        checkedExceptions = null;
        sb.setLength(0);
      } else if (codeBlock) {
        sb.append(line).append("\n");
      }
    }
    return codeBlocks;
  }

  private void assertAllCodeBlocksCanBeCompiled(final Map<String, String> codeBlocks) throws IOException {
    for (final Entry<String, String> entry : codeBlocks.entrySet()) {
      final String header = entry.getKey();
      final String block = entry.getValue();

      final File javaFile = File.createTempFile("ReadmeDotMdCodeBlock", ".java");
      final FileWriter writer = new FileWriter(javaFile);
      writer.write("import io.github.zeroone3010.yahueapi.*;\n");
      writer.write("public class " + javaFile.getName().replace(".java", "") + " {\n");
      writer.write("  public static void main(String[] args) {\n");
      writer.write(block);
      writer.write("  }\n");
      writer.write("}\n");
      writer.flush();
      writer.close();

      final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
      int result = javaCompiler.run(null, null, null, javaFile.getAbsolutePath());
      assertEquals(0, result, "Code block '" + header + "' fails to compile: \n" + block);
    }
  }
}
