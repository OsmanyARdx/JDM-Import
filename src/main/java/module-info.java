module com.osmanyargueta.jdmimport {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;

    opens com.osmanyargueta.jdmimport to javafx.fxml, javafx.media;
    exports com.osmanyargueta.jdmimport;
}
