package com.jinternals.support.agent.etl.workflow.indexing.activities;

import com.jinternals.support.agent.etl.workflow.indexing.activities.dto.IndexingActivityInput;
import com.jinternals.support.agent.etl.workflow.indexing.activities.dto.IndexingActivityOutput;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface IndexingActivity {

    IndexingActivityOutput indexSource(IndexingActivityInput indexingActivityInput);

}
