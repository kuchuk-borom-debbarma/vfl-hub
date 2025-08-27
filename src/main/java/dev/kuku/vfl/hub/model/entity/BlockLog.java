package dev.kuku.vfl.hub.model.entity;

import org.checkerframework.checker.nullness.qual.Nullable;

public record BlockLog(String id,
                       String blockId,
                       @Nullable String message,
                       @Nullable String parentLogId,
                       @Nullable String referencedBlockId,
                       long timestamp,
                       LogType logType,
                       long persistedTime) {
}
