package com.example.lab4;

import com.example.lab4.controller.UsersController;
import com.example.lab4.domain.FriendshipDB;
import com.example.lab4.domain.User;
import com.example.lab4.domain.validators.FriendshipValidator;
import com.example.lab4.domain.validators.UserValidator;
import com.example.lab4.repository.Repository0;
import com.example.lab4.repository.database.FriendshipDBRepository;
import com.example.lab4.repository.database.UserDBRepository;
import com.example.lab4.repository.memory.InMemoryRepository0;
import com.example.lab4.service.Service0;
import com.example.lab4.service.ServiceDB;
import com.example.lab4.ui.UI0;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class HelloApplication extends Application {
    Repository0<UUID, User> userRepo;
    Repository0<UUID, FriendshipDB> friendRepo;

    ServiceDB service;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        String username = "postgres";
        String password = "ecaterinac1852";
        String url="jdbc:postgresql://localhost:5432/socialnetwork";
        userRepo = new UserDBRepository(url, username, password, new UserValidator());
        friendRepo = new FriendshipDBRepository(url, username, password, new FriendshipValidator());

        service = new ServiceDB(userRepo, friendRepo);

        loginView(stage);
        stage.show();
    }

    private void loginView(Stage stage) throws IOException{
        URL fxmlLocation = HelloApplication.class.getResource("views/users-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        AnchorPane Layout = fxmlLoader.load();
        stage.setScene(new Scene(Layout));
        stage.setTitle("Main Page");

        UsersController usersController = fxmlLoader.getController();
        usersController.setService(service);
    }

    public static void main(String[] args) {
       launch();
//
//        InMemoryRepository0<UUID, User> utilizatorRepo = new InMemoryRepository0<>(new UserValidator());
//        InMemoryRepository0<UUID, FriendshipDB> prietenieeRepo = new InMemoryRepository0<>(new FriendshipValidator());
//
//        Service0 service = new Service0(utilizatorRepo,prietenieeRepo);
//        UI0 ui = new UI0(service);
//
//        ui.runUI();
    }
}