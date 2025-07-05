package dev.bogdanjovanovic.leafchatbot;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingest")
public class IngestController {

  private final IngestService ingestService;

  public IngestController(IngestService ingestService) {
    this.ingestService = ingestService;
  }

  @PostMapping
  public void ingest(HttpServletResponse response) {
    ingestService.ingest();
    response.setStatus(HttpStatus.CREATED.value());
  }

}
