package md.vnastasi.slv.usecase;

import md.vnastasi.slv.model.FilterSpec;
import md.vnastasi.slv.model.LogEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CreateLogItemsUseCase {

    List<LogEntry> execute(@NotNull FilterSpec filterSpec);
}
