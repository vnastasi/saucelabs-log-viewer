package md.vnastasi.slv.usecase.impl;

import md.vnastasi.slv.model.LogLevel;
import md.vnastasi.slv.usecase.model.FilterSpec;
import md.vnastasi.slv.model.LogEntry;
import md.vnastasi.slv.usecase.model.MessageKeywordSpec;
import md.vnastasi.slv.usecase.model.RangeSpec;
import md.vnastasi.slv.filter.Filter;
import md.vnastasi.slv.filter.FilterService;
import md.vnastasi.slv.storage.Storage;
import md.vnastasi.slv.usecase.CreateLogViewUseCase;
import md.vnastasi.slv.util.CollectionUtils;
import md.vnastasi.slv.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
            case RangeSpec.LineNumber lr :
                list.add(new Filter.ByLineRange(lr.start(), lr.end()));
                break;
            case RangeSpec.Time tr :
                if (StringUtils.isNotEmpty(tr.start()) && StringUtils.isNotEmpty(tr.end())) {
                    list.add(new Filter.ByTimeRange(tr.start(), tr.end()));
                }
                break;
            case null :
                break;
        }

        Optional.ofNullable(filterSpec.logLevels())
                .filter(CollectionUtils::isNotEmpty)
                .map(it -> it.stream().map(LogLevel::valueOf).toList())
                .ifPresent(it -> list.add(new Filter.ByLogLevel(it)));

        Optional.ofNullable(filterSpec.messageKeywordSpec())
                .filter(it -> StringUtils.isNotEmpty(it.keyword()))
                .ifPresent(it -> list.add(new Filter.ByMessage(it.keyword(), it.caseSensitive())));

        return list;
    }
}
