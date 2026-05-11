package com.example.network.datas.users;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.User;
import com.google.gson.GsonBuilder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class UserCreate extends MyAsyncTask {
    User user;

    public UserCreate(User user, MyResponseCallback callback) {
        super(callback);
        this.user = user;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String rawData = new GsonBuilder().create().toJson(user);

        try {
            Connection.Response response = Jsoup.connect(Settings.URL + "/api/user/create")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .header("Content-type", "application/json")
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
