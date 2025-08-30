package dev.kuku.vfl.hub.model.dtos;

import dev.kuku.vfl.hub.model.dtos.pagination.BlockLogCursor;
import dev.kuku.vfl.hub.model.entity.LogType;
import dev.kuku.vfl.hub.util.cursorUtil.BlockLogCursorUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ToFetchBlockLog {
    private final String id;
    private final String blockId;
    private final @Nullable String message;
    private @Nullable ToFetchBlock referencedBlock;
    private final long timestamp;
    private final LogType logType;
    private final String cursor;

    public ToFetchBlockLog(String id, String blockId, @Nullable String message, @Nullable ToFetchBlock referencedBlock, long timestamp, LogType logType) {
        this.id = id;
        this.blockId = blockId;
        this.message = message;
        this.referencedBlock = referencedBlock;
        this.timestamp = timestamp;
        this.logType = logType;
        BlockLogCursor c = new BlockLogCursor(id, timestamp);
        this.cursor = BlockLogCursorUtil.encode(c);
    }

    public String getId() {
        return id;
    }

    public String getBlockId() {
        return blockId;
    }

    public @Nullable String getMessage() {
        return message;
    }

    public @Nullable ToFetchBlock getReferencedBlock() {
        return referencedBlock;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public LogType getLogType() {
        return logType;
    }

    public String getCursor() {
        return cursor;
    }


}
