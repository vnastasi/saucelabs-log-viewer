module slv.app {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires slv.service;

    opens md.vnastasi.slv.app to javafx.fxml;

    exports md.vnastasi.slv.app;
    exports md.vnastasi.slv.app.scene;

    opens md.vnastasi.slv.app.scene to javafx.fxml;
}