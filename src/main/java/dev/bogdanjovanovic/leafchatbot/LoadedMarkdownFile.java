package dev.bogdanjovanovic.leafchatbot;

import java.util.List;
import org.springframework.ai.document.Document;

public record LoadedMarkdownFile(
    String filename,
    List<Document> documents,
    List<Document> splitDocuments
) {

}
