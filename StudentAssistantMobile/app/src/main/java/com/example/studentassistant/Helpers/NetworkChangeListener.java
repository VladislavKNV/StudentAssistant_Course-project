package com.example.studentassistant.Helpers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.android.material.snackbar.Snackbar;



public class NetworkChangeListener  extends BroadcastReceiver {

    int check = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Internet.ConnectionCheck(context))  //Internet is not Connected
        {
            check = 1;
            Toast.makeText(context, "Подключение к интернету прервано", Toast.LENGTH_SHORT).show();
        } else {
            check = 2;
            Toast.makeText(context,
                    "Подключение к интернету есть", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checking() {
        if (check == 1) {
            return true;
        } else {
            return false;
        }
    }
}