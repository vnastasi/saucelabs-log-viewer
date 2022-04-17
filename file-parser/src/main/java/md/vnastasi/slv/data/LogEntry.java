package md.vnastasi.slv.data;

import javax.validation.constraints.NotNull;

public record LogEntry(
        int id,
        @NotNull String tim,
        @NotNull LogLevel level,
        @NotNull String message
) {
}
