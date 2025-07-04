# Leaf Chatbot

Leaf Chatbot is a Retrieval-Augmented Generation (RAG) application designed to answer questions based on the content of ingested Markdown documents. LEAF (Lean Enterprise Automation Framework) is a test automation framework built on top of Playwright, used for E2E, UI and API testing.

## Tech stack

- Java 17
- Maven
- Spring Boot
- Spring AI
- PGVector
- Docker

## How to run

1. Set `OPENAI_API_KEY` environment variable.
2. Put Markdown files in `leaf-docs` directory in root project dir.
3. Run `docker compose up -d`

Ingesting data will calculate the embeddings of provided markdown files using OpenAI embedding model `text-embedding-ada-002` and store them in the `pgvector` database. Note that the endpoint below will first delete embeddings if there are any and recalculate again.

```shell
POST http://localhost:8080/api/v1/ingest
```

Ask a question

```shell
POST http://localhost:0880/api/v1/conversation

{
  "question": "How to create Page Object Model using LEAF?"
}
```

## Build an image and publish to a private registry

```shell
mvn spring-boot:build-image \
  -Dimage.publish=true \
  -Ddocker.publishRegistry.username=$DOCKER_USERNAME \
  -Ddocker.publishRegistry.password=$DOCKER_PAT
```
