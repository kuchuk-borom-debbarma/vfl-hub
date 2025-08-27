package dev.kuku.vfl.hub.model.dtos;

import org.checkerframework.checker.nullness.qual.Nullable;

public class ToAddBlockLog {
    private final String id;
    private final String blockId;
    private final @Nullable String parentLogId;
    private final @Nullable String referencedBlockId;
    private final long timestamp;
    private final String logType;

    public ToAddBlockLog(String id, String blockId, @Nullable String parentLogId, @Nullable String referencedBlockId, long timestamp, String logType) {
        this.id = id;
        this.blockId = blockId;
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

    public @Nullable String getParentLogId() {
        return parentLogId;
    }

    public @Nullable String getReferencedBlockId() {
        return referencedBlockId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLogType() {
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
