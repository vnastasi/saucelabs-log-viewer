package md.vnastasi.slv.util;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static boolean isNotEmpty(@Nullable Collection<?> input) {
        return input != null && !input.isEmpty();
    }
}
