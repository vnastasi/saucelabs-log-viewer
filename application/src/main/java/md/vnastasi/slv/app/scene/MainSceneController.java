package md.vnastasi.slv.app.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class MainSceneController {

    @FXML
    @NotNull
    private Button selectLogFileButton;

    @FXML
    @NotNull
    private Label selectedLogFileLabel;

    @FXML
    @NotNull
    private ListView<String> logItemListView;

    private final ObservableList<String> logItemObservableList = FXCollections.observableArrayList();

    @FXML
    protected void initialize() {
        setupListView();
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

    private void setupListView() {
        logItemListView.setItems(logItemObservableList);
        logItemListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        logItemListView.setCellFactory(cell -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setFont(Font.font(14));
                }
            }
        });
    }

    private void uploadFile(@NotNull File logFile) {
        var service = new LogFileUploadService(logFile);
        service.setOnSucceeded(event -> {
            selectedLogFileLabel.setText(logFile.getPath());
            refreshLogItemList();
        });
        service.setOnFailed(event -> {
            event.getSource().getException().printStackTrace();
            selectedLogFileLabel.setText("[Error]");
        });
        service.start();
    }

    private void refreshLogItemList() {
        var service = new CreateLogItemsListService();
        service.setOnSucceeded(event -> {
            @SuppressWarnings("unchecked") var list = (List<String>) event.getSource().getValue();
            logItemObservableList.setAll(list);
        });
        service.setOnFailed(event -> {
            event.getSource().getException().printStackTrace();
            logItemObservableList.setAll(List.of("Error"));
        });
        service.start();
    }
}