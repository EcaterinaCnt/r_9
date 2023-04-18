package com.example.lab4.repository.database;

import com.example.lab4.domain.User;
import com.example.lab4.domain.validators.Validator;
import com.example.lab4.repository.Repository0;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import java.sql.*;

public class UserDBRepository implements Repository0<UUID, User> {
    private String URL;

    private String username;

    private String password;

    private Validator<User> validator;

    public UserDBRepository(String URL, String username, String password, Validator<User> validator) {
        this.URL = URL;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }



    @Override
    public User findOne(UUID uuid) {
        String sql = "select * from users where id = ?";

        try(Connection connection = DriverManager.getConnection(URL, username, password);
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, String.valueOf(uuid));
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String userName = resultSet.getString("user_name");

                User user = new User(firstName, lastName, userName);
                user.setId(uuid);
                return user;

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(URL, username, password);
        PreparedStatement ps =connection.prepareStatement("select * from users");
        ResultSet resultSet = ps.executeQuery()){
            while(resultSet.next()){
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String userName = resultSet.getString("user_name");
                User user = new User(firstName, lastName, userName);
                users.add(user);
            }
            return users;

        }catch(SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        String sql = "insert into users(id, first_name, last_name, user_name) values(?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(URL, username, password);
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, String.valueOf(entity.getId()));
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setString(4, entity.getUserName());

            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(UUID uuid) {
        String s = "delete from users where id = ?";
        String s2 = "delete from friendship where id1 = ? or id2 = ?";
        User user = null;
        try(Connection connection = DriverManager.getConnection(URL, username, password)){
            PreparedStatement ps = connection.prepareStatement(s);
            PreparedStatement ps2 = connection.prepareStatement(s2);
            user = findOne(uuid);
            if(user == null)
                return null;
            ps2.setString(1, String.valueOf(uuid));
            ps2.setString(2, String.valueOf(uuid));
            ps2.executeUpdate();

            ps.setString(1, String.valueOf(uuid));
            ps.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User update(User entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null");
        validator.validate(entity);

        String sql = "update users set first_name = ?, last_name = ?, user_name = ? where id = ?";
        int  rowsNo = 0;

        try(Connection connection = DriverManager.getConnection(URL, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getUserName());
            ps.setString(4, String.valueOf(entity.getId()));

            rowsNo = ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(rowsNo > 0)
            return null;
        return entity;
    }
}
