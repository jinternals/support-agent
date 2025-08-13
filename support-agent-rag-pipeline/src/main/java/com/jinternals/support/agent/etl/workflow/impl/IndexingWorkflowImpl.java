package com.jinternals.support.agent.etl.workflow.impl;

import com.jinternals.support.agent.etl.workflow.activities.IndexingActivity;
import com.jinternals.support.agent.etl.workflow.IndexingWorkflow;
import com.jinternals.support.agent.etl.workflow.activities.dto.IndexingActivityInput;
import com.jinternals.support.agent.etl.workflow.activities.dto.IndexingActivityOutput;
import com.jinternals.support.agent.etl.workflow.dto.IndexingWorkflowInput;
import com.jinternals.support.agent.etl.workflow.TaskQueue;
import com.jinternals.support.agent.etl.workflow.dto.IndexingWorkflowOutput;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;

import java.time.Duration;

@WorkflowImpl(taskQueues = TaskQueue.INDEXING_TASK_QUEUE)
public class IndexingWorkflowImpl implements IndexingWorkflow {

    private final RetryOptions retryOptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(2))
            .setBackoffCoefficient(1.5)
            .setMaximumAttempts(10)
            .build();

    private final ActivityOptions  activityOptions = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(120))
            .setScheduleToCloseTimeout(Duration.ofSeconds(130))
            .setTaskQueue(TaskQueue.INDEXING_TASK_QUEUE)
            .setRetryOptions(retryOptions)
            .build();

    private final IndexingActivity activity =  Workflow.newActivityStub(IndexingActivity.class, activityOptions);

    @Override
    public IndexingWorkflowOutput triggerWorkflow(IndexingWorkflowInput input) {
        var indexingActivityInput = new IndexingActivityInput(input.sourcePath(), input.keywords(), input.reIndex());
        IndexingActivityOutput indexingActivityOutput = activity.indexSource(indexingActivityInput);
        return new IndexingWorkflowOutput(indexingActivityOutput);
    }
}
