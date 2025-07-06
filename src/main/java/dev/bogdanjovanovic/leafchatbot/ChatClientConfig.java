package dev.bogdanjovanovic.leafchatbot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ChatClientConfig {

  @Value("classpath:/prompts/system-message.st")
  private Resource systemResource;

  @Bean
  public ChatClient openAiChatClient(OpenAiChatModel chatModel, PgVectorStore pgVectorStore) {
    final var stTemplateRenderer = StTemplateRenderer.builder()
        .startDelimiterToken('<')
        .endDelimiterToken('>')
        .build();
    final var promptTemplate = PromptTemplate.builder()
        .renderer(stTemplateRenderer)
        .resource(systemResource)
        .build();

    final var qaAdvisor = QuestionAnswerAdvisor.builder(pgVectorStore)
        .promptTemplate(promptTemplate)
        .build();

    return ChatClient.builder(chatModel)
        .defaultAdvisors(qaAdvisor)
        .build();
  }

}
