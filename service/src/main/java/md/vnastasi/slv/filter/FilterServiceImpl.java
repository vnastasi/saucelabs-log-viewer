package md.vnastasi.slv.filter;

import md.vnastasi.slv.data.LogEntry;

import java.util.List;
import java.util.function.Predicate;

public class FilterServiceImpl implements FilterService {

    private final List<LogEntry> list;

    public FilterServiceImpl(List<LogEntry> list) {
        this.list = list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LogEntry> filer(List<Filter> filters) {
        if (filters.isEmpty()) throw new IllegalArgumentException("Filter list cannot be empty");
        var predicate = filters.stream().map(Predicate.class::cast).reduce(filters.get(0), Predicate::and);
        return list.parallelStream().filter(predicate).toList();
    }
}
