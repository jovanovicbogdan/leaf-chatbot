package dev.bogdanjovanovic.leafchatbot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.PostgresChatMemoryRepositoryDialect;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ChatClientConfig {

  @Value("classpath:/prompts/system-message-v2.st")
  private Resource systemResource;
  private final JdbcChatMemoryRepository jdbcChatMemoryRepository;

  public ChatClientConfig(JdbcTemplate jdbcTemplate) {
    this.jdbcChatMemoryRepository = JdbcChatMemoryRepository.builder()
        .jdbcTemplate(jdbcTemplate)
        .dialect(new PostgresChatMemoryRepositoryDialect())
        .build();
  }

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

    final var chatMemory = MessageWindowChatMemory.builder()
        .chatMemoryRepository(jdbcChatMemoryRepository)
        .maxMessages(15)
        .build();

    final var chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

    return ChatClient.builder(chatModel)
        .defaultAdvisors(qaAdvisor, chatMemoryAdvisor)
        .build();
  }

}
