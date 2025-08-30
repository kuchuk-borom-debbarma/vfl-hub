package dev.kuku.vfl.hub.util;

import java.time.Instant;

public class VFLUtil {
    public static long CurrentTime() {
        return Instant.now().toEpochMilli();
    }
}
