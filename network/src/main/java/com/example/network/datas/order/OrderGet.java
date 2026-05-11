package com.example.network.datas.order;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.google.gson.GsonBuilder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class OrderGet extends MyAsyncTask {
    Integer orderId;
    String token;

    public OrderGet(Integer orderId, String token, MyResponseCallback callback) {
        super(callback);
        this.orderId = orderId;
        this.token = token;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Connection.Response response = Jsoup.connect(Settings.URL + "/api/order/get/" + orderId)
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
