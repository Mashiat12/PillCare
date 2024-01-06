package com.example.medialert.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.medialert.HomeActivity;
import com.example.medialert.R;
import com.example.medialert.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private TextView registrationTV;
    private LottieAnimationView lottieAnimationView;
    private TextInputEditText mobileNumberEditText,passwordEditText;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth=FirebaseAuth.getInstance();
        registrationTV=findViewById(R.id.registrationTVId);
        lottieAnimationView=findViewById(R.id.loadingAnimationId);
        loginButton=findViewById(R.id.loginId);
        mobileNumberEditText=findViewById(R.id.phoneNumberEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        registrationTV.setOnClickListener(view -> {
            startActivity(new Intent(this,Registration.class));
        });

        loginButton.setOnClickListener(view -> {
            loginButton.setEnabled(false);
            if (Utils.checkEditTextIsNull(mobileNumberEditText,"Enter Email")&&Utils.checkEditTextIsNull(passwordEditText,"Enter Password")){
                String email=mobileNumberEditText.getText().toString();
                String password=passwordEditText.getText().toString();
                signIn(email,password);
            }
        });
    }

    private void signIn(String email, String password) {
        lottieAnimationView.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                lottieAnimationView.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    startActivity(new Intent(SignIn.this,HomeActivity.class));
                    finish();
                }else {
                    loginButton.setEnabled(true);
                    Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}