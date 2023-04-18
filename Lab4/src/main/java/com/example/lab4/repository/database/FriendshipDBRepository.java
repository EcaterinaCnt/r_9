package com.example.lab4.repository.database;

import com.example.lab4.domain.FriendshipDB;
import com.example.lab4.domain.FriendshipDB;
import com.example.lab4.domain.validators.ValidationException;
import com.example.lab4.domain.validators.Validator;
import com.example.lab4.repository.Repository0;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FriendshipDBRepository implements Repository0<UUID, FriendshipDB> {
    private String URL;

    private String username;

    private String password;

    private Validator<FriendshipDB> validator;

    public FriendshipDBRepository(String URL, String username, String password, Validator<FriendshipDB> validator) {
        this.URL = URL;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }



    @Override
    public FriendshipDB findOne(UUID uuid) {
            FriendshipDB friendship;
            String sql = "select * from friendship where id = ?";

            try(Connection connection = DriverManager.getConnection(URL, username, password);
                PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1, String.valueOf(uuid));
                ResultSet resultSet = ps.executeQuery();

                friendship = extractFriendship(resultSet);
                return friendship;
            }   catch(SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;


        }

    private FriendshipDB extractFriendship(ResultSet resultSet) throws SQLException{
        FriendshipDB friendship;
        if (resultSet.next()) {
            String firstUser = resultSet.getString("first_user");
            String secondUser = resultSet.getString("second_user");
            LocalDate date = resultSet.getTimestamp("date").toLocalDateTime().toLocalDate();
            boolean acceptat = resultSet.getBoolean("acceptat");

            friendship = new FriendshipDB(firstUser, secondUser, date);
            friendship.setAccepted(acceptat);

            return friendship;
        }
        return null;
    }

    @Override
    public Iterable<FriendshipDB> findAll() {
        Set<FriendshipDB> friends = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String firstUser = resultSet.getString("first_user");
                String secondUser = resultSet.getString("second_user");
                LocalDate date = resultSet.getTimestamp("date").toLocalDateTime().toLocalDate();
                boolean accepted = resultSet.getBoolean("acceptat");

                FriendshipDB friendship = new FriendshipDB(firstUser, secondUser, date);
                friendship.setId(UUID.fromString(id));
                friends.add(friendship);
                friendship.setAccepted(accepted);
            }
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    @Override
    public FriendshipDB save(FriendshipDB entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date1 = entity.getDate().format(formatter);

        if(findOne(entity.getId())!= null){
            throw new ValidationException("Duplicate ID!");
        }
        validator.validate(entity);
        String sql = "insert into friendship(id, first_user, second_user, date, acceptat) values(?, ?, ?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(URL, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, String.valueOf(entity.getId()));
            ps.setString(2, entity.getUtilizator1());
            ps.setString(3, entity.getUtilizator2());
            ps.setDate(4, Date.valueOf(entity.getDate()));
            ps.setBoolean(5, entity.isAccepted());
            ps.executeUpdate();
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendshipDB delete(UUID uuid) {
        FriendshipDB friendship = this.findOne(uuid);
        if(friendship!= null){
            String sql = "delete from friendship where first_user = ? and second_user =?";
            try(Connection connection = DriverManager.getConnection(URL, username, password);
                PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1, friendship.getUtilizator1());
                ps.setString(2, friendship.getUtilizator2());
                ps.executeUpdate();
            } catch(SQLException throwables) {
                throwables.printStackTrace();
            }

        }return friendship;

    }

    @Override
    public FriendshipDB update(FriendshipDB entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null!");
        validator.validate(entity);
        String sql = "update friendship set acceptat = ? where id = ?";
        int rowsNo = 0;

        try(Connection connection = DriverManager.getConnection(URL, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBoolean(1, entity.isAccepted());
            ps.setString(2, String.valueOf(entity.getId()));

            rowsNo = ps.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
        if(rowsNo > 0)
            return null;
        return entity;

    }
}
