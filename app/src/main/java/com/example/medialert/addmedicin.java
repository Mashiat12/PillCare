package com.example.medialert;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class addmedicin extends AppCompatActivity {

    private EditText medicineNameEditText;
    private EditText dosageEditText;
    private EditText frequencyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmedicin);


        medicineNameEditText = findViewById(R.id.medicineNameEditText);
        dosageEditText = findViewById(R.id.dosageEditText);
        frequencyEditText = findViewById(R.id.frequencyEditText);

        Button setReminderButton = findViewById(R.id.setReminderButton);
        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get entered data
                String medicineName = medicineNameEditText.getText().toString();
                String dosage = dosageEditText.getText().toString();
                String frequency = frequencyEditText.getText().toString();

                // Validate data
                if (medicineName.isEmpty() || dosage.isEmpty() || frequency.isEmpty()) {
                    Toast.makeText(addmedicin.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Set a reminder for the entered medicine
                setReminder(medicineName, dosage, frequency);
            }
        });
    }

    private void setReminder(String medicineName, String dosage, String frequency) {
        // Create an intent to trigger the reminder
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("medicineName", medicineName);
        intent.putExtra("dosage", dosage);
        intent.putExtra("frequency", frequency);

        // Create a PendingIntent for the reminder
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Set the reminder time (for testing, set it to 10 seconds from now)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10);

        // Set the reminder using AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Reminder set for " + frequency, Toast.LENGTH_SHORT).show();
        }
    }
}

