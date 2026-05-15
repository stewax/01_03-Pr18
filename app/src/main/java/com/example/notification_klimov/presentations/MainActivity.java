package com.example.notification_klimov.presentations;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notification_klimov.R;
import com.example.notification_klimov.infrastructure.OrderService;
import com.example.notification_klimov.infrastructure.PromoService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Intent PromoService = new Intent(this, com.example.notification_klimov.infrastructure.PromoService.class);
        startService(PromoService);
    }
}