package com.example.quizzy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore dbFireStore;


    EditText nameEditText, emailEditText, passwordEditText, referBoxEditText;
    Button submitButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEditText = findViewById(R.id.nameBox);
        emailEditText = findViewById(R.id.emailBox);
        passwordEditText = findViewById(R.id.passwordBox);
        referBoxEditText = findViewById(R.id.referBox);
        submitButton = findViewById(R.id.createNewBtn);
        loginButton = findViewById(R.id.loginBtn);
        
        auth = FirebaseAuth.getInstance();
        dbFireStore = FirebaseFirestore.getInstance();
        
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name, email, pass, referCode;

                Name = nameEditText.getText().toString();
                email = emailEditText.getText().toString();
                pass = passwordEditText.getText().toString();
                referCode = referBoxEditText.getText().toString();

                final User usr = new User(Name, email, pass, referCode);
                
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid = task.getResult().getUser().getUid();
                            dbFireStore.collection("User Collection").document(uid).set(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else{
                                        Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Toast.makeText(SignupActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        
    }
}