# Grafana Dashboard Setup Guide

## Quick Import

1. **Access Grafana:**
   - Open http://localhost:3001
   - Login: `admin` / `admin`

2. **Add Prometheus Data Source:**
   ```
   Configuration → Data Sources → Add data source
   - Type: Prometheus
   - Name: prometheus
   - URL: http://prometheus:9090
   - Click "Save & Test"
   ```

3. **Import Dashboard:**
   ```
   Dashboards → Import
   - Click "Upload JSON file"
   - Select: grafana-dashboard-spring-ai.json
   - Select Prometheus data source
   - Click "Import"
   ```

---

## Dashboard Panels

### 1. Chat Operation Response Time
- **Metrics:** Average, P95, P99 response times
- **Alert Threshold:** > 5 seconds
- **What to watch:** Spikes indicate performance issues

### 2. Chat Operation Throughput
- **Metrics:** Requests per second
- **What to watch:** Traffic patterns and load

### 3. Token Usage Rate
- **Metrics:** Input/Output tokens per second
- **What to watch:** Cost implications

### 4. Total Token Usage (24h)
- **Metrics:** Cumulative tokens
- **Alert Thresholds:**
  - Yellow: 100,000 tokens
  - Red: 500,000 tokens

### 5. Advisor Execution Time
- **Metrics:** Memory, RAG, Logger advisor performance
- **Alert Threshold:** > 1 second per advisor
- **What to watch:** Bottlenecks in advisor chain

### 6. Vector Store Query Performance
- **Metrics:** Average and P95 RAG query times
- **Alert Threshold:** > 500ms
- **What to watch:** Vector DB performance

### 7. JVM Memory Usage
- **Metrics:** Heap used vs max
- **Alert Threshold:** > 80% heap usage
- **What to watch:** Memory leaks

### 8. JVM Threads
- **Metrics:** Live and daemon threads
- **What to watch:** Thread pool exhaustion

---

## Testing the Dashboard

```bash
# Generate test traffic
for i in {1..20}; do
  curl -X POST http://localhost:8080/ai/assistant/test-$i \
    -H "Content-Type: application/json" \
    -d "{\"userId\": \"user$i\", \"question\": \"How do I reset my password?\"}"
  sleep 1
done
```

Watch the dashboard update in real-time!

---

## Alerting (Optional)

Create alerts in Grafana for:

1. **High Response Time:**
   ```promql
   rate(spring_ai_chat_client_operation_seconds_sum[5m]) / 
   rate(spring_ai_chat_client_operation_seconds_count[5m]) > 5
   ```

2. **High Token Usage:**
   ```promql
   increase(gen_ai_client_token_usage_total[1h]) > 50000
   ```

3. **RAG Performance:**
   ```promql
   histogram_quantile(0.95, rate(spring_ai_vectorstore_query_seconds_bucket[5m])) > 0.5
   ```

---

## Customization

Edit panels to:
- Adjust time ranges
- Add more metrics
- Change visualization types
- Set custom thresholds
- Add annotations
