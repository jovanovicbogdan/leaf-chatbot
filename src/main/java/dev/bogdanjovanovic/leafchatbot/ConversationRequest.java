package dev.bogdanjovanovic.leafchatbot;

import javax.validation.constraints.NotNull;

public record ConversationRequest(
    @NotNull(message = "Please provide conversation id.")
    String conversationId,
    @NotNull(message = "Please provide query string.")
    String query
) {

}
