package dev.kuku.vfl.hub.model.dtos.pagination;

public class BlockCursor {
    private final String blockId;
    private final long timestamp;

    public BlockCursor(String blockId, long timestamp) {
        this.blockId = blockId;
        this.timestamp = timestamp;
    }

    public String getBlockId() {
        return blockId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
