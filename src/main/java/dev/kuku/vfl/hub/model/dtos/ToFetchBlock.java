package dev.kuku.vfl.hub.model.dtos;

import dev.kuku.vfl.hub.model.dtos.pagination.BlockCursor;
import dev.kuku.vfl.hub.util.cursorUtil.BlockCursorUtil;
import org.springframework.lang.Nullable;

public class ToFetchBlock {
    public final String id;
    public final String name;
    public final long createdAt;
    public final @Nullable Long enteredAt;
    public final @Nullable Long exitedAt;
    public final @Nullable Long returnedAt;
    public final @Nullable String exitMessage;
    public final String cursor;

    public ToFetchBlock(String id, String name, long createdAt, @Nullable Long enteredAt, @Nullable Long exitedAt, @Nullable Long returnedAt, @Nullable String exitMessage) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.enteredAt = enteredAt;
        this.exitedAt = exitedAt;
        this.returnedAt = returnedAt;
        this.exitMessage = exitMessage;
        this.cursor = BlockCursorUtil.encode(new BlockCursor(id, createdAt));
    }
}
