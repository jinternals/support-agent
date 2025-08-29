
# What is an LLM (Large Language Mode)

An LLM, or Large Language Model, is a type of deep learning model specialized in understanding, generating,
and manipulating human language (natural language). These models are trained on massive datasets containing
text from books, articles, websites, and other sources to learn patterns, semantics, grammar, and even reasoning.

### 1. Core Concept
- Language Models predict the next word or token in a sequence given the previous context. For example, given the input “The cat sat on the”, the model predicts “mat” as a likely next word.
- Large refers to the scale of the model — usually billions or even trillions of parameters (the model’s learned weights). This scale allows the model to capture complex patterns, semantics, and nuanced language understanding.

### 2. How LLMs Work Internally
#### a) Architecture: Transformer
- Modern LLMs are mostly based on the Transformer architecture (introduced in 2017 by Ashish Vaswani and his colleagues at Google).
    - Paper "Attention is All You Need".
    - https://proceedings.neurips.cc/paper_files/paper/2017/file/3f5ee243547dee91fbd053c1c4a845aa-Paper.pdf
- Transformers use self-attention mechanisms to weigh the importance of every word in the input relative to every other word.
- This enables the model to understand context deeply and handle long-range dependencies in text.

#### b) Training
- Trained on massive corpora with self-supervised learning.
- The model learns to predict missing or next tokens without explicit labeling.
- Example: Given a huge dataset, it learns probabilities of word sequences.

####  c) Parameters
- Parameters are weights that define the model.
- An LLM with billions of parameters has a vast capacity to encode language structure, facts, and some reasoning skills.

### 3. Capabilities of LLMs
- Text generation: Compose coherent, contextually relevant text.
- Translation: Convert text between languages.
- Summarization: Condense long documents into shorter summaries.
- Question answering: Provide answers based on input queries.
- Code generation: Write or complete programming code.
- Conversational agents: Power chatbots and virtual assistants.

### 4. Limitations
- Knowledge cutoff: LLMs only know what’s in their training data (usually up to a specific date).
- Hallucinations: Sometimes generate plausible-sounding but false or nonsensical outputs.
- Bias: Reflect biases in training data.
- Context window: Limited to a fixed-size input sequence (e.g., 4k or 8k tokens).

> https://docs.spring.io/spring-ai/reference/getting-started.html


## Model Context Protocol (MCP)
The Model Context Protocol (MCP) is a standardized protocol that enables AI models to interact with external tools
and resources in a structured way. It supports multiple transport mechanisms to provide flexibility across different environments.

- https://modelcontextprotocol.io/introduction
- https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html
- https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html

![mcp.png](../docs/mcp.png)

