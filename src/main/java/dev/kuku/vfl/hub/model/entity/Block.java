package dev.kuku.vfl.hub.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.checkerframework.checker.nullness.qual.Nullable;

@Entity
@Table(name = "blocks")
public class Block {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private long createdAt;

    @Nullable
    @Column(name = "entered_at", nullable = true)
    private Long enteredAt;

    @Nullable
    @Column(name = "exited_at", nullable = true)
    private Long exitedAt;

    @Nullable
    @Column(name = "exit_message", nullable = true)
    private String exitMessage;

    @Nullable
    @Column(name = "returned_at", nullable = true)
    private Long returnedAt;

    @Column(name = "persisted_time", nullable = false)
    private long persistedTime;

    // Default constructor for JPA
    protected Block() {
        id = "";
        name = "";
        createdAt = 0;
    }

    // Constructor matching the original record
    public Block(String id, String name, long createdAt, @Nullable Long enteredAt,
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
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Nullable
    public Long getEnteredAt() {
        return enteredAt;
    }

    @Nullable
    public Long getExitedAt() {
        return exitedAt;
    }

    @Nullable
    public String getExitMessage() {
        return exitMessage;
    }

    @Nullable
    public Long getReturnedAt() {
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
        return "Block{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", createdAt=" + createdAt +
               ", enteredAt=" + enteredAt +
               ", exitedAt=" + exitedAt +
               ", exitMessage='" + exitMessage + '\'' +
               ", returnedAt=" + returnedAt +
               ", persistedTime=" + persistedTime +
               '}';
    }
}