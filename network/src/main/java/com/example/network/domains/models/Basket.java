package com.example.network.domains.models;

public class Basket {
    public Integer count;
    public Integer idProduct;

    public Basket(Integer idProduct, Integer count) {
        this.count = count;
        this.idProduct = idProduct;
    }
}
