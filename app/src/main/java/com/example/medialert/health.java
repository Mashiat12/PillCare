package com.example.medialert;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class health extends AppCompatActivity {
    EditText etHeight;
    EditText etWeight;
    TextView bmi, status, bmi_tv, articleTextView;
    Button ReCalculate, calculate_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        ImageView articleImageView = findViewById(R.id.articleImageView);
        articleTextView = findViewById(R.id.articleTextView);

        articleImageView.setImageResource(R.drawable.health_pic);

        // Set the HTML-formatted text to TextView
        String healthArticle = getString(R.string.health_article_content);
        articleTextView.setText(Html.fromHtml(healthArticle));

        // Initialize other UI components
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        bmi = findViewById(R.id.bmi);
        status = findViewById(R.id.status);
        ReCalculate = findViewById(R.id.ReCalculate);
        bmi_tv = findViewById(R.id.bmi_tv);
        calculate_btn = findViewById(R.id.calculate_btn);

        findViewById(R.id.calculate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(health.this, "something clicked", Toast.LENGTH_SHORT).show();

                String heightStr = etHeight.getText().toString();
                String weightStr = etWeight.getText().toString();

                if (!heightStr.isEmpty() && !weightStr.isEmpty()) {
                    int height = Integer.parseInt(heightStr);
                    int weight = Integer.parseInt(weightStr);

                    float BMI = calculateBMI(height, weight);

                    bmi.setText(String.valueOf(BMI));
                    bmi.setVisibility(View.VISIBLE);

                    Toast.makeText(health.this, String.valueOf(BMI), Toast.LENGTH_SHORT).show();

                    if (BMI < 18.5) {
                        status.setText("Under Weight");
                    } else if (BMI >= 18.5 && BMI < 24.9) {
                        status.setText("Healthy");
                    } else if (BMI >= 24.9 && BMI < 30) {
                        status.setText("Overweight");
                    } else if (BMI >= 30) {
                        status.setText("Suffering from Obesity");
                    }
                    bmi_tv.setVisibility(View.VISIBLE);
                    status.setVisibility(View.VISIBLE);

                    ReCalculate.setVisibility(View.VISIBLE);
                    calculate_btn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(health.this, "please enter the valid height and weight", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.ReCalculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetEverything();
            }
        });
    }

    private void ResetEverything() {
        calculate_btn.setVisibility(View.VISIBLE);
        ReCalculate.setVisibility(View.GONE);

        etHeight.getText().clear();
        etWeight.getText().clear();
        status.setText(" ");
        bmi.setText(" ");
        bmi_tv.setVisibility(View.GONE);
    }

    private float calculateBMI(int height, int weight) {
        float Height_in_metre = (float) height / 100;
        return weight / (Height_in_metre * Height_in_metre);
    }
}