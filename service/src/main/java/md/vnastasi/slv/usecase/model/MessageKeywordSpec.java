package md.vnastasi.slv.usecase.model;

import org.jetbrains.annotations.Nullable;

public record MessageKeywordSpec(
        @Nullable String keyword,
        boolean caseSensitive
) {
}
