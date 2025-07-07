package dev.bogdanjovanovic.leafchatbot;

import javax.validation.Valid;
import org.springframework.http.MediaType;
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

  @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> conversation(
      @Valid @RequestBody ConversationRequest conversationRequest) {
    return conversationService.conversation(conversationRequest);
  }

}
