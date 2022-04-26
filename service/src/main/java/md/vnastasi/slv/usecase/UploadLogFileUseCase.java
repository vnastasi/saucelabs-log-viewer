package md.vnastasi.slv.usecase;

import java.io.File;
import java.io.IOException;

public interface UploadLogFileUseCase {

    void execute(File file) throws IOException;
}
