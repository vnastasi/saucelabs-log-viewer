package md.vnastasi.slv.data;

import javax.validation.constraints.NotNull;

public record LogEntry(
        int id,
        @NotNull String time,
        @NotNull LogLevel level,
        @NotNull String message
) {
}
