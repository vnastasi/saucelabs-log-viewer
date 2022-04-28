package md.vnastasi.slv.app.scene;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import md.vnastasi.slv.model.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainSceneController {

    @FXML
    @NotNull
    private Button selectLogFileButton;

    @FXML
    @NotNull
    private Button applyFilterButton;

    @FXML
    @NotNull
    private Label selectedLogFileLabel;

    @FXML
    @NotNull
    private TextField messageKeywordTextField;

    @FXML
    @NotNull
    private CheckBox verboseLevelCheckBox;

    @FXML
    @NotNull
    private CheckBox infoLevelCheckBox;

    @FXML
    @NotNull
    private CheckBox debugLevelCheckBox;

    @FXML
    @NotNull
    private CheckBox warnLevelCheckBox;

    @FXML
    @NotNull
    private CheckBox errorLevelCheckBox;

    @FXML
    @NotNull
    private RadioButton lineNumberPartitionButton;

    @FXML
    @NotNull
    private RadioButton timePartitionButton;

    @FXML
    @NotNull
    private TextField lineNumberFromTextField;

    @FXML
    @NotNull
    private TextField lineNumberToTextField;

    @FXML
    @NotNull
    private TextField timeFromTextField;

    @FXML
    @NotNull
    private TextField timeToTextField;

    @FXML
    @NotNull
    private ListView<LogEntry> logItemListView;

    private final ObservableList<LogEntry> logItemObservableList = FXCollections.observableArrayList();
    private final ObservableFilterSpec observableFilterSpec = new ObservableFilterSpec();
    private final ToggleGroup partitionToggleGroup = new ToggleGroup();

    @FXML
    protected void initialize() {
        setupListView();
        setupRadioButtons();
        bindProperties();
    }

    @FXML
    protected void onSelectLogFileClicked() {
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("LOG files", "*.log"));
        var file = fileChooser.showOpenDialog(selectLogFileButton.getScene().getWindow());
        if (file != null) {
            uploadFile(file);
        } else {
            selectedLogFileLabel.setText("[Nothing selected]");
        }
    }

    @FXML
    protected void onApplyFiltersClicked() {
        refreshLogItemList();
    }

    private void setupListView() {
        logItemListView.setItems(logItemObservableList);
        logItemListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        logItemListView.setCellFactory(cell -> new ListCell<>() {
            @Override
            protected void updateItem(LogEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.join());
                    setFont(Font.font(14));
                    setTextFill(getLogLevelColor(item.level()));
                }
            }
        });
    }

    private void setupRadioButtons() {
        lineNumberPartitionButton.setToggleGroup(partitionToggleGroup);
        timePartitionButton.setToggleGroup(partitionToggleGroup);
    }

    private void bindProperties() {
        Bindings.bindBidirectional(messageKeywordTextField.textProperty(), observableFilterSpec.messageKeywordText);
        Bindings.bindBidirectional(verboseLevelCheckBox.selectedProperty(), observableFilterSpec.verboseLevelSelected);
        Bindings.bindBidirectional(infoLevelCheckBox.selectedProperty(), observableFilterSpec.infoLevelSelected);
        Bindings.bindBidirectional(debugLevelCheckBox.selectedProperty(), observableFilterSpec.debugLevelSelected);
        Bindings.bindBidirectional(warnLevelCheckBox.selectedProperty(), observableFilterSpec.warnLevelSelected);
        Bindings.bindBidirectional(errorLevelCheckBox.selectedProperty(), observableFilterSpec.errorLevelSelected);
        Bindings.bindBidirectional(lineNumberFromTextField.textProperty(), observableFilterSpec.lineNumberFromText);
        Bindings.bindBidirectional(lineNumberToTextField.textProperty(), observableFilterSpec.lineNumberToText);
        Bindings.bindBidirectional(timeFromTextField.textProperty(), observableFilterSpec.timeFromText);
        Bindings.bindBidirectional(timeToTextField.textProperty(), observableFilterSpec.timeToText);
    }

    private Color getLogLevelColor(LogLevel logLevel) {
        return switch (logLevel) {
            case ERROR -> Color.RED;
            case WARN -> Color.DARKORANGE;
            case DEBUG -> Color.DARKBLUE;
            case INFO -> Color.BLACK;
            case VERBOSE -> Color.DARKGREY;
        };
    }

    private void uploadFile(@NotNull File logFile) {
        var service = new LogFileUploadService(logFile);
        service.setOnSucceeded(event -> {
            selectedLogFileLabel.setText(logFile.getPath());
            applyFilterButton.setDisable(false);
            refreshLogItemList();
        });
        service.setOnFailed(event -> {
            event.getSource().getException().printStackTrace();
            selectedLogFileLabel.setText("[Error]");
        });
        service.start();
    }

    private void refreshLogItemList() {
        var service = new CreateLogItemsListService(observableFilterSpec.createFilterSpec());
        service.setOnSucceeded(event -> {
            @SuppressWarnings("unchecked") var list = (List<LogEntry>) event.getSource().getValue();
            logItemObservableList.setAll(list);
        });
        service.setOnFailed(event -> {
            event.getSource().getException().printStackTrace();
        });
        service.start();
    }

    private class ObservableFilterSpec {

        private final StringProperty messageKeywordText = new SimpleStringProperty();
        private final BooleanProperty verboseLevelSelected = new SimpleBooleanProperty(true);
        private final BooleanProperty infoLevelSelected = new SimpleBooleanProperty(true);
        private final BooleanProperty debugLevelSelected = new SimpleBooleanProperty(true);
        private final BooleanProperty warnLevelSelected = new SimpleBooleanProperty(true);
        private final BooleanProperty errorLevelSelected = new SimpleBooleanProperty(true);
        private final StringProperty lineNumberFromText = new SimpleStringProperty();
        private final StringProperty lineNumberToText = new SimpleStringProperty();
        private final StringProperty timeFromText = new SimpleStringProperty();
        private final StringProperty timeToText = new SimpleStringProperty();

        public FilterSpec createFilterSpec() {
            var logLevels = new ArrayList<String>();
            if (verboseLevelSelected.get()) {
                logLevels.add(LogLevel.VERBOSE.name());
            }
            if (infoLevelSelected.get()) {
                logLevels.add(LogLevel.INFO.name());
            }
            if (debugLevelSelected.get()) {
                logLevels.add(LogLevel.DEBUG.name());
            }
            if (warnLevelSelected.get()) {
                logLevels.add(LogLevel.WARN.name());
            }
            if (errorLevelSelected.get()) {
                logLevels.add(LogLevel.ERROR.name());
            }

            RangeSpec rangeSpec;
            if (partitionToggleGroup.getSelectedToggle() == lineNumberPartitionButton) {
                rangeSpec = new RangeSpec.LineNumber(Integer.parseInt(lineNumberFromText.get()), Integer.parseInt(lineNumberToText.get()));
            } else if (partitionToggleGroup.getSelectedToggle() == timePartitionButton) {
                rangeSpec = new RangeSpec.Time(timeFromText.get(), timeToText.get());
            } else {
                rangeSpec = null;
            }

            return new FilterSpec(rangeSpec, logLevels, new MessageKeywordSpec(messageKeywordText.get(), false));
        }
    }
}
