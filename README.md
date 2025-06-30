# 🧑‍💼 Support Agent

> ⚠️ **Note:**This is subject to change as this is my learning project.


This is a demo application that showcases capabilities such as **Retrieval-Augmented Generation (RAG)** and **Tool Calling** using **Spring AI MCP (Model Control Plane)**.

## 📦 Modules

The application is divided into the following modules:

- [**support-agent-app**](support-agent-app)  
  Interacts with the LLM, MCP server, and vector store to perform intelligent support operations.

- [**support-agent-etl**](support-agent-etl)  
  Responsible for loading data into the vector store for RAG-based retrieval.

- [**support-mcp**](support-mcp)  
  MCP server that exposes support tools (e.g., for creating and managing tickets) to be invoked by the agent.


```shell
                                  ┌─────────────────────────────┐
                                  │    PostgreSQL + pgvector    │
                                  │    (Vector Store for RAG)   │
                                  └──────────────▲──────────────┘
                                                 │
                                                 │
                                           RAG Retrieval
                                                 │
    ┌──────────────────────────┐       ┌─────────┴───────────┐      ┌────────────────────────────┐
    │    support-agent-etl     │──────▶│   support-agent     │◀────▶│        support-mcp         │
    │  (ETL: Loads support     │       │ (RAG + Tool calling)│      │  (MCP Tools: CreateTicket) │
    │   data into pgvector)    │       └─────────▲───────────┘      └────────────────────────────┘
    └──────────────────────────┘                 │
                                                 │
                                          🔮 Spring AI
                                                 │
                                                 ▼
                                       🌐 OpenAI (LLM API)

```
## 🚀 Features

- ✅ Retrieval-Augmented Generation (RAG)
- ✅ Tool Invocation via MCP
- ✅ Modular Design for Clarity and Separation of Concerns

## 📡 Demo API Usage

Use the following sample requests to interact with the support agent.

<details>
<summary>🧾 Example: Ask a support question</summary>

```http
POST http://localhost:8080/ai/assistant/1
Content-Type: application/json

{
  "userId": "e85c20f0-ad28-4531-9ac4-158b9b4a3ece",
  "question": "I can’t log in even though my password and 2FA seem fine. Can you help?"
}
```
</details>

<details>
<summary>🧾 Example: Create a high priority support ticket</summary>

```http
POST http://localhost:8080/ai/assistant/1
Content-Type: application/json

{
  "userId": "e85c20f0-ad28-4531-9ac4-158b9b4a3ece",
  "question": "Please create ticket with high priority.."
}
```
</details>

<details>
<summary>🧾 Example: List all support tickets</summary>

```http
POST http://localhost:8080/ai/assistant/1
Content-Type: application/json

{
  "userId": "e85c20f0-ad28-4531-9ac4-158b9b4a3ece",
  "question": "Please give me list of my tickets"
}
```
</details>

---
> Built with [Spring AI](https://docs.spring.io/spring-ai/reference/) and powered by Open AI.


---

## ⚙️ How to Run

### 🚀 Start database services

```shell
docker-compose up -d
```

### 🔐 Set OpenAI API Key (for local LLM fallback or remote OpenAI access)

Add the following to your `~/.zshrc` (or `~/.bashrc` if you're using Bash):

```shell
export OPENAI_API_KEY=<your-key-here>
```

Then apply the changes:

```shell
source ~/.zshrc
```
