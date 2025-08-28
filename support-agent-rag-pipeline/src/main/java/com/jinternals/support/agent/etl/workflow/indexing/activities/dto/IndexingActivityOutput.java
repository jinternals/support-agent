package com.jinternals.support.agent.etl.workflow.indexing.activities.dto;

import java.util.List;

public record IndexingActivityOutput(String sourcePath, List<String> chunkIds, String message){
}
