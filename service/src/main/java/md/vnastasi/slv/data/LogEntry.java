package md.vnastasi.slv.data;

import org.jetbrains.annotations.NotNull;

public record LogEntry(
        int id,
        @NotNull String time,
        LogLevel level,
        String message
) {

    public String join() {
        return String.format("%7d:\t%s\t%s\t%s", id, time, level.name(), message);
    }
}
