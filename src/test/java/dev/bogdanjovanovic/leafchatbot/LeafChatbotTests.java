package dev.bogdanjovanovic.leafchatbot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

public class LeafChatbotTests extends AbstractIntegrationTest {

  private List<Document> knowledgeBaseDocuments;

  @Autowired
  private PgVectorStore pgVectorStore;
  @Autowired
  private ChatModel chatModel;
  @Value("classpath:/leaf-docs/*")
  private List<Resource> knowledgeBaseResources;

  @BeforeEach
  void setUp() {
    knowledgeBaseResources.forEach(kb -> {
      final var markdownReader = new MarkdownDocumentReader(kb, MarkdownDocumentReaderConfig.defaultConfig());
      knowledgeBaseDocuments = markdownReader.read();
      pgVectorStore.add(knowledgeBaseDocuments);
    });
  }

  @AfterEach
  void tearDown() {
    pgVectorStore.delete(knowledgeBaseDocuments.stream().map(Document::getId).toList());
  }

  @Test
  void evaluateRelevancy() {
    final var question = "What is LEAF?";

    final var ragAdvisor = RetrievalAugmentationAdvisor.builder()
        .documentRetriever(VectorStoreDocumentRetriever.builder()
            .vectorStore(pgVectorStore)
            .build())
        .build();

    final var chatResponse = ChatClient.builder(chatModel).build()
        .prompt(question)
        .advisors(ragAdvisor)
        .call()
        .chatResponse();

    assertThat(chatResponse).isNotNull();

    final var evaluationRequest = new EvaluationRequest(
        question,
        chatResponse.getMetadata().get(RetrievalAugmentationAdvisor.DOCUMENT_CONTEXT),
        chatResponse.getResult().getOutput().getText()
    );

    final var evaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));

    final var evaluationResponse = evaluator.evaluate(evaluationRequest);

    assertThat(evaluationResponse.isPass()).isTrue();
  }

}
