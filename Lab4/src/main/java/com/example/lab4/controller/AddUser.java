package com.example.lab4.controller;

import com.example.lab4.HelloApplication;
import com.example.lab4.domain.User;
import com.example.lab4.service.ServiceDB;
import com.example.lab4.utils.Events.ChangeEvent;
import com.example.lab4.utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

public class AddUser implements Observer<ChangeEvent> {
    @FXML
    public TextField userNameField;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public Text textResponse;
    @FXML
    public Button registerButton;

    ServiceDB service;

    private UsersController controller;

    private ObservableList<User> modelUser = FXCollections.observableArrayList();

    public void setController(UsersController controller) {
        this.controller = controller;
    }

    public void handleRegisterButton(ActionEvent actionEvent) {
        User u = new User(firstNameField.getText(), lastNameField.getText(), userNameField.getText());

        if(service.addUser(u)!= null){
            service.addUser(u);
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Information", "User added successfully!");
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/users-view.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            UsersController usersController = fxmlLoader.getController();
            usersController.setService(service);
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();


        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void setUserService(ServiceDB service){
        this.service = service;
    }

    @Override
    public void update(ChangeEvent changeEvent) {
        initModel();
        initialize();
    }

    private void initModel() {
        modelUser.setAll();
        modelUser.addAll(service.getUsersAsList());
    }

    private void initialize(){

    }
}
