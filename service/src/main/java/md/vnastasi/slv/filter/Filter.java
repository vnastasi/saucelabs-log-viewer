package md.vnastasi.slv.filter;

import md.vnastasi.slv.data.LogEntry;
import md.vnastasi.slv.data.LogLevel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public sealed interface Filter extends Predicate<LogEntry> {

    record ByLogLevel(@NotNull List<LogLevel> levels) implements Filter {

        @Override
        public boolean test(LogEntry logEntry) {
            return levels.contains(logEntry.level());
        }
    }

    record ByMessage(@NotNull String keyword, boolean caseSensitive) implements Filter {

        @Override
        public boolean test(LogEntry logEntry) {
            var normalizedMessage = logEntry.message();
            var normalizedKeyword = keyword;
            if (!caseSensitive) {
                normalizedMessage = normalizedMessage.toLowerCase(Locale.ROOT);
                normalizedKeyword = normalizedKeyword.toLowerCase(Locale.ROOT);
            }
            return normalizedMessage.contains(normalizedKeyword);
        }
    }

    record ByLineRange(int start, int end) implements Filter {

        @Override
        public boolean test(LogEntry logEntry) {
            return logEntry.id() >= start && logEntry.id() <= end;
        }
    }

    record ByTimeRange(@NotNull String start, @NotNull String end) implements Filter {

        @Override
        public boolean test(LogEntry logEntry) {
            return logEntry.time().compareTo(start) >= 0 && logEntry.time().compareTo(end) <= 0;
        }
    }
}
