package com.example.medialert.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.medialert.Adapter.HistoryAdapter;
import com.example.medialert.Adapter.MedicineListAdapter;
import com.example.medialert.Adapter.PrescriptionAdapter;
import com.example.medialert.Model.Medicine;
import com.example.medialert.Model.Prescription;
import com.example.medialert.Model.User;
import com.example.medialert.R;
import com.example.medialert.Utils.Config;
import com.example.medialert.Utils.SharedPreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyProfile extends AppCompatActivity {
    private SharedPreferenceManager sharedPreferenceManager;
    private TextView nameTV;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private List<Prescription> prescriptionList;
    private List<Medicine> medicineArrayList;
    private LottieAnimationView lottieAnimationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labtest);
        prescriptionList=new ArrayList<>();
        medicineArrayList=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance(Config.dbUrl);
        sharedPreferenceManager=new SharedPreferenceManager(this);

        recyclerView=findViewById(R.id.recyclerViewId);
        recyclerView2=findViewById(R.id.recyclerViewId2);
        lottieAnimationView=findViewById(R.id.lottiId);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        nameTV=findViewById(R.id.med_user_name);
        if (sharedPreferenceManager.getName().length()!=0){
            nameTV.setText(sharedPreferenceManager.getName());
        }else {
            getUserInfo();
        }
        getAllData();
        createMedicineList();
    }
    private void getAllData() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference=firebaseDatabase.getReference("Prescription");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prescriptionList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Prescription prescription=dataSnapshot.getValue(Prescription.class);
                    if (prescription.getUploaderUserId().contentEquals(firebaseAuth.getCurrentUser().getUid())){
                        prescriptionList.add(prescription);
                    }
                }
                createMainPage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                lottieAnimationView.setVisibility(View.GONE);
            }
        });
    }
    private void createMainPage() {
        lottieAnimationView.setVisibility(View.GONE);
        PrescriptionAdapter prescriptionAdapter=new PrescriptionAdapter(this,prescriptionList);
        recyclerView.setAdapter(prescriptionAdapter);
    }


    private void getUserInfo() {
        DatabaseReference databaseReference=firebaseDatabase.getReference("User").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                nameTV.setText(user.getName());
                sharedPreferenceManager.setName(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void createMedicineList() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference=firebaseDatabase.getReference("AlarmHistory");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lottieAnimationView.setVisibility(View.GONE);
                medicineArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Medicine medicine=dataSnapshot.getValue(Medicine.class);
                    if(medicine.getUserId().contentEquals(firebaseAuth.getCurrentUser().getUid())&&medicine.isTaken()){
                        medicineArrayList.add(medicine);
                    }
                }
                createHistoryPage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lottieAnimationView.setVisibility(View.GONE);
                Toast.makeText(MyProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void createHistoryPage() {
        HistoryAdapter medicineListAdapter=new HistoryAdapter(this,medicineArrayList);
        recyclerView2.setAdapter(medicineListAdapter);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==12){
            deleteItemFromDatabase(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteItemFromDatabase(int groupId) {
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference=firebaseDatabase.getReference("Prescription").child(prescriptionList.get(groupId).getId());
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                lottieAnimationView.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(MyProfile.this, "Prescription Removed", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}