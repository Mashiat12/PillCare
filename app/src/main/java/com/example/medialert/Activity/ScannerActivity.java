package com.example.medialert.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.medialert.Adapter.PrescriptionAdapter;
import com.example.medialert.Model.Prescription;
import com.example.medialert.R;
import com.example.medialert.Utils.Config;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ScannerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private List<Prescription> prescriptionList;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private LottieAnimationView lottieAnimationView;
    private ExtendedFloatingActionButton extendedFloatingActionButton;
    private Uri filePath;
    private ImageView iconImage;
    private FirebaseStorage firebaseStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        showToolbar("Scanner");
        prescriptionList=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance(Config.dbUrl);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        recyclerView2=findViewById(R.id.recyclerViewId2);
        recyclerView=findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        lottieAnimationView=findViewById(R.id.lottiId);
        extendedFloatingActionButton=findViewById(R.id.addMedicineId);
        extendedFloatingActionButton.setOnClickListener(view -> {
            showAlertDialog();
        });
        getAllData();

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
                Toast.makeText(ScannerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                lottieAnimationView.setVisibility(View.GONE);
            }
        });
    }
    private void showAlertDialog() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(this);
        View view=getLayoutInflater().inflate(R.layout.alert_dialog_for_add_prescription,null);
        materialAlertDialogBuilder.setView(view);

        iconImage=view.findViewById(R.id.imageId);

        Button cancelButton=view.findViewById(R.id.cancelButtonId);
        Button saveButton=view.findViewById(R.id.saveButtonId);
        iconImage.setOnClickListener(view1 -> {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Image"),1);

        });
        AlertDialog alertDialog=materialAlertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        cancelButton.setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });

        saveButton.setOnClickListener(view1 -> {
            if (filePath!=null) {
                Prescription prescription=new Prescription(UUID.randomUUID().toString(),firebaseAuth.getCurrentUser().getUid(),new Date());
                alertDialog.dismiss();
                lottieAnimationView.setVisibility(View.VISIBLE);
                String uniqueId= UUID.randomUUID().toString();
                StorageReference storageReference = firebaseStorage.getReference().child("Prescription").child(uniqueId);
                storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()){
                                    String url=task.getResult().toString();
                                    prescription.setPrescriptionUrlLink(url);
                                    saveInFirebase(prescription);

                                }else {
                                    lottieAnimationView.setVisibility(View.GONE);
                                    Toast.makeText(ScannerActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }

        });
    }

    private void saveInFirebase(Prescription prescription) {
        DatabaseReference databaseReference=firebaseDatabase.getReference("Prescription").child(prescription.getId());
        databaseReference.setValue(prescription).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                lottieAnimationView.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(ScannerActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ScannerActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            filePath=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                iconImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void createMainPage() {
        lottieAnimationView.setVisibility(View.GONE);
        PrescriptionAdapter prescriptionAdapter=new PrescriptionAdapter(this,prescriptionList);
        recyclerView.setAdapter(prescriptionAdapter);
    }

    private void showToolbar(String title) {
        CardView backCardView=findViewById(R.id.backId);
        TextView titleTV=findViewById(R.id.activityNameId);
        titleTV.setText(title);
        backCardView.setOnClickListener(view -> {
            finish();
        });
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
                    Toast.makeText(ScannerActivity.this, "Prescription Removed", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ScannerActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}