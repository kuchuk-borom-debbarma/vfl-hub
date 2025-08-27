package dev.kuku.vfl.hub.model.dtos;

import dev.kuku.vfl.hub.model.entity.LogType;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ToAddBlockLog {
    private final String id;
    private final String blockId;
    private final @Nullable String message;
    private final @Nullable String parentLogId;
    private final @Nullable String referencedBlockId;
    private final long timestamp;
    private final LogType logType;

    public ToAddBlockLog(String id, String blockId, @Nullable String message, @Nullable String parentLogId, @Nullable String referencedBlockId, long timestamp, LogType logType) {
        this.id = id;
        this.blockId = blockId;
        this.message = message;
        this.parentLogId = parentLogId;
        this.referencedBlockId = referencedBlockId;
        this.timestamp = timestamp;
        this.logType = logType;
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

    public @Nullable String getParentLogId() {
        return parentLogId;
    }

    public @Nullable String getReferencedBlockId() {
        return referencedBlockId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public LogType getLogType() {
        return logType;
    }

    @Override
    public String toString() {
        return "ToAddBlockLog{" +
               "id='" + id + '\'' +
               ", blockId='" + blockId + '\'' +
               ", parentLogId='" + parentLogId + '\'' +
               ", referencedBlockId='" + referencedBlockId + '\'' +
               ", timestamp=" + timestamp +
               ", logType='" + logType + '\'' +
               '}';
    }
}
