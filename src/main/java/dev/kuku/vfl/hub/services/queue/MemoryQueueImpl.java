package dev.kuku.vfl.hub.services.queue;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import dev.kuku.vfl.hub.services.vfl.VFLService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * In memory queue that simply holds the data in a queue and flushes them periodically in background thread. OR when the capacity is full
 */
@Service
public class MemoryQueueImpl implements QueueService {

    private static final int DEFAULT_CAPACITY = 10;
    @Value("${vfl.queue.flush.interval.seconds:60}")
    private int PUSH_INTERVAL_SECONDS = 60; // Push every 60 seconds

    private final Queue<ToAddBlock> blockQueue = new LinkedList<>();
    private final Queue<ToAddBlockLog> logQueue = new LinkedList<>();
    private final VFLService vflService;
    private final Logger log = LoggerFactory.getLogger(MemoryQueueImpl.class);

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(
            r -> {
                Thread t = new Thread(r, "queue-scheduler");
                t.setDaemon(true);
                return t;
            }
    );

    public MemoryQueueImpl(VFLService vflService) {
        this.vflService = vflService;
    }

    @PostConstruct
    private void initialize() {
        startScheduledPush();
    }

    private void startScheduledPush() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                synchronized (this) {
                    pushBlocksIfNotEmpty();
                    pushLogsIfNotEmpty();
                }
            } catch (Exception e) {
                log.error("Error during scheduled push", e);
            }
        }, PUSH_INTERVAL_SECONDS, PUSH_INTERVAL_SECONDS, TimeUnit.SECONDS);

        log.info("Started scheduled push with interval of {} seconds", PUSH_INTERVAL_SECONDS);
    }

    private void pushBlocksIfNotEmpty() {
        if (!blockQueue.isEmpty()) {
            List<ToAddBlock> blocksToAdd = new ArrayList<>(blockQueue);
            blockQueue.clear();
            log.debug("Pushing {} blocks to VFL service", blocksToAdd.size());
            vflService.persistBlocks(blocksToAdd);
        }
    }

    private void pushLogsIfNotEmpty() {
        if (!logQueue.isEmpty()) {
            List<ToAddBlockLog> logsToAdd = new ArrayList<>(logQueue);
            logQueue.clear();
            log.debug("Pushing {} logs to VFL service", logsToAdd.size());
            vflService.persistLogs(logsToAdd);
        }
    }

    @Override
    public synchronized void addBlockToQueue(ToAddBlock block) {
        log.trace("addBlockToQueue {}", block);

        if (blockQueue.size() >= DEFAULT_CAPACITY) {
            pushBlocksIfNotEmpty();
        }

        blockQueue.offer(block);
    }

    @Override
    public synchronized void addBlockLogsToQueue(ToAddBlockLog blockLog) {
        log.trace("addBlockLogsToQueue {}", blockLog);

        if (logQueue.size() >= DEFAULT_CAPACITY) {
            pushLogsIfNotEmpty();
        }

        logQueue.offer(blockLog);
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down queue scheduler");
        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Push any remaining items before shutdown
        synchronized (this) {
            pushBlocksIfNotEmpty();
            pushLogsIfNotEmpty();
        }
    }
}
