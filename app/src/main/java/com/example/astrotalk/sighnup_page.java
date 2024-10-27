package com.example.astrotalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class sighnup_page extends AppCompatActivity {
    EditText username,password,email,phonenumber;
    FirebaseAuth auth;
    TextView loginpage;
    AppCompatButton signupbutton;
    DatabaseReference databaseReference;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighnup_page);

        loginpage = findViewById(R.id.loginpage);
        username = findViewById(R.id.textview3);
        password = findViewById(R.id.passwordsignup);
        email = findViewById(R.id.emailsignup);
        phonenumber = findViewById(R.id.phonenumbersignup);
        signupbutton = findViewById(R.id.signupbutton);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        loginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sighnup_page.this, login_page.class);
                startActivity(intent);
            }
        });
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Phonenumber = phonenumber.getText().toString().trim();

                signUpUser(Username,Password,Email,Phonenumber);
            }
        });
    }
    void signUpUser(String username, String password, String email, String phonenumber) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if (firebaseUser != null) {
                        String userId = firebaseUser.getUid();

                        // Prepare user data for database storage
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("Name", username);
                        userMap.put("Password", password);
                        userMap.put("Email", email);
                        userMap.put("PhoneNumber", phonenumber);

                        databaseReference.child("Users").child(userId).setValue(userMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(sighnup_page.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                            // Navigate to login page on successful registration
                                            Intent intent1 = new Intent(sighnup_page.this, login_page.class);
                                            startActivity(intent1);
                                            finish();
                                        } else {
                                            Toast.makeText(sighnup_page.this, "Database error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(sighnup_page.this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    // Firebase authentication error handling
                    Toast.makeText(sighnup_page.this, "Authentication error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}