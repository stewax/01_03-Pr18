package com.example.network.datas.product;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;
import com.google.gson.GsonBuilder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class ProductCreate extends MyAsyncTask {
    String token;
    Product product;

    public ProductCreate(String token, Product product, MyResponseCallback callback) {
        super(callback);
        this.token = token;
        this.product = product;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String rawData = new GsonBuilder().create().toJson(product);

        try {
            Connection.Response response = Jsoup.connect(Settings.URL + "/api/product/create")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .header("Content-type", "application/json")
                    .header("token", token)
                    .requestBody(rawData)
                    .execute();
            return response.statusCode() == 200 ?
                    response.body() :
                    "Error: " + response.body();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
