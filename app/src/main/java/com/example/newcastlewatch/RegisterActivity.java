package com.example.newcastlewatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newcastlewatch.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = binding.signUpFirstname.getText().toString();
                String second_name = binding.signUpSecondName.getText().toString();
                String email = binding.signUpEmail.getText().toString();
                String password = binding.signUpPassword.getText().toString();
                String confirmPassword = binding.signUpConfirmPassword.getText().toString();

                if (firstname.equals("") || second_name.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")){
                    Toast.makeText(RegisterActivity.this,"All fields are mandatory", Toast.LENGTH_SHORT).show();
                }else {
                    if (password.equals(confirmPassword)){
                        Boolean checkUserEmail = databaseHelper.checkEmail(email);

                        if (checkUserEmail == false){
                            Boolean insert = databaseHelper.insertData(firstname,second_name,email, password);

                            if (insert == true){
                                Toast.makeText(RegisterActivity.this,"SignUp Successful",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegisterActivity.this, "SignUp Failed", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(RegisterActivity.this, "Passwords not the same", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });





    }
}