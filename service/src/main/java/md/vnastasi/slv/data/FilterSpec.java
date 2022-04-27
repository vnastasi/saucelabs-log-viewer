package md.vnastasi.slv.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record FilterSpec(
        @Nullable RangeSpec range,
        @Nullable List<LogLevel> logLevels,
        @Nullable MessageKeywordSpec messageKeywordSpec
) {

    public FilterSpec() {
        this(null, null, null);
    }

    public FilterSpec(@NotNull RangeSpec range) {
        this(range, null, null);
    }

    public FilterSpec(@NotNull List<LogLevel> logLevels) {
        this(null, logLevels, null);
    }

    public FilterSpec(@NotNull MessageKeywordSpec messageKeywordSpec) {
        this(null, null, messageKeywordSpec);
    }
}
