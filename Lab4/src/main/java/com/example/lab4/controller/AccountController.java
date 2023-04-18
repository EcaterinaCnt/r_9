package com.example.lab4.controller;

import com.example.lab4.HelloApplication;
import com.example.lab4.domain.FriendshipDB;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class AccountController implements Observer<ChangeEvent> {
    @FXML
    public TextField friendSearch;
    @FXML
    public Button deleteFriendButton;
    @FXML
    public TextField userSearch;
    @FXML
    public Button addFriendButton;
    @FXML
    public TableColumn<User, String> tableColumnUserLastName;
    @FXML
    public TableColumn<User, String> tableColumnUserFirstName;
    @FXML
    public TableColumn<User, String> tableColumnFirstName;
    @FXML
    public TableColumn<User, String> tableColumnLastName;
    @FXML
    public TableView<User> tableFriendView;
    @FXML
    public TableView<User> tableUserView;
    @FXML
    public Button returnButton;
    @FXML
    public TableColumn<FriendshipDB, Boolean> tableColumnDate;
    private User currentUser;

    ServiceDB service;

    private ObservableList<User> modelUser = FXCollections.observableArrayList();
    private ObservableList<User> modelFriends = FXCollections.observableArrayList();

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setService(ServiceDB service) {
        this.service = service;
        service.addObserver(this);
        initModel();
        initialize();
    }

    private void initModel() {
        modelFriends.setAll();

        modelFriends.addAll(service.getListOfFriends(currentUser));

        modelUser.setAll();
        modelUser.addAll(service.getUsersForAccount(currentUser));
    }



    @Override
    public void update(ChangeEvent changeEvent) {
        tableUserView.setItems(null);
        tableFriendView.setItems(null);
        initModel();
        initialize();
    }

    private void initialize(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableFriendView.setItems(modelFriends);

        tableColumnUserFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnUserLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableUserView.setItems(modelUser);
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        User user = tableFriendView.getSelectionModel().getSelectedItem();
        FriendshipDB friendshipToDelete = service.getFriendshipForUser(user.getUserName(), currentUser.getUserName());
        service.deleteFriendship(friendshipToDelete.getId());

    }

    public void handleAddFriend(ActionEvent actionEvent) {
        User user = tableUserView.getSelectionModel().getSelectedItem();
//        if(Objects.equals(currentUser.getId(), user.getId())){
//
//        }

        service.sendFriendRequest(currentUser.getUserName(), user.getUserName());
    }

    public void handleClickSearchUsers(MouseEvent mouseEvent) {
        userSearch.setText("");
    }

    public void handleClickSearchFriend(MouseEvent mouseEvent) {
        friendSearch.setText("");
    }

    public void handleSearchFriends(KeyEvent keyEvent) {
        modelFriends.setAll(service.getListOfFriends(currentUser));
        tableFriendView.setItems(modelFriends);
        String name = String.valueOf(friendSearch.getCharacters());
        if(!name.equals("")){
            List<User> friends = service.getListOfFriends(currentUser).stream().filter(x -> (x.getLastName()+x.getFirstName()).contains(name)).toList();
            modelFriends.setAll(friends);
            tableFriendView.setItems(modelFriends);
        }
        deleteFriendButton.setDisable(false);
    }

    public void handleSearchUsers(KeyEvent keyEvent) {
        modelUser.setAll(service.getUsersAsList());
        tableUserView.setItems(modelUser);
        String name = String.valueOf(userSearch.getCharacters());
        if(!name.equals("")){
            List<User> users = service.getUsersAsList().stream().filter(x -> (x.getLastName() + x.getFirstName()).contains(name)).toList();
            modelUser.setAll(users);
            tableUserView.setItems(modelUser);
        }
        addFriendButton.setDisable(true);
    }

    public void handleReturnButton(ActionEvent actionEvent) throws IOException {
        URL fxmlLocation = HelloApplication.class.getResource("views/users-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Page");
        UsersController usersController = fxmlLoader.getController();
        usersController.setService(service);
        stage.show();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();


    }
}
