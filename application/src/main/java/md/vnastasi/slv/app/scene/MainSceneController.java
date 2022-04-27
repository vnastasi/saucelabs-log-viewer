package md.vnastasi.slv.app.scene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainSceneController {

    @FXML
    private Label testLabel;

    @FXML
    protected void onTestButtonClicked() {
        testLabel.setText("Welcome to Saucelabs Log Viewer!");
    }
}