package dev.bogdanjovanovic.leafchatbot;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class CustomMarkdownReader {

  private final Resource resource;

  public CustomMarkdownReader(@Value("classpath:leaf-docs/effective-poms.md") Resource resource) {
    this.resource = resource;
  }

  public List<Document> loadMarkdown() {
    MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
        .withHorizontalRuleCreateDocument(true)
        .withIncludeCodeBlock(false)
        .withIncludeBlockquote(false)
        .withAdditionalMetadata("filename", "effective-poms.md")
        .build();

    MarkdownDocumentReader reader = new MarkdownDocumentReader(this.resource, config);

    return reader.get();
  }

}
