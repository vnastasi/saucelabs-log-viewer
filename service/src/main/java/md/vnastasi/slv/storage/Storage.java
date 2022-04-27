package md.vnastasi.slv.storage;

import md.vnastasi.slv.data.LogEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Storage {

    private List<LogEntry> list = List.of();

    private Storage() {
    }

    public @NotNull List<LogEntry> getList() {
        return list;
    }

    public synchronized void setList(@NotNull List<LogEntry> list) {
        this.list = list;
    }

    private static class StorageInstance {

        private static final Storage _instance = new Storage();
    }

    public static @NotNull Storage getInstance() {
        return StorageInstance._instance;
    }
}
