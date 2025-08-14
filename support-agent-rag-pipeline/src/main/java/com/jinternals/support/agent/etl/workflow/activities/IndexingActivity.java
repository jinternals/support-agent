package com.jinternals.support.agent.etl.workflow.activities;

import com.jinternals.support.agent.etl.workflow.activities.dto.IndexingActivityInput;
import com.jinternals.support.agent.etl.workflow.activities.dto.IndexingActivityOutput;
import io.temporal.activity.ActivityInterface;

import java.util.List;

@ActivityInterface
public interface IndexingActivity {

    IndexingActivityOutput indexSource(IndexingActivityInput indexingActivityInput);

}
