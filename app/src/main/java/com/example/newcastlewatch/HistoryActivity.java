package com.example.newcastlewatch;

import static com.example.newcastlewatch.R.id.history;
import static com.example.newcastlewatch.R.id.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryActivity extends AppCompatActivity {

    // Declaring TextView from the Layout
    TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);


        // initializing the TextView
        textview = findViewById(R.id.text);

        // Creating a string that contains the information to be displayed
        String para = "Newcastle disease is a highly contagious disease that " +
                    " affects birds  including  domestic poultry.\n" +
                    "\n" +
                    "It is caused by virulent strains of avian paramyxovirus type 1." +
                    " The disease usually presents as a respiratory  infection, " +
                    "but depression, nervous manifestations, or diarrhoea " +
                    " may be the predominant clinical form.Virulent Newcastle"+
                    " disease virus strains are endemic in poultry in most of Asia, Africa, and" +
                    "some countries of North and South America. Other countries, "+
                    "including the united states of America and Canada," +
                    "are free of those strains in poultry\n" +
                    "\n" +
                    "TRANSMISSION\n" +
                    "\n "+
                    "According to the Journal of Veterinary Science & Technology" +
                    " The transmission of NDV occurs through respiratory aerosols," +
                    "exposure to fecal and other excretions from infected birds, through" +
                    "newly introduced birds, selling and giving away sick birds and contacts," +
                    " with contaminated feed, water, equipment and clothing. The usual." +
                    " source of virus is an infected chicken, and spread is usually attributed " +
                    "to the movement of chickens through chicken markets and traders " +
                    "Newcastle disease is very contagious and is easily spread from one" +
                    " bird to another. The infection is usually transmitted by direct contact"+
                    "with sick birds or unaffected birds carrying the virus.\n" +
                    "\n" +
                    "Even vaccinated birds that are clinically healthy can excrete virulent" +
                    " virus after they have been exposed.\n" +
                    "\n" +
                    "Experimental assessment using virulent viruses is usually \n" +
                    "\n" +
                    "hampered by cessation of egg laying in infected birds. " +
                    "Infected embryos have been reported during naturally" +
                    " occurring infections of laying hens with virulent virus " +
                    " but this generally results in the death of the " +
                    "infected embryo during incubation." +
                    " Cracked or broken infected eggs may serve as a source of virus " +
                    "Jfor newly hatched chicks, as may virus-laden feces contaminating the\n" +
                    "outside of eggs.\n " ;

        // set value to the given TextView
        textview.setText(para);

        // to perform the movement action
        // Moves the cursor or scrolls to the
        // top or bottom of the document
        textview.setMovementMethod(new ScrollingMovementMethod());




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(history);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                finish();
                return true;
            } else if (itemId == R.id.history) {

                return true;
            } else if (itemId == R.id.tips) {
                startActivity(new Intent(getApplicationContext(), TipsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

    }
}