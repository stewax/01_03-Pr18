package com.example.notification_klimov.domains;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionManager {
    public static void PermissionNotification(Context context, Activity activity) {
        if (CheckPermission(context) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    1
            );
        }
    }

    public static int CheckPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS);
    }
}
