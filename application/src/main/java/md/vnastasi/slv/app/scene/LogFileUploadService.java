package md.vnastasi.slv.app.scene;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import md.vnastasi.slv.di.BeanFactory;
import md.vnastasi.slv.usecase.UploadLogFileUseCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

class LogFileUploadService extends Service<Void> {

    private final File logFile;
    private final UploadLogFileUseCase uploadLogFileUseCase;

    LogFileUploadService(@NotNull File logFile) {
        this.logFile = logFile;
        this.uploadLogFileUseCase = BeanFactory.getInstance().createUploadLogFileUseCase();
    }

    @Override
    protected @NotNull Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected @Nullable Void call() throws Exception {
                uploadLogFileUseCase.execute(logFile);
                return null;
            }
        };
    }
}
