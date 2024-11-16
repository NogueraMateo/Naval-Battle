module com.example.navalbattle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jdk.compiler;


    opens com.example.navalbattle to javafx.fxml;
    exports com.example.navalbattle;
    exports com.example.navalbattle.controllers;
    opens com.example.navalbattle.controllers to javafx.fxml;
    exports com.example.navalbattle.views;
    opens com.example.navalbattle.views to javafx.fxml;
}