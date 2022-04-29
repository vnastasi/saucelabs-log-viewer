package md.vnastasi.slv.app.scene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import md.vnastasi.slv.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainSceneViewModel {

    public static final String SELECTED_PARTITION_LINE_NUMBER = "SELECTED_PARTITION_LINE_NUMBER";
    public static final String SELECTED_PARTITION_TIME = "SELECTED_PARTITION_TIME";

    public final ToggleGroup partitionTypeToggleGroup = new ToggleGroup();

    public final BooleanProperty caseSensitiveToggleProperty = new SimpleBooleanProperty(false);
    public final BooleanProperty verboseLogLevelToggleProperty = new SimpleBooleanProperty(true);
    public final BooleanProperty infoLogLevelToggleProperty = new SimpleBooleanProperty(true);
    public final BooleanProperty debugLogLevelToggleProperty = new SimpleBooleanProperty(true);
    public final BooleanProperty warnLogLevelToggleProperty = new SimpleBooleanProperty(true);
    public final BooleanProperty errorLogLevelToggleProperty = new SimpleBooleanProperty(true);
    public final BooleanProperty applyFiltersDisabledProperty = new SimpleBooleanProperty(true);
    public final BooleanProperty uploadFileDisabledProperty = new SimpleBooleanProperty(false);

    public final StringProperty selectedFileTextProperty = new SimpleStringProperty("[Nothing selected]");
    public final StringProperty messageKeywordTextProperty = new SimpleStringProperty();
    public final StringProperty lineNumberFromTextProperty = new SimpleStringProperty();
    public final StringProperty lineNumberToTextProperty = new SimpleStringProperty();
    public final StringProperty timeFromTextProperty = new SimpleStringProperty();
    public final StringProperty timeToTextProperty = new SimpleStringProperty();

    public final ObservableList<LogEntry> observableLogEntryList = FXCollections.observableArrayList();

    public void onLogFileUpload(@Nullable File file) {
        if (file != null) {
            doOnFileUploaded(file);
        } else {
            selectedFileTextProperty.set("[Nothing selected]");
        }
    }

    public void onRefreshListView() {
        applyFiltersDisabledProperty.set(true);
        var service = new CreateLogItemsListService(createFilterSpec());
        service.setOnSucceeded(event -> {
            @SuppressWarnings("unchecked") var list = (List<LogEntry>) event.getSource().getValue();
            observableLogEntryList.setAll(list);
            applyFiltersDisabledProperty.set(false);
        });
        service.setOnFailed(event -> {
            event.getSource().getException().printStackTrace();
            applyFiltersDisabledProperty.set(false);
        });
        service.start();
    }

    private void doOnFileUploaded(@NotNull File file) {
        uploadFileDisabledProperty.set(true);
        var service = new LogFileUploadService(file);
        service.setOnSucceeded(event -> {
            selectedFileTextProperty.set(file.getPath());
            uploadFileDisabledProperty.set(false);
            onRefreshListView();
        });
        service.setOnFailed(event -> {
            event.getSource().getException().printStackTrace();
            selectedFileTextProperty.set("[Error uploading file]");
            uploadFileDisabledProperty.set(false);
        });
        service.start();
    }

    private @NotNull FilterSpec createFilterSpec() {
        var rangeSpec = Optional.ofNullable(partitionTypeToggleGroup.getSelectedToggle())
                .map(Toggle::getUserData)
                .map(String.class::cast)
                .map(this::createRangeSpec)
                .orElse(null);

        var messageKeywordSpec = new MessageKeywordSpec(messageKeywordTextProperty.get(), caseSensitiveToggleProperty.get());

        return new FilterSpec(rangeSpec, createLogLevelList(), messageKeywordSpec);
    }

    private @Nullable RangeSpec createRangeSpec(@NotNull String selectedToggleName) {
        return switch (selectedToggleName) {
            case SELECTED_PARTITION_LINE_NUMBER -> new RangeSpec.LineNumber(
                    Integer.parseInt(lineNumberFromTextProperty.get()),
                    Integer.parseInt(lineNumberToTextProperty.get())
            );
            case SELECTED_PARTITION_TIME -> new RangeSpec.Time(
                    timeFromTextProperty.get(),
                    timeToTextProperty.get()
            );
            default -> null;
        };
    }

    private @NotNull List<LogLevel> createLogLevelList() {
        var logLevelList = new ArrayList<LogLevel>();
        if (verboseLogLevelToggleProperty.get()) {
            logLevelList.add(LogLevel.VERBOSE);
        }
        if (infoLogLevelToggleProperty.get()) {
            logLevelList.add(LogLevel.INFO);
        }
        if (debugLogLevelToggleProperty.get()) {
            logLevelList.add(LogLevel.DEBUG);
        }
        if (warnLogLevelToggleProperty.get()) {
            logLevelList.add(LogLevel.WARN);
        }
        if (errorLogLevelToggleProperty.get()) {
            logLevelList.add(LogLevel.ERROR);
        }

        return logLevelList;
    }
}
