module com.example.batallamarcos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.batallamarcos to javafx.fxml;
    exports com.example.batallamarcos;
}