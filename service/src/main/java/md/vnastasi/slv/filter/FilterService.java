package md.vnastasi.slv.filter;

import md.vnastasi.slv.data.LogEntry;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Stream;

public interface FilterService {

    @NotNull Stream<LogEntry> filter(@NotNull List<Filter> filters);
}
