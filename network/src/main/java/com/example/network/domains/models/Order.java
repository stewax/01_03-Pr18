package com.example.network.domains.models;

import java.util.ArrayList;

public class Order {
    public Integer id;
    public Integer idUser;
    public Integer status;
    public ArrayList<ProductBasket> products;
}
