package com.example.medialert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class health extends AppCompatActivity {



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_health);

            ImageView articleImageView = findViewById(R.id.articleImageView);
            TextView articleTextView = findViewById(R.id.articleTextView);

            articleImageView.setImageResource(R.drawable.health_image);

            String healthArticle = getString(R.string.health_article_content);
            articleTextView.setText(healthArticle);
        }
    }


