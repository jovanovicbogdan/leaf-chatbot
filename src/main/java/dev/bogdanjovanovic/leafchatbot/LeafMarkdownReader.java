package dev.bogdanjovanovic.leafchatbot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class LeafMarkdownReader {

  private final List<FileSystemResource> resources;

  public LeafMarkdownReader() {
    final var leafDocs = System.getenv("LEAF_MARKDOWN_DIR");
    if (leafDocs == null || leafDocs.isEmpty()) {
      throw new RuntimeException("Please set LEAF_MARKDOWN_DIR environment variable.");
    }
    try (Stream<Path> paths = Files.walk(Paths.get(leafDocs))) {
      this.resources = paths
          .filter(Files::isRegularFile)
          .map(p -> new FileSystemResource(p.toFile()))
          .toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<LoadedMarkdownFile> loadMarkdown() {
    return resources.stream()
        .map(r -> {
          final var config = MarkdownDocumentReaderConfig.builder()
              .withHorizontalRuleCreateDocument(true) // create new Document when horizontal rule
              .withIncludeCodeBlock(false) // create separate Document for code blocks
              .withIncludeBlockquote(false); // create separate Document for blockquotes

          final var filename = r.getFilename();
          config.withAdditionalMetadata("filename", filename);

          final var reader = new MarkdownDocumentReader(r, config.build());
          final var documents = reader.get();

          final var splitter = new TokenTextSplitter();

          return new LoadedMarkdownFile(filename, documents, splitter.apply(documents));
        }).toList();
  }

}
