package dev.kuku.vfl.hub.model.entity;

import jakarta.persistence.*;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "block_logs")
public class BlockLog {

    @Id
    private String id;

    @Column(name = "block_id", nullable = false)
    private String blockId;

    @Column(name = "message")
    private @Nullable String message;

    @Column(name = "parent_log_id")
    private @Nullable String parentLogId;

    @Column(name = "referenced_block_id")
    private @Nullable String referencedBlockId;

    @Column(name = "timestamp", nullable = false)
    private long timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type", nullable = false)
    private LogType logType;

    @Column(name = "persisted_time", nullable = false)
    private long persistedTime;

    // Default constructor for JPA
    protected BlockLog() {
        id = "";
        blockId = "";
        logType = LogType.INFO;
    }

    // Constructor matching the original record
    public BlockLog(String id, String blockId, @Nullable String message, @Nullable String parentLogId, @Nullable String referencedBlockId, long timestamp, LogType logType, long persistedTime) {
        this.id = id;
        this.blockId = blockId;
        this.message = message;
        this.parentLogId = parentLogId;
        this.referencedBlockId = referencedBlockId;
        this.timestamp = timestamp;
        this.logType = logType;
        this.persistedTime = persistedTime;
    }

    // Getters
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

    // Setters (if needed for JPA or business logic)
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

    @Override
    public String toString() {
        return "BlockLog{" + "id='" + id + '\'' + ", blockId='" + blockId + '\'' + ", message='" + message + '\'' + ", parentLogId='" + parentLogId + '\'' + ", referencedBlockId='" + referencedBlockId + '\'' + ", timestamp=" + timestamp + ", logType=" + logType + ", persistedTime=" + persistedTime + '}';
    }
}