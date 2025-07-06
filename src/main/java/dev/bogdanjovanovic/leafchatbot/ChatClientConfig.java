package dev.bogdanjovanovic.leafchatbot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.StreamingChatModel;
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
    final var promptTemplate = PromptTemplate.builder()
        .renderer(
            StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
        .resource(systemResource)
//        .template()
//        .template("""
//            You are an expert assistant specialized in the Lean Enterprise Automation Framework (LEAF). You will receive documents retrieved from a vector database. These documents contain authoritative information about LEAF.
//
//            Your task is to answer user questions **only based on the retrieved documents**.
//            **Do not invent, assume, or fabricate any information.**
//            If the answer is not present in the provided documents, clearly state:
//            "The answer is not available in the retrieved documents."
//
//            ### Guidelines:
//            - **Use only** the content from the retrieved documents.
//            - **Do not include** general knowledge, personal opinions, or assumptions.
//            - If multiple interpretations are possible based on the documents, list them clearly.
//            - Be accurate, concise, and cite or reference the document content when helpful.
//
//            ### Format:
//            Always base your answer on the retrieved data. Optionally, reference document excerpts like this:
//            "According to the retrieved document, '...'"
//            """)
        .build();

    final var qaAdvisor = QuestionAnswerAdvisor.builder(pgVectorStore)
        .promptTemplate(promptTemplate)
        .build();

    return ChatClient.builder(chatModel)
        .defaultAdvisors(qaAdvisor)
        .build();
  }

}
