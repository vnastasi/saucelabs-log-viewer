package md.vnastasi.slv.model;

import org.jetbrains.annotations.NotNull;

public record LogEntry(
        int id,
        @NotNull String time,
        LogLevel level,
        String message
) {

    public String join() {
        return String.format("%7d\t\t%s %s %s", id, time, level.name(), message);
    }
}
