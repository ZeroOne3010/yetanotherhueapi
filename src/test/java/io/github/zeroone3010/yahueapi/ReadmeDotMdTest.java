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
import java.util.ArrayList;
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
  private static final String CODE_IMPORTS = "imports";
  private static final String HEADER_IMPORT = "import ";
  private static final String CODE_BLOCK_STARTS = "```java";
  private static final String CODE_BLOCK_ENDS = "```";

  private enum BlockType {CODE, IMPORTS, NONE}

  @Test
  void verifyExamplesOnReadmeDotMdCanBeCompiled() throws IOException {
    final Logger logger = Logger.getAnonymousLogger();

    final URL readmeDotMdLocation = ClassLoader.getSystemResource("README.md");
    Assumptions.assumeTrue(readmeDotMdLocation != null, "README.md not found.");

    logger.info("Parsing code blocks from README.md");
    final Code code = readReadmeDotMdCodeBlocks(readmeDotMdLocation);

    logger.info("Found " + code.getCodeBlocks().size() + " blocks, starting to compile.");
    assertAllCodeBlocksCanBeCompiled(code);

    logger.info("Successfully compiled all code blocks.");
  }

  private Code readReadmeDotMdCodeBlocks(final URL readmeDotMdLocation) throws IOException {
    final StringBuilder sb = new StringBuilder();
    BlockType blockType = BlockType.NONE;
    String codeHeader = null;
    int codeBlockNumber = 0;
    String checkedExceptions = null;

    final List<String> lines = new BufferedReader(new InputStreamReader(readmeDotMdLocation.openStream()))
        .lines().collect(Collectors.toList());
    final Map<String, String> codeBlocks = new LinkedHashMap<>();
    final List<String> importStatements = new ArrayList<>();
    for (final String line : lines) {
      if (line.startsWith(COMMENT_LINE_START)) {
        final String header = line.replace(COMMENT_LINE_START, "").replace(COMMENT_LINE_END, "");
        if (header.startsWith(HEADER_IMPORT)) {
          importStatements.add(header);
        } else {
          codeHeader = header;
        }
      } else if (line.equals(CODE_BLOCK_STARTS)) {
        if (CODE_IMPORTS.equals(codeHeader)) {
          blockType = BlockType.IMPORTS;
        } else {
          blockType = BlockType.CODE;
        }
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
        blockType = BlockType.NONE;
        codeHeader = null;
        checkedExceptions = null;
        sb.setLength(0);
      } else if (blockType == BlockType.CODE) {
        sb.append(line).append("\n");
      } else if (blockType == BlockType.IMPORTS) {
        importStatements.add(line);
      }
    }
    return new Code(importStatements, codeBlocks);
  }

  private void assertAllCodeBlocksCanBeCompiled(final Code code) throws IOException {
    for (final Entry<String, String> entry : code.getCodeBlocks().entrySet()) {
      final String header = entry.getKey();
      final String block = entry.getValue();

      final File javaFile = File.createTempFile("ReadmeDotMdCodeBlock", ".java");
      final FileWriter writer = new FileWriter(javaFile);
      for (final String importStatement : code.getImportStatements()) {
        writer.write(importStatement);
      }
      writer.write("");
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


  private static class Code {
    private final List<String> importStatements;
    private final Map<String, String> codeBlocks;

    public Code(final List<String> importStatements, final Map<String, String> codeBlocks) {
      this.importStatements = importStatements;
      this.codeBlocks = codeBlocks;
    }

    public List<String> getImportStatements() {
      return importStatements;
    }

    public Map<String, String> getCodeBlocks() {
      return codeBlocks;
    }
  }
}
