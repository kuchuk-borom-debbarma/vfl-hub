package dev.kuku.vfl.hub.services.vfl;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlock;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;


public interface VFLService {
    void persistBlocks(List<ToAddBlock> toAddBlocks);

    void persistLogs(List<ToAddBlockLog> logs);

    List<ToFetchBlock> getRootBlocks(@Nullable String cursor, int limit);
}
