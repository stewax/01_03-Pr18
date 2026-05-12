package com.example.notification_klimov.infrastructure;

import android.app.Service;
import android.os.Handler;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.network.datas.order.OrderGet;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.models.Order;
import com.example.notification_klimov.presentations.BasketActivity;
import com.example.notification_klimov.domains.NotifyManager;
import com.google.gson.GsonBuilder;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderService extends Service {
    public Integer id;
    String TAG = "ORDER SERVICE";
    Integer Interval = 30 * 1000;
    Integer PromoInterval = 60 * 60 * 1000;
    Handler handler = new Handler();
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    NotifyManager notifyManager;
    Random random = new Random();

    String[] promos = {
            "Скидка 20% на всё!",
            "Бесплатная доставка!",
            "Закажи сегодня - получи кешбек 10%",
            "Товары недели со скидкой!"
    };

    Runnable CheckStatusRunnable = new Runnable() {
        @Override
        public void run() {
            OrderGet ResponseOrderGet = new OrderGet(
                    id,
                    BasketActivity.TOKEN,
                    new MyResponseCallback() {
                        @Override
                        public void onCompile(String result) {
                            Order OrderData = new GsonBuilder().create().fromJson(result, Order.class);
                            if (OrderData.status == 1) {
                                Integer AllSum = 0;
                                for (int i = 0; i < OrderData.products.size(); i++) {
                                    AllSum += OrderData.products.get(i).product.price + OrderData.products.get(i).count;
                                }

                                notifyManager.SendNotify(
                                        "Ваш заказ, на сумму: " + AllSum +
                                                " состоящий из " + OrderData.products.size() +
                                                " товаров, успешно доставлен."
                                );
                            } else {
                                handler.postDelayed(CheckStatusRunnable, Interval);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Log.e(TAG, error);
                        }
                    }
            );
            ResponseOrderGet.execute();
        }
    };

    Runnable PromoRunnable = new Runnable() {
        @Override
        public void run() {
            String randomPromo = promos[random.nextInt(promos.length)];
            notifyManager.SendNotify(randomPromo);
            handler.postDelayed(PromoRunnable, PromoInterval);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        notifyManager = new NotifyManager(this);
        Log.d(TAG, "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.removeCallbacks(CheckStatusRunnable);
        handler.removeCallbacks(PromoRunnable);

        handler.post(CheckStatusRunnable);

        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
        }

        handler.postDelayed(PromoRunnable, 10000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(CheckStatusRunnable);
        executorService.shutdown();
        Log.d(TAG, "Service destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
