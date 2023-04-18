package com.example.lab4.controller;

import com.example.lab4.HelloApplication;
import com.example.lab4.domain.User;
import com.example.lab4.service.Service;
import com.example.lab4.service.ServiceDB;
import com.example.lab4.utils.Events.ChangeEvent;
import com.example.lab4.utils.Observer.Observable;
import com.example.lab4.utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;

public class UsersController implements Observer<ChangeEvent> {
    @FXML
    public TableColumn<User, String> tableColumnUserName;
    @FXML
    public TableColumn<User, String> tableColumnFirstName;
    @FXML
    public TableColumn<User, String> tableColumnLastName;
    @FXML
    public TableView<User> tableUser;
    @FXML
    public Button okButton;
    @FXML
    public Button addUser;

    ServiceDB service;

    private ObservableList<User> modelUser = FXCollections.observableArrayList();


    public void setService(ServiceDB service){
        this.service = service;
        service.addObserver(this);
        initModel();
    }
    public void handleUserMenu(ActionEvent actionEvent) {
        User user = tableUser.getSelectionModel().getSelectedItem();
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/user-view.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("User Menu");
            AccountController accountController = fxmlLoader.getController();
            accountController.setCurrentUser(user);
            accountController.setService(service);
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(ChangeEvent changeEvent) {
        tableUser.setItems(null);
        initModel();
        initialize();
    }

    private void initModel() {
        modelUser.setAll();
        modelUser.addAll(service.getUsersAsList());
    }

    @FXML
    private void initialize(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tableUser.setItems(modelUser);
    }

    public void handleAddUser(ActionEvent actionEvent) {
        try{

            URL fxmlLocation = HelloApplication.class.getResource("views/new-user-view.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            AddUser addUserController = fxmlLoader.getController();
            addUserController.setUserService(service);
            stage.show();
            Stage stage2 = (Stage) addUser.getScene().getWindow();
            stage2.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
