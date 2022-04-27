package md.vnastasi.slv.util;

import org.jetbrains.annotations.Nullable;

public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isNotEmpty(@Nullable String input) {
        return input != null && !input.isEmpty();
    }
}
