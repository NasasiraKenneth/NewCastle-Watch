package com.example.newcastlewatch;

import static com.example.newcastlewatch.R.id.home;
import static com.example.newcastlewatch.R.id.tips;
import static com.google.android.material.internal.ViewUtils.dpToPx;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.TypedValue;
import android.content.res.Resources;

import android.graphics.Color;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tips);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(tips);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                finish();
                return true;
            } else if (itemId == R.id.history) {
                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                finish();
                return true;
            } else if (itemId == R.id.tips) {

                return true;
            }
            return false;
        });

    }
}