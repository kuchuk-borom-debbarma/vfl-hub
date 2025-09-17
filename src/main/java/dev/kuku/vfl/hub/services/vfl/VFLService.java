package dev.kuku.vfl.hub.services.vfl;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlock;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlockLog;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;


public interface VFLService {
    void persistBlocks(List<ToAddBlock> toAddBlocks);

    void updateBlockEntered(Map<String, Long> data);

    void updateBlockExited(Map<String, Long> data);

    void updateBlockReturned(Map<String, Long> data);

    void persistLogs(List<ToAddBlockLog> logs);

    List<ToFetchBlock> getRootBlocks(@Nullable String cursor, int limit);

    List<ToFetchBlockLog> getLogsByBlockId(String blockId, @Nullable String cursor, int limit);

    @Nullable
    ToFetchBlock getBlockById(String blockId);
}
