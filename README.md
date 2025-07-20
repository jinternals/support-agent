# 🧑‍💼 Support Agent

> ⚠️ **Note:**This repository is subject to change as this is my learning project.


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

`json
{
  "answer": "I'm sorry to hear about the trouble you're having with logging in. \n\nFrom our knowledge base, here are some things you can do:\n\n1. Double-check your password and 2FA details. Sometimes, a minor typo can prevent you from logging in. You may refer to the article [Common Reasons for Login Failure](https://support.example.com/articles/login-troubleshooting) for more information.\n\n2. If you're locked out due to 2FA, you can contact support with ID verification to reset it. For more details, please see the article [Resetting 2FA When Locked Out](https://support.example.com/articles/reset-2fa).\n\n3. If you suspect an issue with your password, you can reset it by clicking 'Forgot Password' on the login screen and following the emailed instructions. Here is the article [How to Reset Your Password](https://support.example.com/articles/reset-password) for your reference.\n\nIf you have tried all these steps and still can't log in, I can help create a support ticket for you. Would you like me to do that?"
}
`
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

```json
{
  "answer": "I've created a high-priority support ticket for your login issue. The ticket ID is 321e7bdf-673b-4039-85aa-ade1e8e4bf4b. Our support team will reach out to you shortly to assist you further."
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
```json
{
  "answer": "Here is the list of your current support tickets:\n\n1. Ticket ID: 321e7bdf-673b-4039-85aa-ade1e8e4bf4b\n   Title: Login issue with correct credentials\n   Priority: HIGH\n   Description: The user can’t log in even though the password and 2FA are correct. The user has already tried all the troubleshooting steps mentioned in the knowledge base articles.\n\nIf you need further assistance, please let me know."
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

### 🔐 Set OpenAI API Key:

Add the following to your `~/.zshrc` (or `~/.bashrc` if you're using Bash):

```shell
export OPENAI_API_KEY=<your-key-here>
```

Then apply the changes:

```shell
source ~/.zshrc
```
