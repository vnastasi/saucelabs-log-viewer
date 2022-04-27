package md.vnastasi.slv.filter;

import md.vnastasi.slv.model.LogEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public interface FilterService {

    @NotNull Stream<LogEntry> filter(@NotNull List<LogEntry> logEntries, @NotNull List<Filter> filters);
}
