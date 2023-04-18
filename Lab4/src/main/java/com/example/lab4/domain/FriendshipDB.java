package com.example.lab4.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class FriendshipDB extends Entity<UUID>{


    private String user1;
    private String user2;
    private LocalDate date;
    private boolean accepted;

    public FriendshipDB(String user1, String user2, LocalDate date) {
        this.user1 = user1;
        this.user2 = user2;
        this.date = date;
        this.accepted = false;
        this.setId(UUID.randomUUID());
    }
//Prietenia va contine cei doi utilizatori(clasele intregi)
    public String getUtilizator1() {
        return user1;
    }   //getter pentru u1

    public String getUtilizator2() {
        return user2;
    } // getter pentru u2

    public void setUtilizator1(String user1) {
        this.user1 = user1;
    }  //setter pentru u1

    public void setUtilizator2(String user2) {
        this.user2 = user2;
    }  //setter pentru u2

    @Override
    public String toString() {
        return "\nPrietenie: \n" +
                "Utilizatorul = " + user1 +';'+

                ",\nUtilizatorul = " + user2 +
                ';' ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipDB friendship = (FriendshipDB) o;
        return Objects.equals(user1, friendship.user1) && Objects.equals(user2, friendship.user2);
    }  //operator de egalitate

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public LocalDate getDate() {
        return date;
    }
}
