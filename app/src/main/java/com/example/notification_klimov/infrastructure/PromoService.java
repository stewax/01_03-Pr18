package com.example.notification_klimov.infrastructure;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.notification_klimov.domains.NotifyManager;

import java.util.Random;

public class PromoService extends Service {
    String TAG = "PROMO SERVICE";
    Integer PromoInterval = 5 * 60 * 1000;
    Handler handler = new Handler();
    NotifyManager notifyManager;
    Random random = new Random();

    String[] promos = {
            "Скидка 20% на всё!",
            "Бесплатная доставка!",
            "Закажи сегодня - получи кешбек 10%",
            "Товары недели со скидкой!"
    };

    Runnable PromoRunnable = new Runnable() {
        @Override
        public void run() {
            String randomPromo = promos[random.nextInt(promos.length)];
            if (notifyManager != null) {
                notifyManager.SendNotify(randomPromo);
            }
            handler.postDelayed(PromoRunnable, PromoInterval);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        notifyManager = new NotifyManager(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(PromoRunnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(PromoRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
