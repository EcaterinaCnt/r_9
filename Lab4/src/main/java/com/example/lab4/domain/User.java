package com.example.lab4.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class User extends Entity<UUID>{
    private String firstName;
    private String lastName;
    private String userName;
    private Map<UUID, User> friends; //dictionar cu ID-urile prieteniei unui utiliztaor, si utilizatorul

    public User(String firstName, String lastName, String userName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        friends = new HashMap<>();
        this.setId(UUID.randomUUID());
    }

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getUserName() {return userName;}

    public void setusername(String username) {this.userName = username;}

    public Iterable<User> getFriends() {return friends.values();}

    @Override
    public String toString() {
        return "Utilizatorul: " +
                "\nPrenumele: " + firstName  +
                ",\nNumele: " + lastName  +
                ",\nusername-ul: " + userName +" "+ '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = new User(((User) o).getFirstName(), ((User) o).getLastName(), ((User) o).getUserName());
        that.setId(((User) o).getId());
        return ((User) o).getUserName().equals(userName) && ((User) o).getFirstName().equals(firstName) && ((User) o).getLastName().equals(lastName);
    }

    @Override
    public int hashCode() {return Objects.hash(getFirstName(), getLastName(), getUserName());}

    public void addFriend(User u) {friends.put(u.id, u);}

    public boolean removeFriend(User u) {
        return friends.remove(u.id) != null;
    }

}