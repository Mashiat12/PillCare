package com.example.medialert.Activity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medialert.Adapter.HistoryAdapter;
import com.example.medialert.Adapter.MedicineListAdapter;
import com.example.medialert.Model.Medicine;
import com.example.medialert.Model.User;
import com.example.medialert.R;
import com.example.medialert.Utils.Config;
import com.example.medialert.Utils.TimePickerFragment;
import com.example.medialert.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MedicineActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    public RecyclerView medList;
    public TextView medUserName;
    public MedicineListAdapter medListAdapter;
    public FloatingActionButton medFab;
    public int user_id;
    Button medTime;
    TextInputEditText medName, medicineDescription;
    Switch isRepeat;
    ChipGroup chipGroup;
    Chip sun,mon,tue,wed,thu,fri,sat;
    Button button;
    private ArrayList<Medicine> medicineArrayList;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private User user;
    private int hour,min;
    private boolean isTimeTaken=false;
    private boolean isRepeated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Pls Wait...");


        firebaseDatabase=FirebaseDatabase.getInstance(Config.dbUrl);
        firebaseAuth=FirebaseAuth.getInstance();

        medicineArrayList=new ArrayList<>();
        user_id = getIntent().getIntExtra("userId",0);
        medList = findViewById(R.id.med_list);
        medUserName = findViewById(R.id.med_user_name);
        medFab = findViewById(R.id.med_fab);
        button=findViewById(R.id.historyId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        medList.setLayoutManager(linearLayoutManager);
        medListAdapter = new MedicineListAdapter(this,medicineArrayList);
        medList.setAdapter(medListAdapter);
        button.setOnClickListener(view -> {
            Intent intent=new Intent(this,HistoryActivity.class);
            intent.putExtra("id",user_id);
            startActivity(intent);
        });


        getUserDetails();




        medFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicineAdder().show();
            }
        });

        medUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextDialog().show();
            }
        });
    }

    private void getUserDetails() {
        progressDialog.show();
        DatabaseReference databaseReference=firebaseDatabase.getReference("User").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                user=snapshot.getValue(User.class);
                if (user.getName()!=null){
                    medUserName.setText(user.getName());
                }
                createMedicineList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.cancel();
                Toast.makeText(MedicineActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void createMedicineList() {
        DatabaseReference databaseReference=firebaseDatabase.getReference("AlarmHistory");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.cancel();
                medicineArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Medicine medicine=dataSnapshot.getValue(Medicine.class);
                    if(medicine.getUserId().contentEquals(firebaseAuth.getCurrentUser().getUid())){
                        medicineArrayList.add(medicine);
                    }
                }
                createHistoryPage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.cancel();
                Toast.makeText(MedicineActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void createHistoryPage() {
        MedicineListAdapter medicineListAdapter=new MedicineListAdapter(this,medicineArrayList);
        medList.setAdapter(medicineListAdapter);
    }

    private AlertDialog myTextDialog() {
        View layout = View.inflate(this, R.layout.add_user_dialog, null);
        TextInputEditText savedText = layout.findViewById(R.id.add_username);
        TextView title = layout.findViewById(R.id.headingId);
        Button saveButton = layout.findViewById(R.id.save_button);
        Button cancelButton = layout.findViewById(R.id.cancel_button);
        title.setText("Update User");
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(layout);
        AlertDialog alertDialog= builder.create();
        cancelButton.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        saveButton.setOnClickListener(view -> {
            medUserName.setText(savedText.getText().toString());
        });
        alertDialog.setCancelable(false);
        return  alertDialog;
    }

    private AlertDialog medicineAdder(){
        View layout = View.inflate(this, R.layout.add_med_dialog, null);
        medName = layout.findViewById(R.id.add_med_name);
        medicineDescription = layout.findViewById(R.id.add_med_qty);
        medTime = layout.findViewById(R.id.add_med_time);
        isRepeat = layout.findViewById(R.id.repeat_switch);
        chipGroup = layout.findViewById(R.id.chip_group);
        Button saveButton = layout.findViewById(R.id.save_button);
        Button cancelButton = layout.findViewById(R.id.cancel_button);
        setChildrenEnabled(chipGroup,false);
        sun = layout.findViewById(R.id.sunday);
        mon = layout.findViewById(R.id.monday);
        tue = layout.findViewById(R.id.tuesday);
        wed = layout.findViewById(R.id.wednesday);
        thu = layout.findViewById(R.id.thursday);
        fri = layout.findViewById(R.id.friday);
        sat = layout.findViewById(R.id.saturday);
        medTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"Time Picker");
            }
        });

        isRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRepeat.isChecked()){
                    setChildrenEnabled(chipGroup,false);
                }else{
                    setChildrenEnabled(chipGroup,true);
                    isRepeated=true;
                }
            }
        });


        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(layout);
        AlertDialog alertDialog= builder.create();
        saveButton.setOnClickListener(view -> {
            if (Utils.checkEditTextIsNull(medName,"Enter Medicine Name")&&Utils.checkEditTextIsNull(medicineDescription,"Enter Description")){
                String description = medicineDescription.getText().toString();
                String days="0000000";
                if (isTimeTaken){
                    if(isRepeat.isChecked()){
                        days = setDaysFormat(sun,mon,tue,wed,thu,fri,sat);
                    }

                    Medicine medicine=new Medicine(UUID.randomUUID().toString(),user.getId(),medName.getText().toString(),days,description,user.getName(),hour,min,isRepeated);
                    medicine.setEnabled(false);
                    saveDataInFirebase(medicine);



                /*createMedicineList();
                medListAdapter.setMedicineArrayList(medicineArrayList);
                medListAdapter.notifyDataSetChanged();*/
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(this, "Please Select Time", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    private void saveDataInFirebase(Medicine medicine) {
        progressDialog.show();
        DatabaseReference databaseReference=firebaseDatabase.getReference("AlarmHistory").child(medicine.getId());
        databaseReference.setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MedicineActivity.this, "History Saved Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MedicineActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String setDaysFormat(Chip sun, Chip mon, Chip tue, Chip wed, Chip thu, Chip fri, Chip sat){
        String dayString = ""+ (sun.isChecked()?"1":"0") + (mon.isChecked()?"1":"0") + (tue.isChecked()?"1":"0") + (wed.isChecked()?"1":"0") + (thu.isChecked()?"1":"0") + (fri.isChecked()?"1":"0") + (sat.isChecked()?"1":"0");
        return dayString;
    }

    public void setChildrenEnabled(ChipGroup chipGroup, Boolean enable) {
        for(int i=0; i<chipGroup.getChildCount(); i++){
            chipGroup.getChildAt(i).setEnabled(enable);
        }

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        isTimeTaken=true;
        hour=hourOfDay;
        min=minute;
        medTime.setText(hourOfDay + ":"+minute);
    }
}