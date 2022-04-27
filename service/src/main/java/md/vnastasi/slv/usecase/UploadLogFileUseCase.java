package md.vnastasi.slv.usecase;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public interface UploadLogFileUseCase {

    void execute(@Nullable File file) throws IOException;
}
