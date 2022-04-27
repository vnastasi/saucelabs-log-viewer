package md.vnastasi.slv.usecase.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record FilterSpec(
        @Nullable RangeSpec range,
        @Nullable List<String> logLevels,
        @Nullable MessageKeywordSpec messageKeywordSpec
) {

    public FilterSpec() {
        this(null, null, null);
    }

    public FilterSpec(@NotNull RangeSpec range) {
        this(range, null, null);
    }

    public FilterSpec(@NotNull List<String> logLevels) {
        this(null, logLevels, null);
    }

    public FilterSpec(@NotNull MessageKeywordSpec messageKeywordSpec) {
        this(null, null, messageKeywordSpec);
    }
}
