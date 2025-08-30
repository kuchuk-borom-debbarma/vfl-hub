package dev.kuku.vfl.hub.util.cursorUtil;

import dev.kuku.vfl.hub.model.dtos.pagination.BlockCursor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
//TODO single cursor util base
public class BlockCursorUtil {
    public static String encode(BlockCursor cursor) {
        String rawString = "${cursor.getBlockId()}:${cursor.getTimestamp()}";
        return Base64.getEncoder().encodeToString(rawString.getBytes(StandardCharsets.UTF_8));
    }

    public static BlockCursor decode(String cursorBlockId) {
        byte[] decodedBytes = Base64.getDecoder().decode(cursorBlockId.getBytes(StandardCharsets.UTF_8));
        String rawString = new String(decodedBytes, StandardCharsets.UTF_8);
        var splits = rawString.split(":");
        String blockId = splits[0];
        long timestamp = Long.parseLong(splits[1]);
        return new BlockCursor(blockId, timestamp);
    }
}
