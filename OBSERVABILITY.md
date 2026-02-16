# Spring AI Support Agent - Observability Setup

## Quick Start

1. **Start Prometheus and Grafana:**
   ```bash
   docker-compose up -d prometheus grafana
   ```

2. **Start the Support Agent:**
   ```bash
   mvn spring-boot:run -pl support-agent-app
   ```

3. **Access the dashboards:**
   - Prometheus: http://localhost:9090
   - Grafana: http://localhost:3001 (admin/admin)

---

## Prometheus Queries

### Spring AI Metrics

**Chat Operation Duration:**
```promql
rate(spring_ai_chat_client_operation_seconds_sum[5m]) / rate(spring_ai_chat_client_operation_seconds_count[5m])
```

**Token Usage Over Time:**
```promql
rate(gen_ai_client_token_usage_total[5m])
```

**Advisor Execution Time:**
```promql
spring_ai_advisor_seconds{advisor="MessageChatMemoryAdvisor"}
```

**RAG Query Performance:**
```promql
histogram_quantile(0.95, rate(spring_ai_vectorstore_query_seconds_bucket[5m]))
```

---

## Grafana Dashboard Setup

1. **Add Prometheus Data Source:**
   - Go to Configuration → Data Sources
   - Add Prometheus
   - URL: `http://prometheus:9090`
   - Save & Test

2. **Import Spring Boot Dashboard:**
   - Go to Dashboards → Import
   - Use ID: `19004` (Spring Boot 3.x Statistics)
   - Select Prometheus data source

3. **Create Custom Spring AI Dashboard:**
   - Create new dashboard
   - Add panels with the queries above

---

## Key Metrics to Monitor

| Metric | Description | Alert Threshold |
|--------|-------------|-----------------|
| `spring_ai_chat_client_operation_seconds` | Response time | > 5s |
| `gen_ai_client_token_usage_total` | Token consumption | Budget dependent |
| `spring_ai_advisor_seconds` | Advisor performance | > 1s per advisor |
| `spring_ai_vectorstore_query_seconds` | RAG retrieval time | > 500ms |
| `jvm_memory_used_bytes` | Memory usage | > 80% |

---

## Testing

```bash
# Generate some traffic
for i in {1..10}; do
  curl -X POST http://localhost:8080/ai/assistant/test-$i \
    -H "Content-Type: application/json" \
    -d "{\"userId\": \"user$i\", \"question\": \"How do I reset my password?\"}"
  sleep 2
done

# Check Prometheus targets
curl http://localhost:9090/api/v1/targets

# View metrics directly
curl http://localhost:8080/actuator/prometheus | grep spring_ai
```
