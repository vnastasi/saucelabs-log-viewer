package md.vnastasi.slv.filter;

import md.vnastasi.slv.model.LogEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FilterServiceImpl implements FilterService {

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Stream<LogEntry> filter(@NotNull List<LogEntry> logEntries, @NotNull List<Filter> filters) {
        if (filters.isEmpty()) throw new IllegalArgumentException("Filter list cannot be empty");
        var predicate = filters.stream().map(Predicate.class::cast).reduce(it -> true, Predicate::and);
        return logEntries.parallelStream().filter(predicate);
    }
}
