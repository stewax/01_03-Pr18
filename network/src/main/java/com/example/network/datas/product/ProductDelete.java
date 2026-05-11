package com.example.network.datas.product;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;
import com.google.gson.GsonBuilder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class ProductDelete extends MyAsyncTask {
    String token;
    Integer id;

    public ProductDelete(String token, Integer id, MyResponseCallback callback) {
        super(callback);
        this.token = token;
        this.id = id;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String rawData = new GsonBuilder().create().toJson(id);

        try {
            Connection.Response response = Jsoup.connect(Settings.URL + "/api/product/delete/")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.DELETE)
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
