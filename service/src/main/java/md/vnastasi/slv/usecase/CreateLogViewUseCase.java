package md.vnastasi.slv.usecase;

import md.vnastasi.slv.usecase.model.FilterSpec;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CreateLogViewUseCase {

    List<String> execute(@NotNull FilterSpec filterSpec);
}
