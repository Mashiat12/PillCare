package com.example.medialert.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.medialert.Adapter.SellMedicineListAdapter;
import com.example.medialert.Model.SellMedicine;
import com.example.medialert.R;
import com.example.medialert.Utils.Config;
import com.example.medialert.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BuyMedicineActivity extends AppCompatActivity {
    private ExtendedFloatingActionButton extendedFloatingActionButton;
    private RecyclerView recyclerView;
    private List<SellMedicine> sellMedicineList;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private LottieAnimationView lottieAnimationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buymedicin);
        showToolbar("Available Medicine");
        sellMedicineList=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance(Config.dbUrl);
        firebaseAuth=FirebaseAuth.getInstance();


        recyclerView=findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        lottieAnimationView=findViewById(R.id.lottiId);
        extendedFloatingActionButton=findViewById(R.id.addMedicineId);
        
        
        extendedFloatingActionButton.setOnClickListener(view -> {
            showAlertDialogForAddMedicine();
        });
        
        getAllData();
        

    }

    private void showToolbar(String title) {
        CardView backCardView=findViewById(R.id.backId);
        TextView titleTV=findViewById(R.id.activityNameId);
        titleTV.setText(title);
        backCardView.setOnClickListener(view -> {
            finish();
        });
    }

    private void getAllData() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference=firebaseDatabase.getReference("Sell_Medicine_List");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lottieAnimationView.setVisibility(View.GONE);
                sellMedicineList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    SellMedicine sellMedicine=dataSnapshot.getValue(SellMedicine.class);
                    sellMedicineList.add(sellMedicine);
                }
                createMainPage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createMainPage() {
        SellMedicineListAdapter sellMedicineListAdapter=new SellMedicineListAdapter(this,sellMedicineList);
        recyclerView.setAdapter(sellMedicineListAdapter);
        sellMedicineListAdapter.setItemClick(new SellMedicineListAdapter.ItemClick() {
            @Override
            public void onClick(int position) {

            }
        });
        sellMedicineListAdapter.setCallButtonClick(position -> {
            openDialPad(sellMedicineList.get(position));
        });
    }

    private void openDialPad(SellMedicine sellMedicine) {
        String phoneNumber = "tel:"+sellMedicine.getContactNumber();
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse(phoneNumber));
        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        }
    }

    private void showAlertDialogForAddMedicine() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(this);
        View view=getLayoutInflater().inflate(R.layout.alert_dialog_for_sell_medicine,null);
        materialAlertDialogBuilder.setView(view);

        Button saveButton=view.findViewById(R.id.saveButtonId);
        Button cancelButton=view.findViewById(R.id.cancelButtonId);

        TextInputEditText medicineNameEditText=view.findViewById(R.id.medicineNameEditText);
        TextInputEditText contactNumberEditText=view.findViewById(R.id.contactNumberEditText);
        TextInputEditText priceEditText=view.findViewById(R.id.priceEditText);
        TextInputEditText description=view.findViewById(R.id.descriptionEditText);


        AlertDialog alertDialog=materialAlertDialogBuilder.create();
        alertDialog.show();

        cancelButton.setOnClickListener(view1 -> {
            alertDialog.create();
        });
        saveButton.setOnClickListener(view1 -> {
            if (Utils.checkEditTextIsNull(medicineNameEditText,"Enter Medicine Name")&&Utils.checkEditTextIsNull(contactNumberEditText,"Enter Contact Number")&&Utils.checkEditTextIsNull(priceEditText,"Enter Price")&&Utils.checkEditTextIsNull(description,"Enter Description")){
                String medicineName=medicineNameEditText.getText().toString();
                String contactNumber=contactNumberEditText.getText().toString();
                String price=priceEditText.getText().toString();
                String descriptionText=description.getText().toString();


                SellMedicine sellMedicine=new SellMedicine(UUID.randomUUID().toString(),medicineName,price,descriptionText,contactNumber,firebaseAuth.getCurrentUser().getUid(),new Date());
                saveData(sellMedicine);
                alertDialog.dismiss();
            }
        });
    }

    private void saveData(SellMedicine sellMedicine) {
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference=firebaseDatabase.getReference("Sell_Medicine_List").child(sellMedicine.getId());
        databaseReference.setValue(sellMedicine).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(BuyMedicineActivity.this, "Save", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(BuyMedicineActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}