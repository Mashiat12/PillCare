package com.example.medialert.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.medialert.Model.TouchImageView;
import com.example.medialert.R;
import com.squareup.picasso.Picasso;

public class OpenImageActivity extends AppCompatActivity {
    TouchImageView touchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_image);
        touchImageView=findViewById(R.id.imageViewId);
        Picasso.get().load(getIntent().getStringExtra("url")).into(touchImageView);
    }
}