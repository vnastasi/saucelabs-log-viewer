module slv.service {

    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;

    exports md.vnastasi.slv.di;
    exports md.vnastasi.slv.usecase;
    exports md.vnastasi.slv.usecase.model;

    opens md.vnastasi.slv.model to com.fasterxml.jackson.databind;
}
