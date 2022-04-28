module slv.service {

    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;

    exports md.vnastasi.slv.di;
    exports md.vnastasi.slv.model;
    exports md.vnastasi.slv.usecase;

    opens md.vnastasi.slv.model to com.fasterxml.jackson.databind;
}
