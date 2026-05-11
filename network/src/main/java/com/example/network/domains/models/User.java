package com.example.network.domains.models;

public class User {

    public Integer id;
    public String email;
    public String password;
    public String firstname;
    public String lastname;
    public String surname;
    public Integer gender;
    public String token;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String firstname, String lastname, String surname, Integer gender) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.surname = surname;
        this.gender = gender;
    }
}
