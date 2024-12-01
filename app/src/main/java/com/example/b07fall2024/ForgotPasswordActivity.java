package com.example.b07fall2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.b072024gr2.ecoproj.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

//1. 点击x回到login界面
//2. 点击sent reset link调用fb API让后端发送邮件
//3. 邮件发送后发 email sent的提醒
//4. 点击back to sign in回到login界面
public class ForgotPasswordActivity extends AppCompatActivity {
    EditText email_input;
    FirebaseAuth mAuth;
    String email;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        TextView close_text = findViewById(R.id.close_text);
        email_input = findViewById(R.id.email_input);
        Button reset_button = findViewById(R.id.reset_button);
        TextView backToLogin_text = findViewById(R.id.backToLogin_text);
        mAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.progressbar);

        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                email = email_input.getText().toString().trim();
                if (!email.isEmpty()) {
                    progressbar.setVisibility(View.GONE);
                    mAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener(new OnSuccessListener<Void>(){
                                @Override
                                public void onSuccess(Void unused){
                                    Toast.makeText(ForgotPasswordActivity.this, "Reset password link has been sent to your registered email successfully!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener(){
                                @Override
                                public void onFailure(@NonNull Exception e){
                                    Toast.makeText(ForgotPasswordActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    email_input.setError("Email address can't be empty");
                }
            }
        });

        backToLogin_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
