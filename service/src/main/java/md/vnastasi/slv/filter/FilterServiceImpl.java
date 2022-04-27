package md.vnastasi.slv.filter;

import md.vnastasi.slv.data.LogEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FilterServiceImpl implements FilterService {

    private final Supplier<List<LogEntry>> listSupplier;

    public FilterServiceImpl(@NotNull Supplier<List<LogEntry>> listSupplier) {
        this.listSupplier = listSupplier;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Stream<LogEntry> filter(@NotNull List<Filter> filters) {
        if (filters.isEmpty()) throw new IllegalArgumentException("Filter list cannot be empty");
        var predicate = filters.stream().map(Predicate.class::cast).reduce(it -> true, Predicate::and);
        return listSupplier.get().parallelStream().filter(predicate);
    }
}
