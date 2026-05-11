package com.example.network.datas.product;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.URLEncoder;

public class ProductByUser extends MyAsyncTask {
    String token;
    public ProductByUser(String token, MyResponseCallback callback) {
        super(callback);
        this.token = token;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Connection.Response response = Jsoup.connect(Settings.URL + "/api/product/get_product_by_user")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.GET)
                    .header("Content-type", "application/json")
                    .header("token", token)
                    .execute();
            return response.statusCode() == 200 ?
                    response.body() :
                    "Error: " + response.body();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
