package com.example.notification_kazakov.infrastructure;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.network.datas.order.OrderGet;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.models.Order;
import com.example.notification_kazakov.R;
import com.example.notification_kazakov.domains.NotifyManager;
import com.google.gson.GsonBuilder;

public class OrderService extends Service {
    private static final String TAG = "ORDER SERVICE";
    public static final String EXTRA_ORDER_ID = "order_id";
    public static final String EXTRA_AUTH_TOKEN = "auth_token";
    private static final int NOTIFICATION_ID = 1;

    private Integer orderId = null;
    private String authToken = null;
    private final int intervalMs = 30 * 1000;
    private final Handler handler = new Handler();
    private NotifyManager notifyManager;

    private final Runnable checkStatusRunnable = new Runnable() {
        @Override
        public void run() {
            if (orderId == null || authToken == null) {
                Log.e(TAG, "orderId or token is null. Stopping.");
                stopSelf();
                return;
            }

            OrderGet orderGet = new OrderGet(orderId, authToken, new MyResponseCallback() {
                @Override
                public void onCompile(String result) {
                    try {
                        Order orderData = new GsonBuilder().create().fromJson(result, Order.class);
                        if (orderData != null && orderData.status == 1) {
                            int allSum = 0;
                            for (int i = 0; i < orderData.products.size(); i++) {
                                allSum += orderData.products.get(i).product.price * orderData.products.get(i).count;
                            }

                            notifyManager.SendNotify(
                                    "Ваш заказ на сумму: " + allSum +
                                            " руб., состоящий из " + orderData.products.size() +
                                            " товаров, успешно доставлен."
                            );
                            stopSelf();
                        } else {
                            handler.postDelayed(checkStatusRunnable, intervalMs);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to parse order: " + e.getMessage());
                        handler.postDelayed(checkStatusRunnable, intervalMs);
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Order check failed: " + error);
                    handler.postDelayed(checkStatusRunnable, intervalMs);
                }
            });
            orderGet.execute();
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
        if (intent == null) {
            Log.e(TAG, "Intent is null. Stopping service.");
            stopSelf();
            return START_NOT_STICKY;
        }

        orderId = intent.getIntExtra(EXTRA_ORDER_ID, -1);
        authToken = intent.getStringExtra(EXTRA_AUTH_TOKEN);

        if (orderId == -1 || authToken == null || authToken.isEmpty()) {
            Log.e(TAG, "Missing order ID or token. Stopping service.");
            stopSelf();
            return START_NOT_STICKY;
        }

        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, "ORDER_TRACKING_CHANNEL")
                .setContentTitle("Отслеживание заказа")
                .setContentText("Ожидаем подтверждение доставки...")
                .setSmallIcon(R.drawable.icon)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        handler.removeCallbacks(checkStatusRunnable);
        handler.post(checkStatusRunnable);

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "ORDER_TRACKING_CHANNEL",
                    "Отслеживание заказов",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkStatusRunnable);
        Log.d(TAG, "Service destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}