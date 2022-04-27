package md.vnastasi.slv.app.scene;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import md.vnastasi.slv.di.BeanFactory;
import md.vnastasi.slv.usecase.CreateLogItemsUseCase;
import md.vnastasi.slv.usecase.model.FilterSpec;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class CreateLogItemsListService extends Service<List<String>> {

    private final CreateLogItemsUseCase createLogItemsUseCase;
    private final FilterSpec filterSpec;

    CreateLogItemsListService() {
        this.createLogItemsUseCase = BeanFactory.getInstance().createLogViewUseCase();
        this.filterSpec = new FilterSpec(List.of("ERROR"));
    }

    @Override
    protected @NotNull Task<List<String>> createTask() {
        return new Task<>() {
            @Override
            protected @NotNull List<String> call() {
                return createLogItemsUseCase.execute(filterSpec);
            }
        };
    }
}
