package com.jinternals.support.agent.etl.workflow.indexing.dto;

import java.util.Map;

public record IndexingWorkflowInput (String sourcePath, Map<String,String> metadata, boolean reIndex){
}
