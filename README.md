# Leaf Chatbot

Leaf Chatbot is a Retrieval-Augmented Generation (RAG) application designed to answer questions
based on the content of ingested Markdown documents. LEAF (Lean Enterprise Automation Framework) is
a test automation framework built on top of Playwright, used for E2E, UI and API testing.

## Features

- Uses a token-based text splitter to divide documents into chunks.
- Computes embeddings for each chunk and stores them in a `pgvector` vector database.
- Performs **cosine** similarity search on user queries and enriches LLM prompts with retrieved
  context from the vector store.
- Supports chat history with the last **15** messages; the client is responsible for managing the
  `conversationId`.
- Enables response streaming.

## Tech Stack

- Java 17
- Maven
- Spring Boot
- Spring AI
- PGVector (PostgreSQL)
- Docker

## How to Run

1. Place your Markdown files in the `leaf-docs` directory located in the root of the project.
2. (Optional) Ensure the Docker image is built and pushed to your container registry. Update the
   image name in `docker-compose.yml` to match the one in your private registry. See
   the [example](#build-an-image-and-publish-to-a-private-registry) below for guidance.
3. Set the environment variables: `OPENAI_API_KEY`, `POSTGRES_USER`, and `POSTGRES_PASSWORD`.
4. Run `docker compose up -d`.
    - The backend will be available at `http://localhost:8080`.
    - `pgvector` will run on port `5432`.
    - `adminer` (a database manager) will be accessible via `http://localhost:8000`.
    - Log in to the PostgreSQL database using the `POSTGRES_USER` and `POSTGRES_PASSWORD`
      credentials.

## Ingest Data

When data is ingested, embeddings are generated for the provided markdown files using OpenAI's
`text-embedding-3-small` model and stored in a `pgvector` database. The endpoint below will first
remove any existing embeddings before recalculating them. It also recursively indexes all markdown
files located within the `leaf-docs` directory and its subdirectories.

```shell
POST http://localhost:8080/api/v1/ingest
```

## Ask a Question

The user's question, along with relevant documents and recent chat history, is sent to the OpenAI
`gpt-4o` model.

```shell
POST http://localhost:8080/api/v1/conversation

{
    "conversationId": "d41d001d-9f90-463b-9ff3-eb6d6da45e6f",
    "query": "What is a test fixture?"
}
```

## Testing

Integration tests use **Testcontainers** and Spring's test support to validate the full RAG
pipeline, from ingestion to LLM response.

A key test case evaluates the relevancy of the chatbot's answers using `RelevancyEvaluator`, which
assesses how well the model's response aligns with the retrieved context from the vector store.

### Key Components:

- **Testcontainers**: Provides isolated PostgreSQL instance with `pgvector`.
- **Document ingestion**: Markdown files are loaded and stored in the vector database before each
  test.
- **RetrievalAugmentationAdvisor**: Enhances prompt with context-relevant documents.
- **RelevancyEvaluator**: Validates if the generated answer matches the expected content scope.

```java
final var evaluationRequest = new EvaluationRequest(
    question,
    chatResponse.getMetadata().get(RetrievalAugmentationAdvisor.DOCUMENT_CONTEXT),
    chatResponse.getResult().getOutput().getText()
);

final var evaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));
final var evaluationResponse = evaluator.evaluate(evaluationRequest);

assertThat(evaluationResponse.isPass()).

isTrue();
```

## Build an Image and Publish to a Private Registry

```shell
mvn spring-boot:build-image \
  -Dimage.publish=true \
  -Ddocker.publishRegistry.username=$DOCKER_USERNAME \
  -Ddocker.publishRegistry.password=$DOCKER_PAT
```
