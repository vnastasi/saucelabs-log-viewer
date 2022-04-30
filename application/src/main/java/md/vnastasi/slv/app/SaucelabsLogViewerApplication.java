package md.vnastasi.slv.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class SaucelabsLogViewerApplication extends Application {

    private static final double MIN_WINDOW_WIDTH = 1080;
    private static final double MIN_WINDOW_HEIGHT = 720;

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SaucelabsLogViewerApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);
        stage.setTitle("Saucelabs Log Viewer");
        stage.setMinWidth(MIN_WINDOW_WIDTH);
        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.getIcons().add(new Image(Objects.requireNonNull(SaucelabsLogViewerApplication.class.getResourceAsStream("/images/icon.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(@NotNull String[] args) {
        launch();
    }
}
