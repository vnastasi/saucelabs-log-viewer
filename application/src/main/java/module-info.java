module md.vnastasi.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;

    opens md.vnastasi.application to javafx.fxml;

    exports md.vnastasi.application;
}