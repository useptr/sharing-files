module com.example.filesharing {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.filesharing to javafx.fxml;
    exports com.example.filesharing;
}