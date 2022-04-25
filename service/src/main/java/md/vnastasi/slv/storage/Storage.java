package md.vnastasi.slv.storage;

import md.vnastasi.slv.data.LogEntry;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Storage {

    private List<LogEntry> list = List.of();

    private Storage() {
    }

    @NotNull
    public List<LogEntry> getList() {
        return list;
    }

    public void setList(@NotNull List<LogEntry> list) {
        this.list = list;
    }

    private static class StorageInstance {

        private static final Storage _instance = new Storage();
    }

    public static Storage getInstance() {
        return StorageInstance._instance;
    }
}
