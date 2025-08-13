package com.jinternals.support.agent.etl.workflow.activities.impl;

import com.jinternals.support.agent.etl.services.IndexingService;
import com.jinternals.support.agent.etl.workflow.TaskQueue;
import com.jinternals.support.agent.etl.workflow.activities.IndexingActivity;
import com.jinternals.support.agent.etl.workflow.activities.dto.IndexingActivityInput;
import com.jinternals.support.agent.etl.workflow.activities.dto.IndexingActivityOutput;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ActivityImpl(taskQueues = TaskQueue.INDEXING_TASK_QUEUE)
public class IndexingActivityImpl implements IndexingActivity {

    private final IndexingService indexingService;

    @Override
    public IndexingActivityOutput indexSource(IndexingActivityInput indexingActivityInput) {
        try {
            List<String> chunkIds = indexingService.indexDocumentFromURL(
                            indexingActivityInput.sourcePath(),
                            indexingActivityInput.keywords(),
                            indexingActivityInput.reIndex())
                    .stream().map(Document::getId).toList();
            return new IndexingActivityOutput(indexingActivityInput.sourcePath(), chunkIds, "Indexed successfully.");

        } catch (Exception e) {
            throw new RuntimeException("Indexing failed");
        }

    }
}
