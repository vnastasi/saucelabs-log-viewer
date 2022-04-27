package md.vnastasi.slv.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import md.vnastasi.slv.filter.FilterService;
import md.vnastasi.slv.filter.FilterServiceImpl;
import md.vnastasi.slv.parser.LogFileParser;
import md.vnastasi.slv.parser.LogFileParserImpl;
import md.vnastasi.slv.storage.Storage;
import md.vnastasi.slv.usecase.CreateLogViewUseCase;
import md.vnastasi.slv.usecase.UploadLogFileUseCase;
import md.vnastasi.slv.usecase.impl.CreateLogViewUseCaseImpl;
import md.vnastasi.slv.usecase.impl.UploadLogFileUseCaseImpl;

public class BeanFactory {

    public static BeanFactory getInstance() {
        return BeanFactoryInstance._instance;
    }

    public UploadLogFileUseCase createUploadLogFileUseCase() {
        return new UploadLogFileUseCaseImpl(Storage.getInstance(), createLogFileParser());
    }

    public CreateLogViewUseCase createLogViewUseCase() {
        return new CreateLogViewUseCaseImpl(Storage.getInstance(), createFilterService());
    }

    private LogFileParser createLogFileParser() {
        return new LogFileParserImpl(createObjectMapper());
    }

    private ObjectMapper createObjectMapper() {
        return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    private FilterService createFilterService() {
        return new FilterServiceImpl();
    }

    private static class BeanFactoryInstance {
        public static BeanFactory _instance = new BeanFactory();
    }
}
