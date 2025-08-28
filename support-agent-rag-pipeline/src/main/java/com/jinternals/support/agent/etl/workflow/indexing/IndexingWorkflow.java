package com.jinternals.support.agent.etl.workflow.indexing;


import com.jinternals.support.agent.etl.workflow.indexing.dto.IndexingWorkflowInput;
import com.jinternals.support.agent.etl.workflow.indexing.dto.IndexingWorkflowOutput;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface IndexingWorkflow {
    @WorkflowMethod
    public IndexingWorkflowOutput triggerWorkflow(IndexingWorkflowInput input);
}
