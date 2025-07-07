package dev.bogdanjovanovic.leafchatbot;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingest")
@CrossOrigin
public class IngestController {

  private final IngestService ingestService;

  public IngestController(IngestService ingestService) {
    this.ingestService = ingestService;
  }

  @PostMapping
  public IngestResponse ingest(HttpServletResponse response) {
    final var ingestedResponse = ingestService.ingest();

    if (ingestedResponse.documentsIngested() > 0) {
      response.setStatus(HttpStatus.CREATED.value());
    } else {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
    }

    return ingestedResponse;
  }

}
