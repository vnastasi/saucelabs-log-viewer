package md.vnastasi.slv.app.scene;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import md.vnastasi.slv.di.BeanFactory;
import md.vnastasi.slv.model.LogEntry;
import md.vnastasi.slv.usecase.CreateLogItemsUseCase;
import md.vnastasi.slv.model.FilterSpec;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class CreateLogItemsListService extends Service<List<LogEntry>> {

    private final CreateLogItemsUseCase createLogItemsUseCase;
    private final FilterSpec filterSpec;

    CreateLogItemsListService() {
        this.createLogItemsUseCase = BeanFactory.getInstance().createLogViewUseCase();
        this.filterSpec = new FilterSpec();
    }

    @Override
    protected @NotNull Task<List<LogEntry>> createTask() {
        return new Task<>() {
            @Override
            protected @NotNull List<LogEntry> call() {
                return createLogItemsUseCase.execute(filterSpec);
            }
        };
    }
}
