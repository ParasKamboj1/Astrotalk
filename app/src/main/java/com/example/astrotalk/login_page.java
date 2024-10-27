package com.example.astrotalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_page extends AppCompatActivity {
    EditText email,password;
    FirebaseAuth auth;
    TextView signuptext;
    AppCompatButton loginbutton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.EmailLogin);
        password = findViewById(R.id.PasswordLogin);
        signuptext = findViewById(R.id.signuptext);
        loginbutton = findViewById(R.id.loginbutton);

        auth = FirebaseAuth.getInstance();

        signuptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_page.this, sighnup_page.class);
                startActivity(intent);
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(login_page.this, MainActivity.class);
                            Toast.makeText(login_page.this, "Login SuccessFull!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(login_page.this, task.getException().getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }


}