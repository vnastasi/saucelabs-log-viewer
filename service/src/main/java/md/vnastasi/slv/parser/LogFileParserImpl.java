package md.vnastasi.slv.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import md.vnastasi.slv.data.LogEntry;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LogFileParserImpl implements LogFileParser {

    private static final TypeReference<List<LogEntry>> TYPE_TOKEN = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public LogFileParserImpl(@NotNull ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public @NotNull List<LogEntry> parse(@NotNull Path filePath) throws IOException {
        return objectMapper.readValue(Files.newInputStream(filePath), TYPE_TOKEN);
    }
}
