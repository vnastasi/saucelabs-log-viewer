package md.vnastasi.slv.usecase.model;

import org.jetbrains.annotations.Nullable;

public sealed interface RangeSpec {

    record LineNumber(int start, int end) implements RangeSpec {
    }

    record Time(@Nullable String start, @Nullable String end) implements RangeSpec {
    }
}
