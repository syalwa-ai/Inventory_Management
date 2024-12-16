package com.example.onlineshoppingfp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private EditText etMail, etPassword, etConfirmPassword;
    private Button btnRegister, btnBack;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        etMail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etMail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (!(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())){
                    if (password.length() > 6) {
                        if (confirmPassword.equals(password)){

                            auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(getApplicationContext(), "Register is Successfull. Please Check Your Email!", Toast.LENGTH_SHORT).show();

                                                            startActivity(new Intent(getApplicationContext(), Login.class));
                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "Verification Failed to Deliver!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(getApplicationContext(), "Register is Failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }else{
                            etConfirmPassword.setError("Password must be same!");
                        }
                    }else{
                        etPassword.setError("Password must be Consist 6 characters!");
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please fill the blank!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}