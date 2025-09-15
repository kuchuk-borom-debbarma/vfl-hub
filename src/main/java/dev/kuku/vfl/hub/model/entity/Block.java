package dev.kuku.vfl.hub.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "blocks")
public class Block {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_block_id")
    private @Nullable String parentBlockId;

    @Column(name = "created_at", nullable = false)
    private long createdAt;

    @Column(name = "entered_at")
    private @Nullable Long enteredAt;

    @Column(name = "exited_at")
    private @Nullable Long exitedAt;

    @Column(name = "exit_message")
    private @Nullable String exitMessage;

    @Column(name = "returned_at")
    private @Nullable Long returnedAt;

    @Column(name = "persisted_time", nullable = false)
    private long persistedTime;

    // Default constructor for JPA
    protected Block() {
        id = "";
        name = "";
        createdAt = 0;
    }

    // Constructor matching the original record
    public Block(String id, String name, @Nullable String parentBlockId, long createdAt, @Nullable Long enteredAt,
                 @Nullable Long exitedAt, @Nullable String exitMessage,
                 @Nullable Long returnedAt, long persistedTime) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.enteredAt = enteredAt;
        this.exitedAt = exitedAt;
        this.exitMessage = exitMessage;
        this.returnedAt = returnedAt;
        this.persistedTime = persistedTime;
        this.parentBlockId = parentBlockId;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public @Nullable String getParentBlockId() {
        return parentBlockId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public @Nullable Long getEnteredAt() {
        return enteredAt;
    }

    public @Nullable Long getExitedAt() {
        return exitedAt;
    }

    public @Nullable String getExitMessage() {
        return exitMessage;
    }

    public @Nullable Long getReturnedAt() {
        return returnedAt;
    }

    public long getPersistedTime() {
        return persistedTime;
    }

    // Setters (if needed for JPA or business logic)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentBlockId(String parentBlockId) {
        this.parentBlockId = parentBlockId;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setEnteredAt(@Nullable Long enteredAt) {
        this.enteredAt = enteredAt;
    }

    public void setExitedAt(@Nullable Long exitedAt) {
        this.exitedAt = exitedAt;
    }

    public void setExitMessage(@Nullable String exitMessage) {
        this.exitMessage = exitMessage;
    }

    public void setReturnedAt(@Nullable Long returnedAt) {
        this.returnedAt = returnedAt;
    }

    public void setPersistedTime(long persistedTime) {
        this.persistedTime = persistedTime;
    }

    @Override
    public String toString() {
        return """
                Block($id, $name, $parentBlockId, $createdAt, $enteredAt, $exitedAt, $returnedAt, $persistedTime)
                """;
    }
}