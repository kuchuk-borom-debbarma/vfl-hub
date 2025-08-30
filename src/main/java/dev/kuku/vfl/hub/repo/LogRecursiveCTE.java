package dev.kuku.vfl.hub.repo;

import com.blazebit.persistence.CTE;
import dev.kuku.vfl.hub.model.entity.LogType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

@CTE
@Entity
public class LogRecursiveCTE {
    private String id;
    private String blockId;
    private @Nullable String message;
    private @Nullable String parentLogId;
    private @Nullable String referencedBlockId;
    private long timestamp;
    private LogType logType;
    private long persistedTime;
    private long depth;

    public LogRecursiveCTE() {
        id = "";
        blockId = "";
        logType = LogType.INFO;
    }

    public LogRecursiveCTE(String id, String blockId, @Nullable String message, @Nullable String parentLogId, @Nullable String referencedBlockId, long timestamp, LogType logType, long persistedTime, int depth) {
        this.id = id;
        this.blockId = blockId;
        this.message = message;
        this.parentLogId = parentLogId;
        this.referencedBlockId = referencedBlockId;
        this.timestamp = timestamp;
        this.logType = logType;
        this.persistedTime = persistedTime;
        this.depth = depth;
    }

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    @Id
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

    public long getPersistedTime() {
        return persistedTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    public void setParentLogId(@Nullable String parentLogId) {
        this.parentLogId = parentLogId;
    }

    public void setReferencedBlockId(@Nullable String referencedBlockId) {
        this.referencedBlockId = referencedBlockId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public void setPersistedTime(long persistedTime) {
        this.persistedTime = persistedTime;
    }
}
