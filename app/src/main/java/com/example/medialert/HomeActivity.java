package com.example.medialert;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import com.example.medialert.Activity.MedicineActivity;
import com.example.medialert.Activity.SignIn;
import com.example.medialert.Activity.BuyMedicineActivity;
import com.example.medialert.Activity.MyProfile;
import com.example.medialert.Activity.ScannerActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity  {
    private FirebaseAuth firebaseAuth;
    GridLayout mainGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth=FirebaseAuth.getInstance();
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);
    }

    private void setSingleEvent(GridLayout mainGrid) {
        for(int i=0;i<mainGrid.getChildCount();i++){
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {

                        Intent intent = new Intent(HomeActivity.this, MyProfile.class);
                        startActivity(intent);


                    }
                    if (finalI == 1) {

                        Intent intent = new Intent(HomeActivity.this, health.class);
                        startActivity(intent);


                    }
                    if (finalI == 2) {

                        Intent intent = new Intent(HomeActivity.this, MedicineActivity.class);
                        startActivity(intent);


                    }
                    if (finalI == 3) {

                        Intent intent = new Intent(HomeActivity.this, BuyMedicineActivity.class);
                        startActivity(intent);


                    }
                    if (finalI == 4) {

                        Intent intent = new Intent(HomeActivity.this, ScannerActivity.class);
                        startActivity(intent);


                    }
                    if (finalI == 5) {
                        MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(HomeActivity.this);
                        materialAlertDialogBuilder.setTitle("Confirm you want to logout?");
                        materialAlertDialogBuilder.setPositiveButton("Yse", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                firebaseAuth.signOut();
                                Intent intent = new Intent(HomeActivity.this, SignIn.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        materialAlertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog alertDialog=materialAlertDialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }
                }
            });
        }
    }
}