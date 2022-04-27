module slv.app {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.jetbrains.annotations;
    requires slv.service;

    exports md.vnastasi.slv.app;
    exports md.vnastasi.slv.app.scene;

    opens md.vnastasi.slv.app to javafx.fxml;
    opens md.vnastasi.slv.app.scene to javafx.fxml;
}
