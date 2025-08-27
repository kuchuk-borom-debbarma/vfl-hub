package dev.kuku.vfl.hub.services.vfl;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;

import java.util.List;


public interface VFLService {
    void persistBlocks(List<ToAddBlock> toAddBlocks);

    void persistLogs(List<ToAddBlockLog> logs);
}
