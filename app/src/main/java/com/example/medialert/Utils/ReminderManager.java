package com.example.medialert.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.medialert.Model.Medicine;

public class ReminderManager {
    public static void setReminder(Context context, Medicine medicine) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent resultIntent=new Intent(context, AlarmBroadcastReceiver.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("MedicineClass",medicine);
        resultIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        long currentTime = System.currentTimeMillis();
        long tenMinutesInMillis = 10 * 60 * 1000; // 10 minutes
        long triggerTime = currentTime + tenMinutesInMillis;
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }
}
