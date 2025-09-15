package dev.kuku.vfl.hub.model.dtos;


import org.springframework.lang.Nullable;

public class ToAddBlock {
    private final String id;
    private final String name;
    private final @Nullable String parentBlockId;
    private final long createdAt;

    public ToAddBlock(String id, String name, @Nullable String parentBlockId, long createdAt) {
        this.id = id;
        this.name = name;
        this.parentBlockId = parentBlockId;
        this.createdAt = createdAt;
    }

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

    @Override
    public String toString() {
        return "ToAddBlock{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", createdAt=" + createdAt +
               '}';
    }
}
