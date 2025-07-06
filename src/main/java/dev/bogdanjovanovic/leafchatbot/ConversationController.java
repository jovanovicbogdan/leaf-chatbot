package dev.bogdanjovanovic.leafchatbot;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/conversation")
public class ConversationController {

  private final ConversationService conversationService;


  public ConversationController(ConversationService conversationService) {
    this.conversationService = conversationService;
  }

  @PostMapping
  public Flux<String> conversation(@RequestBody ConversationRequest conversationRequest) {
    return conversationService.conversation(conversationRequest);
  }

}
