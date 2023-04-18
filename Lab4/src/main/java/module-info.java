module com.example.lab4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.lab4 to javafx.fxml;
    exports com.example.lab4.controller;
    exports com.example.lab4.domain;
    exports com.example.lab4.repository;
    exports com.example.lab4.service;
    exports com.example.lab4;
    exports com.example.lab4.utils.Events;
    exports com.example.lab4.utils.Observer;
    opens com.example.lab4.controller to javafx.fxml;
}