package md.vnastasi.slv.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SaucelabsLogViewerApplication extends Application {

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SaucelabsLogViewerApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle("Saucelabs Log Viewer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(@NotNull String[] args) {
        launch();
    }
}