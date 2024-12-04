module bsi.sistemasdistribuidos.sistemadeestagio {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.mail;
    requires mysql.connector.j;
    requires java.sql;

    opens sistemadeestagio to javafx.fxml;
    exports sistemadeestagio;
    exports sistemadeestagio.Model;
}