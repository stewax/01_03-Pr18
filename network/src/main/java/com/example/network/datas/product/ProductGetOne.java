package com.example.network.datas.product;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class ProductGetOne extends MyAsyncTask {
    Integer id;
    public ProductGetOne(Integer id, MyResponseCallback callback) {
        super(callback);
        this.id = id;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Connection.Response response = Jsoup.connect(Settings.URL + "/api/product/" + id.toString())
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.GET)
                    .header("Content-type", "application/json")
                    .execute();
            return response.statusCode() == 200 ?
                    response.body() :
                    "Error: " + response.body();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
