package dev.kuku.vfl.hub.services.queue;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;

public interface QueueService {
    void addBlockToQueue(ToAddBlock block);

    void addBlockLogsToQueue(ToAddBlockLog blockLog);
}
