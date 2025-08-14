package com.jinternals.support.agent.etl.workflow.activities.dto;

import java.util.List;

public record IndexingActivityInput(String sourcePath, List<String> keywords, boolean reIndex){ }
