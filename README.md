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

When data is ingested, embeddings are generated for the provided markdown files using OpenAI's `text-embedding-3-small` model and stored in a `pgvector` database. The endpoint below will first remove any existing embeddings before recalculating them. It also recursively indexes all markdown files located within the `leaf-docs` directory and its subdirectories.

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
