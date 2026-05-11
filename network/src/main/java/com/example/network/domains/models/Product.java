package com.example.network.domains.models;

public class Product {
    public Integer id;
    public String name;
    public String description;
    public Integer gender;
    public String expenditure;
    public Integer price;
    public Integer idUser;

    public Product(String name, String description, Integer gender, String expenditure, Integer price, String imageFile) {
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.expenditure = expenditure;
        this.price = price;
    }

    public Product(Integer id) {
        this.id = id;
    }
}
