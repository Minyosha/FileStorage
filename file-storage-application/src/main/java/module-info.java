module filestorage.filestorageapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens filestorage.application to javafx.fxml;
    exports filestorage.application;
}