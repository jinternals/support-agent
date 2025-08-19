package com.jinternals.support.agent.etl.workflow.dto;

import java.util.List;

public record IndexingWorkflowInput (String sourcePath, List<String> keywords, boolean reIndex){
}
