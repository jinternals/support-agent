# Tools in AI Agents

- Enable agents to perform real-world tasks (e.g., create tickets, fetch data).
- Tools extend the agent’s capabilities beyond “thinking” into “doing.”

Some ways tools help:

- **Information access:** Tools like search (web, file_search, etc.) let the agent fetch up-to-date knowledge, private documents, or contextual data.

- **Action execution:** Tools like calendars, emails, or APIs allow the agent to take real actions: send reminders, schedule meetings, query a database, etc.

- **Specialized skills:** Tools such as Python execution, image generation, or code interpreters give the agent powers it doesn’t have natively (math, visualization, simulation, file creation).

- **Environment interaction:** In real systems, tools may be connected to IoT devices, cloud APIs, or CI/CD pipelines. This lets the agent automate deployments, run jobs, or control systems.

Agents use tools, but can become overloaded if they are provided with too many. This is often because the tool descriptions overlap, causing model confusion about which tool to use. One approach is to apply RAG (retrieval augmented generation) to tool descriptions in order to fetch only the most relevant tools for a task. Some recent papers have shown that this improve tool selection accuracy by 3-fold.

https://arxiv.org/abs/2505.03275?ref=blog.langchain.com
