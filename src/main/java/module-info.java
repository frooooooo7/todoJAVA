module todoList {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens main to javafx.fxml;
    exports main;
    opens controllers to javafx.fxml;
    opens db to java.sql;
}