package com.jinternals.support.agent.etl.workflow.indexing.activities.dto;

import java.util.List;
import java.util.Map;

public record IndexingActivityInput(String sourcePath, Map<String,String> metadata, boolean reIndex){ }
