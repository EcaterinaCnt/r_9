package com.example.lab4.service;

import com.example.lab4.domain.FriendshipDB;
import com.example.lab4.domain.User;
import com.example.lab4.domain.validators.ValidationException;
import com.example.lab4.repository.Repository0;
import com.example.lab4.utils.Events.ChangeEvent;
import com.example.lab4.utils.Events.ChangeEventType;
import com.example.lab4.utils.Observer.Observable;
import com.example.lab4.utils.Observer.Observer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ServiceDB implements Observable<ChangeEvent> {
    private Repository0<UUID, User> userRepo;

    private Repository0<UUID, FriendshipDB> friendRepo;
    private List<Observer<ChangeEvent>> obs = new ArrayList<>();

    public ServiceDB(Repository0<UUID, User> userRepo, Repository0<UUID, FriendshipDB> friendRepo) {
        this.userRepo = userRepo;
        this.friendRepo = friendRepo;
    }

    public User addUser(User user){
        user.setId(UUID.randomUUID());
        try{
            return userRepo.save(user);

        }catch (ValidationException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public User findUser(UUID uuid){
        if(userRepo.findOne(uuid)!=null)
            return userRepo.findOne(uuid);
        return null;
    }

    public boolean deleteUser(UUID uuid){
        return userRepo.delete(uuid)!= null;
    }

    public Iterable<User> getAll(){
        return userRepo.findAll();
    }

    public LocalDate generateFriendsFrom(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String dateTimeString = now.format(formatter);

        return LocalDate.parse(dateTimeString, formatter);

    }

    public FriendshipDB deleteFriendshipDB(UUID uuid){
        FriendshipDB friendship = friendRepo.delete(uuid);
        FriendshipDB friend = null;
        if(friendship != null)
            friend = friendship;

        notifyObserver(new ChangeEvent(ChangeEventType.DELETE, friend));
        return friendRepo.delete(uuid);
    }

    public List<User> getUsersAsList(){
        Iterable<User> users = userRepo.findAll();
        List<User>  listOfUsers = new ArrayList<>();
        users.forEach(listOfUsers::add);
        return listOfUsers;
    }


    public List<User> getUsersForAccount(User currentUser){
        Iterable<User> users = userRepo.findAll();
        List<User>  listOfUsers = new ArrayList<>();
        List<User> friendsOfUser = getRequestsOfUsers(currentUser);
        List<FriendshipDB> allFriends = getFriendsAsList();

        List<FriendshipDB> desiredFriends = allFriends.stream().filter(x-> (x.getUtilizator1() + x.getUtilizator2()).equals(currentUser.getUserName())).toList();
        for(User u: users){
            if(!listOfUsers.contains(u) && !currentUser.equals(u) && !friendsOfUser.contains(u)){
                listOfUsers.add(u);
            }
        }
        return listOfUsers;

    }
    public List<FriendshipDB> getFriendsAsList(){
        Iterable<FriendshipDB> friends = friendRepo.findAll();
        List<FriendshipDB> listOfFriends = new ArrayList<>();
        friends.forEach(listOfFriends::add);
        return listOfFriends;
    }

    public FriendshipDB getFriendshipForUser(String userName1, String userName2){
        List<FriendshipDB> friendshipList = getFriendsAsList();
        FriendshipDB searchedFriendship = null;
        for(FriendshipDB f: friendshipList){
            if((f.getUtilizator1().equals(userName1) && f.getUtilizator2().equals(userName2))|| (f.getUtilizator2().equals(userName1) && f.getUtilizator1().equals(userName2)))
            {
                searchedFriendship = new FriendshipDB(f.getUtilizator1(), f.getUtilizator2(), f.getDate());
                searchedFriendship.setId(f.getId());

            }
        }
        return searchedFriendship;

    }

    public boolean sendFriendRequest(String userName1, String userName2){
        LocalDate requestFrom = generateFriendsFrom();
        boolean existsAlready = false;
        User u1 = findUserByUserName(userName1);
        User u2 = findUserByUserName(userName2);
        if(u1 == null || u2 == null)
            throw new NullPointerException("Users are null!");
        if(getFriendshipForUser(u1.getUserName(), u2.getUserName()) != null){
            FriendshipDB f = getFriendshipForUser(u1.getUserName(), u2.getUserName());
            f.setAccepted(true);
            friendRepo.update(f);
        }
        else{
            FriendshipDB friendship = new FriendshipDB(u1.getUserName(), u2.getUserName(), requestFrom);
            friendship.setAccepted(false);
            UUID id = UUID.randomUUID();
            friendship.setId(id);
            try{
                friendRepo.save(friendship);
                notifyObserver(new ChangeEvent(ChangeEventType.ADD, friendship));
                return true;
            }catch(ValidationException e){
                System.out.println(e.getMessage());
            }
        }

        return false;

    }

    public boolean deleteFriendship(UUID uuid){
        FriendshipDB friendship = friendRepo.delete(uuid);
        notifyObserver(new ChangeEvent(ChangeEventType.DELETE, friendship));
        return friendship != null;
    }

    public boolean updateFriendship(UUID id1, UUID id2, boolean accepted){
        User u1 = userRepo.findOne(id1);
        User u2 = userRepo.findOne(id2);
        FriendshipDB friendship = new FriendshipDB(u1.getUserName(), u2.getUserName(), generateFriendsFrom());
        UUID id = UUID.randomUUID();
        friendship.setId(id);
        friendship.setAccepted(accepted);
        try{
            friendRepo.update(friendship);
            notifyObserver(new ChangeEvent(ChangeEventType.UPDATE, friendship));
            return true;
        }catch (ValidationException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public User findUserByUserName(String userName){
        List<User> allU = getUsersAsList();
        for(User u: allU){
            if(u.getUserName().equals(userName))
                return u;
        }
        return null;
    }

    public List<User> getListOfFriends(User user){
        List<FriendshipDB> allF = getFriendsAsList();
        List<User> result = new ArrayList<>();

        for(FriendshipDB f: allF){
            User first = findUserByUserName(f.getUtilizator1());
            User second = findUserByUserName(f.getUtilizator2());

            if(Objects.equals(f.getUtilizator1(), user.getUserName()))
                result.add(second);
            else if(Objects.equals(f.getUtilizator2(), user.getUserName()))
                result.add(first);
            }
        return result;

    }

    public List<User> getRequestsOfUsers(User currentUser){
        List<FriendshipDB> allF = getFriendsAsList();
        List<User> rezultat = new ArrayList<>();

        for(FriendshipDB p: allF.stream().filter(x-> x.isAccepted()).toList())
        {
            User u1 = findUserByUserName(p.getUtilizator1());
            if(Objects.equals(p.getUtilizator2(), currentUser.getUserName()))
                rezultat.add(u1);
        }
        return rezultat;
    }

    public List<User> getUnacceptedReq(User currentUser){
        List<User> allU = getUsersAsList();
        List<User> friendsOfU = getListOfFriends(currentUser);
        List<User> result = new ArrayList<>();

        for(User u: allU)
            if(!friendsOfU.contains(u) || !result.contains(u) || !u.equals(currentUser))
                result.add(u);

        return result;
    }

    @Override
    public void addObserver(Observer<ChangeEvent> e) {

    }

    @Override
    public void removeObserver(Observer<ChangeEvent> e) {
        obs.remove(e);
    }

    @Override
    public void notifyObserver(ChangeEvent t) {
        obs.forEach(x->x.update(t));
    }
}
