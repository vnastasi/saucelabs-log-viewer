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

    private static final System.Logger logger = System.getLogger(MainSceneViewModel.class.getName());

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
            logger.log(System.Logger.Level.WARNING, "Selected log file is null");
            selectedFileTextProperty.set("[Nothing selected]");
        }
    }

    public void onRefreshListView() {
        applyFiltersDisabledProperty.set(true);
        var filterSpec = createFilterSpec();
        logger.log(System.Logger.Level.INFO, "Filtering log entries using following filters: {0}", filterSpec);
        var service = new CreateLogItemsListService(filterSpec);
        service.setOnSucceeded(event -> {
            @SuppressWarnings("unchecked") var list = (List<LogEntry>) event.getSource().getValue();
            observableLogEntryList.setAll(list);
            applyFiltersDisabledProperty.set(false);
            logger.log(System.Logger.Level.INFO, "Filtered {0} log entries", list.size());
        });
        service.setOnFailed(event -> {
            applyFiltersDisabledProperty.set(false);
            logger.log(System.Logger.Level.ERROR, "Exception filtering log entries", event.getSource().getException());
        });
        service.start();
    }

    private void doOnFileUploaded(@NotNull File file) {
        uploadFileDisabledProperty.set(true);
        logger.log(System.Logger.Level.INFO, "Uploading log file {0}", file.getPath());
        var service = new LogFileUploadService(file);
        service.setOnSucceeded(event -> {
            selectedFileTextProperty.set(file.getPath());
            uploadFileDisabledProperty.set(false);
            logger.log(System.Logger.Level.INFO, "Log file {0} uploaded successfully", file.getPath());
            onRefreshListView();
        });
        service.setOnFailed(event -> {
            selectedFileTextProperty.set("[Error uploading file]");
            uploadFileDisabledProperty.set(false);
            logger.log(System.Logger.Level.ERROR, "Exception uploading log file", event.getSource().getException());
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
                    getLineNumberValue(lineNumberFromTextProperty, Integer.MIN_VALUE),
                    getLineNumberValue(lineNumberToTextProperty, Integer.MAX_VALUE)
            );
            case SELECTED_PARTITION_TIME -> new RangeSpec.Time(
                    getTimeValue(timeFromTextProperty, "00:00:00"),
                    getTimeValue(timeToTextProperty, "23:59:59")
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

    private int getLineNumberValue(@NotNull StringProperty property, int defaultValue) {
        return Optional.of(property)
                .map(StringProperty::get)
                .filter(it -> !it.isEmpty())
                .map(Integer::parseInt)
                .orElse(defaultValue);
    }

    private @NotNull String getTimeValue(@NotNull StringProperty property, String defaultValue) {
        return Optional.of(property)
                .map(StringProperty::get)
                .filter(it -> !it.isEmpty())
                .orElse(defaultValue);
    }
}
