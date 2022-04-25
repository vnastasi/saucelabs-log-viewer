package md.vnastasi.slv.filter;

import md.vnastasi.slv.data.LogEntry;
import md.vnastasi.slv.data.LogLevel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FilterServiceImplTest {

    private final List<LogEntry> list = List.of(
            new LogEntry(1, "00:00:00", LogLevel.INFO, "Some arbitrary message"),
            new LogEntry(2, "00:00:01", LogLevel.DEBUG, "!@#$%^&*()"),
            new LogEntry(3, "00:00:10", LogLevel.DEBUG, "Some ArBitRArY message"),
            new LogEntry(4, "00:01:34", LogLevel.INFO, "Some ArBitRArY message"),
            new LogEntry(5, "00:01:34", LogLevel.DEBUG, "Some ArBitRArY message"),
            new LogEntry(6, "00:02:56", LogLevel.VERBOSE, "Some arbitrary message"),
            new LogEntry(7, "00:02:57", LogLevel.INFO, "Some arbitrary message"),
            new LogEntry(8, "00:09:01", LogLevel.VERBOSE, "qwerty"),
            new LogEntry(9, "00:10:02", LogLevel.DEBUG, "Some arbitrary message"),
            new LogEntry(10, "00:10:03", LogLevel.INFO, "Some arbitrary message"),
            new LogEntry(11, "01:45:23", LogLevel.VERBOSE, "Some arbitrary message"),
            new LogEntry(12, "01:46:23", LogLevel.VERBOSE, ""),
            new LogEntry(13, "02:00:00", LogLevel.DEBUG, "1234567890"),
            new LogEntry(14, "03:00:00", LogLevel.VERBOSE, "Some arbitrary message"),
            new LogEntry(15, "03:34:59", LogLevel.INFO, "Some arbitrary message"),
            new LogEntry(16, "03:35:00", LogLevel.DEBUG, "1234567890"),
            new LogEntry(17, "10:00:00", LogLevel.INFO, "Some arbitrary message"),
            new LogEntry(18, "12:00:00", LogLevel.VERBOSE, "1234567890"),
            new LogEntry(19, "13:00:00", LogLevel.VERBOSE, "1234567890"),
            new LogEntry(20, "20:59:00", LogLevel.INFO, "qwerty"),
            new LogEntry(21, "21:01:24", LogLevel.INFO, "qwerty"),
            new LogEntry(22, "23:59:59", LogLevel.DEBUG, ""),
            new LogEntry(23, "23:59:59", LogLevel.INFO, "Some arbitrary message")
    );

    private final FilterService filterService = new FilterServiceImpl(list);

    @Test
    void verifyFilterByMessage() {
        var filter = new Filter.ByMessage("arbitrary", true);
        var result = filterService.filer(List.of(filter));
        assertThat(result).extracting(LogEntry::id).containsExactly(1, 6, 7, 9, 10, 11, 14, 15, 17, 23);
    }

    @Test
    void verifyFilterByMessageCaseInsensitive() {
        var filter = new Filter.ByMessage("arbitrary", false);
        var result = filterService.filer(List.of(filter));
        assertThat(result).extracting(LogEntry::id).containsExactly(1, 3, 4, 5, 6, 7, 9, 10, 11, 14, 15, 17, 23);
    }

    @Test
    void verifyFilterByOneLogLevel() {
        var filter = new Filter.ByLogLevel(List.of(LogLevel.VERBOSE));
        var result = filterService.filer(List.of(filter));
        assertThat(result).extracting(LogEntry::id).containsExactly(6, 8, 11, 12, 14, 18, 19);
    }

    @Test
    void verifyFilterByTwoLogLevels() {
        var filter = new Filter.ByLogLevel(List.of(LogLevel.VERBOSE, LogLevel.INFO));
        var result = filterService.filer(List.of(filter));
        assertThat(result).extracting(LogEntry::id).containsExactly(1, 4, 6, 7, 8, 10, 11, 12, 14, 15, 17, 18, 19, 20, 21, 23);
    }

    @Test
    void verifyFilterByLineRange() {
        var filter = new Filter.ByLineRange(5, 8);
        var result = filterService.filer(List.of(filter));
        assertThat(result).extracting(LogEntry::id).containsExactly(5, 6, 7, 8);
    }

    @Test
    void verifyFilterByTimeRange() {
        var filter = new Filter.ByTimeRange("00:01:00", "00:02:57");
        var result = filterService.filer(List.of(filter));
        assertThat(result).extracting(LogEntry::id).containsExactly(4, 5, 6, 7);
    }

    @Test
    void verifyCombiFilterTwoValues() {
        List<Filter> filters = List.of(new Filter.ByLineRange(6, 18), new Filter.ByLogLevel(List.of(LogLevel.VERBOSE)));
        var result = filterService.filer(filters);
        assertThat(result).extracting(LogEntry::id).containsExactly(6, 8, 11, 12, 14, 18);
    }

    @Test
    void verifyCombiFilterThreeValues() {
        List<Filter> filters = List.of(new Filter.ByLineRange(6, 18), new Filter.ByLogLevel(List.of(LogLevel.VERBOSE)), new Filter.ByMessage("Some", true));
        var result = filterService.filer(filters);
        assertThat(result).extracting(LogEntry::id).containsExactly(6, 11, 14);
    }
}
