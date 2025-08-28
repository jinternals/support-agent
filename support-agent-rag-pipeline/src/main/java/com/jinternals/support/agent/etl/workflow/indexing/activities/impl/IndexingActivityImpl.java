package com.jinternals.support.agent.etl.workflow.indexing.activities.impl;

import com.jinternals.support.agent.etl.services.IndexingService;
import com.jinternals.support.agent.etl.workflow.TaskQueue;
import com.jinternals.support.agent.etl.workflow.indexing.activities.IndexingActivity;
import com.jinternals.support.agent.etl.workflow.indexing.activities.dto.IndexingActivityInput;
import com.jinternals.support.agent.etl.workflow.indexing.activities.dto.IndexingActivityOutput;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
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
                            indexingActivityInput.metadata(),
                            indexingActivityInput.reIndex())
                    .stream().map(Document::getId).toList();
            return new IndexingActivityOutput(indexingActivityInput.sourcePath(), chunkIds, "Indexed successfully.");

        } catch (Exception e) {
            log.error("Indexing failed", e);
            throw new RuntimeException("Indexing failed", e);
        }

    }
}
