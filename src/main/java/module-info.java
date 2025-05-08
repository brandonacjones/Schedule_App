module com.project.schedule_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.project.schedule_app to javafx.fxml;
    exports com.project.schedule_app;
}