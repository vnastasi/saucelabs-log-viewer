package md.vnastasi.slv.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import md.vnastasi.slv.data.LogEntry;
import md.vnastasi.slv.data.LogLevel;
import md.vnastasi.slv.parser.LogFileParser;
import md.vnastasi.slv.parser.LogFileParserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogFileParserImplTest {

    private static final String FILE_NAME = "sample.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private final LogFileParser parser = new LogFileParserImpl(objectMapper);

    @BeforeEach
    void createFile() throws IOException {
        var filePath = Paths.get(System.getProperty("java.io.tmpdir"), FILE_NAME);
        var randomizer = new SecureRandom();

        var list = IntStream.range(0, 500_000)
                .mapToObj(id -> {
                    var level = LogLevel.values()[Math.abs(randomizer.nextInt()) % LogLevel.values().length];
                    var time = LocalDateTime.now().format(FORMATTER);
                    var message = "Message";
                    return new LogEntry(id, time, level, message);
                })
                .toList();
        try (OutputStream stream = Files.newOutputStream(filePath)) {
            objectMapper.writeValue(stream, list);
        }
    }

    @Test
    void testDeserialization() throws IOException {
        var list = parser.parse(Paths.get(System.getProperty("java.io.tmpdir"), FILE_NAME));
        assertEquals(500_000, list.size());
    }
}
