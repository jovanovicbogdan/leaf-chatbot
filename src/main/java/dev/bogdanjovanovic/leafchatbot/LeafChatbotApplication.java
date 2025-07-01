package dev.bogdanjovanovic.leafchatbot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
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
  CommandLineRunner commandLineRunner(@Qualifier("openAiChatClient") ChatClient openAiChatClient) {
    return args -> {
      ChatResponse chatResponse = openAiChatClient.prompt()
          .user("Tell me a joke")
          .call()
          .chatResponse();
      System.out.println(chatResponse.getResult().getOutput().getText());
    };
  }

}
