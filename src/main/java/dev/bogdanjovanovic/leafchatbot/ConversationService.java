package dev.bogdanjovanovic.leafchatbot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ConversationService {

  private final ChatClient openAiChatClient;

  public ConversationService(@Qualifier("openAiChatClient") ChatClient openAiChatClient) {
    this.openAiChatClient = openAiChatClient;
  }

  public Flux<String> conversation(ConversationRequest conversationRequest) {
    return openAiChatClient.prompt()
        .user(conversationRequest.query().strip())
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationRequest.conversationId()))
        .stream()
        .content();
  }

}
