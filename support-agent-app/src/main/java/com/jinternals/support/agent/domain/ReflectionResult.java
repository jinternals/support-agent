package com.jinternals.support.agent.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ReflectionResult(
        @JsonProperty("overallScore") Double overallScore,
        @JsonProperty("accuracy") Double accuracy,
        @JsonProperty("completeness") Double completeness,
        @JsonProperty("tone") Double tone,
        @JsonProperty("actionability") Double actionability,
        @JsonProperty("issues") List<String> issues,
        @JsonProperty("improvedResponse") String improvedResponse) {
    public boolean needsImprovement() {
        return overallScore != null && overallScore < 7.0;
    }

    public String getFinalResponse(String originalResponse) {
        return needsImprovement() && improvedResponse != null ? improvedResponse : originalResponse;
    }
}
