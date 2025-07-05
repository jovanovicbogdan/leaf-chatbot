package dev.bogdanjovanovic.leafchatbot;

import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.Filter.Expression;
import org.springframework.ai.vectorstore.filter.Filter.ExpressionType;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngestService {

  private final PgVectorStore pgVectorStore;
  private final LeafMarkdownReader leafMarkdownReader;

  public IngestService(PgVectorStore pgVectorStore, LeafMarkdownReader leafMarkdownReader) {
    this.pgVectorStore = pgVectorStore;
    this.leafMarkdownReader = leafMarkdownReader;
  }

  @Transactional
  public void ingest() {
    final var markdownFiles = leafMarkdownReader.loadMarkdown();
    markdownFiles.forEach(mf -> {
      final var splitDocs = mf.splitDocuments();
      splitDocs.forEach(sd -> {
        final var metadata = sd.getMetadata();
        final var deleteOldVersion = new Expression(
            ExpressionType.EQ,
            new Filter.Key("filename"),
            new Filter.Value(metadata.get("filename")
        ));
        pgVectorStore.delete(deleteOldVersion);
      });
      pgVectorStore.add(splitDocs);
    });
  }

}
