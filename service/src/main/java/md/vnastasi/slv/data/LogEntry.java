package md.vnastasi.slv.data;

import org.jetbrains.annotations.NotNull;

public record LogEntry(
        int id,
        @NotNull String time,
        LogLevel level,
        String message
) {
}
