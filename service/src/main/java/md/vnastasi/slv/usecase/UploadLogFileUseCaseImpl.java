package md.vnastasi.slv.usecase;

import md.vnastasi.slv.parser.LogFileParser;
import md.vnastasi.slv.storage.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class UploadLogFileUseCaseImpl implements UploadLogFileUseCase {

    private final Storage storage;
    private final LogFileParser parser;

    public UploadLogFileUseCaseImpl(@NotNull Storage storage, @NotNull LogFileParser parser) {
        this.storage = storage;
        this.parser = parser;
    }

    @Override
    public void execute(@Nullable File file) throws IOException {
        if (file == null) throw new IllegalArgumentException("File cannot be null");
        storage.setList(parser.parse(file.toPath()));
    }
}
