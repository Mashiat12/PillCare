package com.example.medialert.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medialert.Model.Medicine;
import com.example.medialert.R;
import com.example.medialert.Utils.Config;
import com.example.medialert.Utils.MedicalDB;
import com.example.medialert.Utils.ReminderManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlarmActivity extends AppCompatActivity {
  private Medicine medicine;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alarm);
    medicine= (Medicine) getIntent().getSerializableExtra("MedicineClass");

    TextView userName, medName, medTime, medQty;
    Button tookMed, snooze;

    userName = findViewById(R.id.alarm_user_name);
    medName = findViewById(R.id.alarm_med_name);
    medTime = findViewById(R.id.alarm_med_time);
    medQty = findViewById(R.id.alarm_med_quantity);

    tookMed = findViewById(R.id.alarm_took);
    snooze = findViewById(R.id.alarm_snooze);

    medName.setText(medicine.getName());
    medTime.setText("Time: "+medicine.getHour()+":"+medicine.getMin());
    medQty.setText("Description: "+medicine.getDescription());
    String name=medicine.getUserName();
    userName.setText(name);

    tookMed.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        saveHistoryToDatabase();
      }
    });

    snooze.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ReminderManager.setReminder(AlarmActivity.this,medicine);
        Toast.makeText(getApplicationContext(),"Reminder set after 10 minutes.",Toast.LENGTH_LONG).show();
        finish();
      }
    });
  }

  private void saveHistoryToDatabase() {
    ProgressDialog progressDialog=new ProgressDialog(this);
    progressDialog.setTitle("Pls Wait...");
    progressDialog.show();
    medicine.setTaken(true);
    DatabaseReference databaseReference= FirebaseDatabase.getInstance(Config.dbUrl).getReference("AlarmHistory").child(medicine.getId());
    databaseReference.setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()){
          finish();
        }
        else {
          Toast.makeText(AlarmActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
      }
    });
  }
}