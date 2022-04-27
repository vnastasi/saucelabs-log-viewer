package md.vnastasi.slv.parser;

import md.vnastasi.slv.model.LogEntry;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface LogFileParser {

    @NotNull List<LogEntry> parse(@NotNull Path filePath) throws IOException;
}
