module org.cryptic.fileencryptordecryptortool {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    requires org.apache.commons.lang3;
    requires jakarta.xml.bind;
    requires org.glassfish.jaxb.runtime;

    opens org.cryptic.fileencryptordecryptortool to javafx.fxml, jakarta.xml.bind;
    exports org.cryptic.fileencryptordecryptortool;
    exports org.cryptic.fileencryptordecryptortool.ui.controls.buttons;
    opens org.cryptic.fileencryptordecryptortool.ui.controls.buttons to javafx.fxml;
    exports org.cryptic.fileencryptordecryptortool.utils;
    opens org.cryptic.fileencryptordecryptortool.utils to jakarta.xml.bind, javafx.fxml;
    exports org.cryptic.fileencryptordecryptortool.model;
    opens org.cryptic.fileencryptordecryptortool.model to jakarta.xml.bind, javafx.fxml;
    exports org.cryptic.fileencryptordecryptortool.controller;
    opens org.cryptic.fileencryptordecryptortool.controller to jakarta.xml.bind, javafx.fxml;
}