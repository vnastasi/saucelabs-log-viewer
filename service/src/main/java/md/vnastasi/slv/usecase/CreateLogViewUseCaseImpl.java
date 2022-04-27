package md.vnastasi.slv.usecase;

import md.vnastasi.slv.data.FilterSpec;
import md.vnastasi.slv.data.LogEntry;
import md.vnastasi.slv.data.RangeSpec;
import md.vnastasi.slv.filter.Filter;
import md.vnastasi.slv.filter.FilterService;
import md.vnastasi.slv.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CreateLogViewUseCaseImpl implements CreateLogViewUseCase {

    private final Storage storage;
    private final FilterService filterService;

    public CreateLogViewUseCaseImpl(Storage storage, FilterService filterService) {
        this.storage = storage;
        this.filterService = filterService;
    }

    @Override
    public List<String> execute(@NotNull FilterSpec filterSpec) {
        return filterService.filter(storage.getList(), createFilterList(filterSpec)).map(LogEntry::join).toList();
    }

    private List<Filter> createFilterList(@NotNull FilterSpec filterSpec) {
        var list = new ArrayList<Filter>();

        switch (filterSpec.range()) {
            case RangeSpec.Line lr :
                list.add(new Filter.ByLineRange(lr.start(), lr.end()));
                break;
            case RangeSpec.Time tr :
                list.add(new Filter.ByTimeRange(tr.start(), tr.end()));
                break;
            case null :
                break;
        }

        if (filterSpec.logLevels() != null && !filterSpec.logLevels().isEmpty()) {
            list.add(new Filter.ByLogLevel(filterSpec.logLevels()));
        }

        if (filterSpec.messageKeywordSpec() != null && filterSpec.messageKeywordSpec().keyword() != null && !filterSpec.messageKeywordSpec().keyword().isEmpty()) {
            list.add(new Filter.ByMessage(filterSpec.messageKeywordSpec().keyword(), filterSpec.messageKeywordSpec().caseSensitive()));
        }

        return list;
    }
}
