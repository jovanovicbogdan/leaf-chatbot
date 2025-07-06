package dev.bogdanjovanovic.leafchatbot;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

  @Container
  static PostgreSQLContainer<?> pgvector = new PostgreSQLContainer<>(
      DockerImageName.parse("pgvector/pgvector:0.8.0-pg17"));

  static {
    pgvector.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", pgvector::getJdbcUrl);
    registry.add("spring.datasource.username", pgvector::getUsername);
    registry.add("spring.datasource.password", pgvector::getPassword);
  }

}
