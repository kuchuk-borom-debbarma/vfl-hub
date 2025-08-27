package dev.kuku.vfl.hub.model.dtos;


public class ToAddBlock {
    private final String id;
    private final String name;
    private final long createdAt;

    public ToAddBlock(String id, String name, long createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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
