package md.vnastasi.slv.app.scene;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import md.vnastasi.slv.model.LogEntry;
import md.vnastasi.slv.model.LogLevel;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

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
    private CheckBox caseSensitiveKeywordCheckbox;

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

    private final MainSceneViewModel viewModel = new MainSceneViewModel();

    @FXML
    protected void initialize() {
        setupListView();
        setupRadioButtons();
        setupTextFields();
        bindProperties();
    }

    @FXML
    protected void onSelectLogFileClicked() {
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("LOG files", "*.log"));
        var file = fileChooser.showOpenDialog(selectLogFileButton.getScene().getWindow());
        viewModel.onLogFileUpload(file);
    }

    @FXML
    protected void onApplyFiltersClicked() {
        viewModel.onRefreshListView();
    }

    private void setupListView() {
        logItemListView.setItems(viewModel.observableLogEntryList);
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
        lineNumberPartitionButton.setToggleGroup(viewModel.partitionTypeToggleGroup);
        lineNumberPartitionButton.setUserData(MainSceneViewModel.SELECTED_PARTITION_LINE_NUMBER);
        timePartitionButton.setToggleGroup(viewModel.partitionTypeToggleGroup);
        timePartitionButton.setUserData(MainSceneViewModel.SELECTED_PARTITION_TIME);
    }

    private void setupTextFields() {
        UnaryOperator<TextFormatter.Change> numbersOnlyChangeOperator = change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        };
        lineNumberFromTextField.setTextFormatter(new TextFormatter<>(numbersOnlyChangeOperator));
        lineNumberToTextField.setTextFormatter(new TextFormatter<>(numbersOnlyChangeOperator));
    }

    private void bindProperties() {
        Bindings.bindBidirectional(selectLogFileButton.disableProperty(), viewModel.uploadFileDisabledProperty);
        Bindings.bindBidirectional(selectedLogFileLabel.textProperty(), viewModel.selectedFileTextProperty);
        Bindings.bindBidirectional(messageKeywordTextField.textProperty(), viewModel.messageKeywordTextProperty);
        Bindings.bindBidirectional(caseSensitiveKeywordCheckbox.selectedProperty(), viewModel.caseSensitiveToggleProperty);
        Bindings.bindBidirectional(verboseLevelCheckBox.selectedProperty(), viewModel.verboseLogLevelToggleProperty);
        Bindings.bindBidirectional(infoLevelCheckBox.selectedProperty(), viewModel.infoLogLevelToggleProperty);
        Bindings.bindBidirectional(debugLevelCheckBox.selectedProperty(), viewModel.debugLogLevelToggleProperty);
        Bindings.bindBidirectional(warnLevelCheckBox.selectedProperty(), viewModel.warnLogLevelToggleProperty);
        Bindings.bindBidirectional(errorLevelCheckBox.selectedProperty(), viewModel.errorLogLevelToggleProperty);
        Bindings.bindBidirectional(lineNumberFromTextField.textProperty(), viewModel.lineNumberFromTextProperty);
        Bindings.bindBidirectional(lineNumberToTextField.textProperty(), viewModel.lineNumberToTextProperty);
        Bindings.bindBidirectional(timeFromTextField.textProperty(), viewModel.timeFromTextProperty);
        Bindings.bindBidirectional(timeToTextField.textProperty(), viewModel.timeToTextProperty);
        Bindings.bindBidirectional(applyFilterButton.disableProperty(), viewModel.applyFiltersDisabledProperty);
    }

    private @NotNull Color getLogLevelColor(@NotNull LogLevel logLevel) {
        return switch (logLevel) {
            case ERROR -> Color.RED;
            case WARN -> Color.DARKORANGE;
            case DEBUG -> Color.DARKBLUE;
            case INFO -> Color.BLACK;
            case VERBOSE -> Color.DARKGREY;
        };
    }
}
