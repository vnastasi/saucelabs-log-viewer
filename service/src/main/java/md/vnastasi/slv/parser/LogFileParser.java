package md.vnastasi.slv.parser;

import md.vnastasi.slv.data.LogEntry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface LogFileParser {

    List<LogEntry> parse(Path filePath) throws IOException;
}
