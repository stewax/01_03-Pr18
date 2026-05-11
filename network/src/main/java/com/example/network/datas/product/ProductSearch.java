package com.example.network.datas.product;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.URLEncoder;

public class ProductSearch extends MyAsyncTask {
    String searchText;
    public ProductSearch(String searchText, MyResponseCallback callback) {
        super(callback);
        this.searchText = searchText;
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {
            String encodedSearch = URLEncoder.encode(searchText, "UTF-8");

            Connection.Response response = Jsoup.connect(Settings.URL + "/api/product/search?search=" + encodedSearch)
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
