package dev.kuku.vfl.hub.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.checkerframework.checker.nullness.qual.Nullable;

@Entity(name = "blocks")
public record Block(@Id String id,
                    String name,
                    long createdAt,
                    long enteredAt,
                    long exitedAt,
                    @Nullable String exitMessage,
                    long persistedTime) {
}
