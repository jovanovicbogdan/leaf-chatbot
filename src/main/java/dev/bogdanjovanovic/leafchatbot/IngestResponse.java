package dev.bogdanjovanovic.leafchatbot;

public record IngestResponse(
    Long documentsIngested,
    String message
) {

}
