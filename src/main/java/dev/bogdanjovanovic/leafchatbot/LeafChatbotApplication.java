package dev.bogdanjovanovic.leafchatbot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LeafChatbotApplication {

  public static void main(String[] args) {
    SpringApplication.run(LeafChatbotApplication.class, args);
  }

  @Bean
  CommandLineRunner commandLineRunner(
      @Qualifier("openAiChatClient") ChatClient openAiChatClient,
      LeafMarkdownReader markdownReader,
      VectorStore vectorStore,
      OpenAiChatModel openAiChatModel) {
    return args -> {
//      ChatResponse chatResponse = openAiChatClient.prompt()
//          .user("Tell me a joke")
//          .call()
//          .chatResponse();
//      System.out.println(chatResponse.getResult().getOutput().getText());
      final var documents = markdownReader.loadMarkdown();

//      documents.forEach(d -> {
//        vectorStore.add(d.splitDocuments());
//      });

//      final var results = vectorStore.similaritySearch(SearchRequest.builder()
//          .query("POMs")
//          .topK(5)
//          .build());

      final var chatResponse = ChatClient.builder(openAiChatModel)
          .build().prompt()
          .advisors(new QuestionAnswerAdvisor(vectorStore))
          .user("What is POM container and how to use it?")
          .call()
          .chatResponse();

      System.out.println(chatResponse.getResult().getOutput().getText());
    };
  }

}
