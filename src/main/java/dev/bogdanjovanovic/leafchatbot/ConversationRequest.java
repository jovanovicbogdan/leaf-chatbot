package dev.bogdanjovanovic.leafchatbot;

import javax.validation.constraints.NotNull;

public record ConversationRequest(
    @NotNull(message = "Please provide a query string.")
    String query
) {

}
