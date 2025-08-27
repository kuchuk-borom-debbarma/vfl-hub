package dev.kuku.vfl.hub.services.queue;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import dev.kuku.vfl.hub.model.exception.VFLException;
import dev.kuku.vfl.hub.services.vfl.VFLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@Service
public class MemoryQueueImpl implements QueueService {

    private static final int DEFAULT_CAPACITY = 10;

    private final Queue<ToAddBlock>    blockQueue = new PriorityQueue<>(DEFAULT_CAPACITY);
    private final Queue<ToAddBlockLog> logQueue   = new PriorityQueue<>(DEFAULT_CAPACITY);
    private final VFLService           vflService;
    private final Logger               log        = LoggerFactory.getLogger(MemoryQueueImpl.class);

    public MemoryQueueImpl(VFLService vflService) {
        this.vflService = vflService;
    }

    @Override
    public synchronized void addBlockToQueue(ToAddBlock block) {
        log.trace("addBlockToQueue {}", block);

        if (!blockQueue.offer(block)) {                        // queue full ➜ flush
            List<ToAddBlock> blocksToAdd = new ArrayList<>(blockQueue);
            blockQueue.clear();
            vflService.persistBlocks(blocksToAdd);

            /* retry with the incoming block; if it still fails, something is wrong */
            if (!blockQueue.offer(block)) {
                throw new VFLException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to enqueue block " + block
                );
            }
        }
    }

    @Override
    public synchronized void addBlockLogsToQueue(ToAddBlockLog blockLog) {
        log.trace("addBlockLogsToQueue {}", blockLog);

        if (!logQueue.offer(blockLog)) {                       // queue full ➜ flush
            List<ToAddBlockLog> logsToAdd = new ArrayList<>(logQueue);
            logQueue.clear();
            vflService.persistLogs(logsToAdd);

            /* retry with the incoming log; if it still fails, escalate */
            if (!logQueue.offer(blockLog)) {
                throw new VFLException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to enqueue block log " + blockLog
                );
            }
        }
    }
}
