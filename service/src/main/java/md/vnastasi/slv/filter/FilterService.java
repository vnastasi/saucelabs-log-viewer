package md.vnastasi.slv.filter;

import md.vnastasi.slv.data.LogEntry;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface FilterService {

    @NotNull List<LogEntry> filer(@NotNull List<Filter> filters);
}
