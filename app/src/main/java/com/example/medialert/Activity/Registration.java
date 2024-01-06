package com.example.medialert.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.medialert.Model.User;
import com.example.medialert.R;
import com.example.medialert.Utils.Config;
import com.example.medialert.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Registration extends AppCompatActivity {
    private Spinner countrySpinner;
    private TextInputEditText nameEditText,passwordEditText,confirmPasswordEditText,emailEditText;
    private Button signUpButton;
    private LottieAnimationView lottieAnimationView;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=firebaseDatabase.getInstance(Config.dbUrl);
        emailEditText=findViewById(R.id.emailEditText);
        nameEditText=findViewById(R.id.nameEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        confirmPasswordEditText=findViewById(R.id.confirmPasswordEditText);
        signUpButton=findViewById(R.id.registrationButtonId);
        lottieAnimationView=findViewById(R.id.loadingAnimationId);
        signUpButton.setOnClickListener(view -> {
            if (Utils.checkEditTextIsNull(nameEditText,"Enter Name")&&Utils.checkEditTextIsNull(emailEditText,"Enter Email")&&Utils.checkEditTextIsNull(passwordEditText,"Enter Password")&&Utils.checkEditTextIsNull(confirmPasswordEditText,"Confirm Password")){
                String name=nameEditText.getText().toString();
                String password=passwordEditText.getText().toString();
                String confirmPassword=confirmPasswordEditText.getText().toString();
                String email=emailEditText.getText().toString();
                if (password.contentEquals(confirmPassword)){
                    User user=new User(name,email,password,new Date());
                    signUp(user);
                }else {
                    confirmPasswordEditText.setError("Password Not Match");
                    confirmPasswordEditText.requestFocus();
                    Toast.makeText(this, "Password does not match try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUp(User user) {
        lottieAnimationView.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user.setId(task.getResult().getUser().getUid());
                    saveDataInFireBase(user);
                }else {
                    lottieAnimationView.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveDataInFireBase(User user) {
        lottieAnimationView.setVisibility(View.VISIBLE);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String fcmId = task.getResult();
                    user.setFcmId(fcmId);
                    saveData(user);
                }else {
                    lottieAnimationView.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveData(User user) {
        DatabaseReference databaseReference=firebaseDatabase.getReference("User").child(user.getId());
        databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                lottieAnimationView.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(Registration.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}