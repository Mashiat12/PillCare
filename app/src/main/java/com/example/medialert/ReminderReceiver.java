package com.example.medialert;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get data from the intent
        String medicineName = intent.getStringExtra("medicineName");
        String dosage = intent.getStringExtra("dosage");
        String frequency = intent.getStringExtra("frequency");

        // Display a reminder message
        String reminderMessage = "Take " + dosage + " of " + medicineName + " (" + frequency + ")";
        Toast.makeText(context, reminderMessage, Toast.LENGTH_LONG).show();
    }
}