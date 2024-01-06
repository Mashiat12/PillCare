package com.example.medialert.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.medialert.Adapter.HistoryAdapter;
import com.example.medialert.Model.Medicine;
import com.example.medialert.Model.Prescription;
import com.example.medialert.R;
import com.example.medialert.Utils.Config;
import com.example.medialert.Utils.SharedPreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView2;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private List<Prescription> prescriptionList;
    private List<Medicine> medicineArrayList;

    private LottieAnimationView lottieAnimationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        showToolbar("History");
        medicineArrayList=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance(Config.dbUrl);
        recyclerView2=findViewById(R.id.recyclerViewId2);
        lottieAnimationView=findViewById(R.id.lottiId);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        createMedicineList();
    }
    private void showToolbar(String title) {
        CardView backCardView=findViewById(R.id.backId);
        TextView titleTV=findViewById(R.id.activityNameId);
        titleTV.setText(title);
        backCardView.setOnClickListener(view -> {
            finish();
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
                Toast.makeText(HistoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void createHistoryPage() {
        HistoryAdapter medicineListAdapter=new HistoryAdapter(this,medicineArrayList);
        recyclerView2.setAdapter(medicineListAdapter);
    }
}