package com.example.lab4.ui;


import com.example.lab4.domain.FriendshipDB;
import com.example.lab4.domain.User;
import com.example.lab4.service.Service0;

import java.util.Scanner;

public class UI0 implements UI{
    private Service0 s;
    private Scanner cin;
    public UI0(Service0 service){
        this.s =service;
        cin = new Scanner(System.in);
    }

    private void showmenu(){
        System.out.println("1. Adauga un utilizator.");
        System.out.println("2. Sterge un utilizator. ");
        System.out.println("3. Afiseaza toti utilizatorii.");
        System.out.println("4. Adauga o prietenie.");
        System.out.println("5. Sterge o prietenie.");
        System.out.println("6. Afiseaza toate prieteniile existente.");
        System.out.println("0. Exit");
    }

    private void in_memory(){
        User u1 = new User("Ecaterina", "Constantin", "eca");
        User u2 = new User("Ana", "Manea", "anaa.m");
        User u3 = new User("Radu", "Costache", "RaduCt");

        this.s.addUtilizator(u1);
        this.s.addUtilizator(u2);
        this.s.addUtilizator(u3);


        this.s.createPrietenie(u1.getUserName(), u2.getUserName());
        this.s.createPrietenie(u1.getUserName(), u3.getUserName());

    }

    public void runUI(){
        in_memory();
        cin = new Scanner(System.in);
        while(true){

            showmenu();
            System.out.println("Introduceti optiunea dorita: ");
            int opt = cin.nextInt();
        try{
                switch (opt) {
                    case 0:
                        cin.close();
                        return;

                    case 1:
                        User u = citireutilizator();
                        s.addUtilizator(u);
                        break;

                    case 2:
                        String username = citireausername();
                        s.deleteUtilizator(username);
                        break;

                    case 3:
                        Iterable<User> users = s.getAllUtilizatori();
                        users.forEach(System.out::println);
                        break;

                    case 4:
                        String username1 = citireausername();
                        String username2 = citireausername();
                        s.createPrietenie(username1, username2);
                        break;

                    case 5:
                        username1 = citireausername();
                        username2 = citireausername();
                        s.deletePrietenie(username1, username2);
                        break;

                    case 6:
                        Iterable<FriendshipDB> itf = s.getAllPrietenii();
                        itf.forEach(System.out::println);
                        break;

                    default:
                        System.out.println("Optiune incorecta!");

                }
            }catch(Exception e) {System.err.println(e);}

        }
    }

    public User citireutilizator() {
        System.out.println("Introduceti prenumele utilizatorului: ");
        String firstname = cin.next();
        System.out.println("Introduceti numele utilizatorului: ");
        String lastname = cin.next();
        System.out.println("Introduceti username-ul utilizatorului: ");
        String username = cin.next();
        User user = new User(firstname, lastname, username);
        return user;
    }

    public String citireausername() {
        System.out.print("Introduceti username-ul dorit: ");
        String username = cin.next();
        return username;
    }
}

