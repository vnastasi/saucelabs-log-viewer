module saucelabs.log.viewer.app {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.jetbrains.annotations;
    requires saucelabs.log.viewer.logging;
    requires saucelabs.log.viewer.service;

    exports md.vnastasi.slv.app;
    exports md.vnastasi.slv.app.scene;

    opens md.vnastasi.slv.app.scene to javafx.fxml;
}
