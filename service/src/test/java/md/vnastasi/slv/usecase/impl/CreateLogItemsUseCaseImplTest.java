package md.vnastasi.slv.usecase.impl;

import md.vnastasi.slv.model.*;
import md.vnastasi.slv.filter.Filter;
import md.vnastasi.slv.filter.FilterService;
import md.vnastasi.slv.storage.Storage;
import md.vnastasi.slv.usecase.CreateLogItemsUseCase;
import md.vnastasi.slv.model.FilterSpec;
import md.vnastasi.slv.model.MessageKeywordSpec;
import md.vnastasi.slv.model.RangeSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateLogItemsUseCaseImplTest {

    private final Storage mockStorage = Mockito.mock(Storage.class);
    private final FilterService mockFilterService = Mockito.mock(FilterService.class);

    private final CreateLogItemsUseCase useCase = new CreateLogItemsUseCaseImpl(mockStorage, mockFilterService);

    @BeforeEach
    void setUp() {
        when(mockStorage.getList()).thenReturn(List.of());
        when(mockFilterService.filter(any(), any())).thenReturn(Stream.of());
    }

    @Test
    @SuppressWarnings("unchecked")
    void verifyNoFilters() {
        useCase.execute(new FilterSpec());

        ArgumentCaptor<List<Filter>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockFilterService).filter(any(), argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void verifyLineRangeFilter() {
        useCase.execute(new FilterSpec(new RangeSpec.LineNumber(0, 1)));

        ArgumentCaptor<List<Filter>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockFilterService).filter(any(), argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).containsExactly(new Filter.ByLineRange(0, 1));
    }

    @Test
    @SuppressWarnings("unchecked")
    void verifyTimeRangeFilter() {
        useCase.execute(new FilterSpec(new RangeSpec.Time("0", "1")));

        ArgumentCaptor<List<Filter>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockFilterService).filter(any(), argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).containsExactly(new Filter.ByTimeRange("0", "1"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void verifyMessageKeywordFilter() {
        useCase.execute(new FilterSpec(new MessageKeywordSpec("qwerty", true)));

        ArgumentCaptor<List<Filter>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockFilterService).filter(any(), argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).containsExactly(new Filter.ByMessage("qwerty", true));
    }

    @Test
    @SuppressWarnings("unchecked")
    void verifyLogLevelFilter() {
        useCase.execute(new FilterSpec(List.of(LogLevel.DEBUG)));

        ArgumentCaptor<List<Filter>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockFilterService).filter(any(), argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).containsExactly(new Filter.ByLogLevel(List.of(LogLevel.DEBUG)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void verifyCombiFilter() {
        var filterSpec = new FilterSpec(
                new RangeSpec.LineNumber(0, 1),
                List.of(LogLevel.DEBUG),
                new MessageKeywordSpec("qwerty", false)
        );
        useCase.execute(filterSpec);

        ArgumentCaptor<List<Filter>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockFilterService).filter(any(), argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).containsExactly(
                new Filter.ByLineRange(0, 1),
                new Filter.ByLogLevel(List.of(LogLevel.DEBUG)),
                new Filter.ByMessage("qwerty", false)
        );
    }

    @Test
    void verifyGeneratedLogLine() {
        var logEntry = new LogEntry(100000, "01:01:23", LogLevel.ERROR, "Message");
        when(mockStorage.getList()).thenReturn(List.of(logEntry));
        when(mockFilterService.filter(eq(List.of(logEntry)), any())).thenReturn(Stream.of(logEntry));

        var lines = useCase.execute(new FilterSpec());
        assertThat(lines).containsExactly(logEntry);
    }
}
