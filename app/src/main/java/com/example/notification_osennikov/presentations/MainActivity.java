package com.example.notification_osennikov.presentations;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification_osennikov.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Intent PromoService = new Intent(this, com.example.notification_osennikov.infrastructure.PromoService.class);
        startService(PromoService);
    }
}