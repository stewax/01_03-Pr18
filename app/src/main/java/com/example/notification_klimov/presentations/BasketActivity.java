package com.example.notification_klimov.presentations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.network.datas.basket.BasketDelete;
import com.example.network.datas.basket.BasketGet;
import com.example.network.datas.basket.BasketUpdate;
import com.example.network.datas.order.OrderCreate;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.models.Basket;
import com.example.network.domains.models.Order;
import com.example.network.domains.models.ProductBasket;
import com.example.notification_klimov.R;
import com.example.notification_klimov.domains.PermissionManager;
import com.example.notification_klimov.infrastructure.OrderService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class BasketActivity extends AppCompatActivity {
    public static String TOKEN = "";
    ArrayList<ProductBasket> ProductsBasket = new ArrayList<>();
    LinearLayout llItems;
    TextView tvAllSum;
    View btnBasketDelete;
    View btnOrderCreate;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_basket);

        llItems = findViewById(R.id.llItems);
        tvAllSum = findViewById(R.id.tvAllSum);
        btnBasketDelete = findViewById(R.id.btnBasketDelete);
        btnOrderCreate = findViewById(R.id.btnOrderCreate);

        context = this;

        PermissionManager.PermissionNotification(context, this);

        btnBasketDelete.setOnClickListener(v -> {
            BasketDelete RequestBasketDelete = new BasketDelete(
                    TOKEN,
                    new MyResponseCallback() {
                        @Override
                        public void onCompile(String result) {
                            Log.d("BASKET DELETE", result);
                            onBasketGet();
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("BASKET DELETE", error);
                        }
                    }
            );
            RequestBasketDelete.execute();
        });

        btnOrderCreate.setOnClickListener(v -> {
            OrderCreate RequestOrderCreate = new OrderCreate(
                    TOKEN,
                    new MyResponseCallback() {
                        @Override
                        public void onCompile(String result) {
                            Log.d("BASKET CREATE", result);
                            Order order = new GsonBuilder().create().fromJson(result, Order.class);
                            onBasketGet();
                            Toast.makeText(context,
                                    "Заказ успешно оформлен, при изменении статуса, вам придёт уведомление",
                                    Toast.LENGTH_SHORT).show();
                            Intent OrderService = new Intent(context, OrderService.class);
                            OrderService.putExtra("id", order.id);
                            startService(OrderService);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("BASKET CREATE", error);
                        }
                    }
            );
            RequestOrderCreate.execute();
        });

        onBasketGet();
    }

    public void onBasketGet() {
        BasketGet RequestBasketGet = new BasketGet(
                TOKEN,
                new MyResponseCallback() {
                    @Override
                    public void onCompile(String result) {
                        Log.d("BASKET GET", result);
                        ProductsBasket = new GsonBuilder().create().fromJson(
                                result,
                                new TypeToken<ArrayList<ProductBasket>>(){}.getType()
                        );
                        CreateItemBasket();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("BASKET GET", error);
                    }
                }
        );
        RequestBasketGet.execute();
    }

    public void onBasketUpdate(ProductBasket productBasket) {
        Basket Data = new Basket(productBasket.count, productBasket.product.id);
        BasketUpdate RequestBasketUpdate = new BasketUpdate(
                TOKEN,
                Data,
                new MyResponseCallback() {
                    @Override
                    public void onCompile(String result) {
                        Log.d("BASKET UPDATE", result);
                        onBasketGet();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("BASKET UPDATE", error);
                    }
                }
        );
        RequestBasketUpdate.execute();
    }

    public void CreateItemBasket() {
        llItems.removeAllViews();
        Integer AllSum = 0;

        for (int i = 0; i < ProductsBasket.size(); i++) {
            ProductBasket ProductBasket = ProductsBasket.get(i);
            View itemOrder = LayoutInflater.from(this).inflate(R.layout.item_basket, llItems, false);

            TextView tvName = itemOrder.findViewById(R.id.tvName);
            TextView tvPrice = itemOrder.findViewById(R.id.tvPrice);
            TextView tvCount = itemOrder.findViewById(R.id.tvCount);
            View btnMinus = itemOrder.findViewById(R.id.btnMinus);
            View btnPlus = itemOrder.findViewById(R.id.btnPlus);
            View btnItemClear = itemOrder.findViewById(R.id.btnItemClear);

            tvName.setText(ProductBasket.product.name);
            tvPrice.setText(ProductBasket.product.price + " ₽");
            tvCount.setText(ProductBasket.count + " штук");

            btnMinus.setOnClickListener(v -> {
                ProductBasket.count--;
                onBasketUpdate(ProductBasket);
            });

            btnPlus.setOnClickListener(v -> {
                ProductBasket.count++;
                onBasketUpdate(ProductBasket);
            });

            btnItemClear.setOnClickListener(v -> {
                ProductBasket.count = 0;
                onBasketUpdate(ProductBasket);
            });

            AllSum += ProductBasket.product.price * ProductBasket.count;
            llItems.addView(itemOrder);
        }
        tvAllSum.setText(AllSum + " ₽");
    }
}