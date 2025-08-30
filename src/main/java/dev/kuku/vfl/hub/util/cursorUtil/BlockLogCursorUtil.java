package dev.kuku.vfl.hub.util.cursorUtil;

import dev.kuku.vfl.hub.model.dtos.pagination.BlockLogCursor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BlockLogCursorUtil {

    public static String encode(BlockLogCursor cursor) {
        String rawString = cursor.id() + ":" + cursor.timestamp();
        return Base64.getEncoder().encodeToString(rawString.getBytes(StandardCharsets.UTF_8));
    }

    public static BlockLogCursor decode(String blockLogCursor) {
        byte[] decodedBytes = Base64.getDecoder().decode(blockLogCursor.getBytes(StandardCharsets.UTF_8));
        String rawString = new String(decodedBytes, StandardCharsets.UTF_8);
        String[] splits = rawString.split(":");
        String id = splits[0];
        long timestamp = Long.parseLong(splits[1]);
        return new BlockLogCursor(id, timestamp);
    }
}