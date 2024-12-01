package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.b072024gr2.ecoproj.R;
import com.example.b07fall2024.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText name_input;//this is the username
    EditText username_input;//this is actually email
    EditText password_input;
    EditText confirmpassword_input;
    FirebaseAuth mAuth;
    ProgressBar progressbar;
    DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        Button register_button = findViewById(R.id.register_button);
        name_input = findViewById(R.id.name_input);
        username_input = findViewById(R.id.username_input);
        password_input = findViewById(R.id.password_input);
        confirmpassword_input = findViewById(R.id.confirmpassword_input);
        TextView backToSignin_text = findViewById(R.id.backToSignin_text);
        mAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.progressbar);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                String name = name_input.getText().toString();
                String username = username_input.getText().toString();
                String password = password_input.getText().toString();
                String confirmpassword = confirmpassword_input.getText().toString();

                if(TextUtils.isEmpty(name)){
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(username)){
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmpassword)){
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Please confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmpassword)) {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressbar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    String uid = mAuth.getCurrentUser().getUid();
                                    User newUser = new User(name, username, uid);

                                    databaseReference.child("Users").child(uid).setValue(newUser)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, "User data saved in Database.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    Toast.makeText(SignUpActivity.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    Log.e("FirebaseAuthError", "Error: " + task.getException().getMessage());
                                    // If sign in fails, display a message to the user.
                                    //Toast.makeText(SignUpActivity.this,
                                                     //task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        backToSignin_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
