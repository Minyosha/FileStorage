module com.example.filestorage {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.filestorage to javafx.fxml;
    exports com.example.filestorage;
}