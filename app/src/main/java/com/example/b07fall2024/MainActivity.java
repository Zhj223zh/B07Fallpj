package com.example.b07fall2024;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.b072024gr2.ecoproj.R;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginContract.View{
    private EditText username_input;
    private EditText password_input;
    private ProgressBar progressbar;
    private CheckBox remember_me_checkbox;

    private LoginContract.Presenter presenter;
    private LoginModel model;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        LoginModel model = new LoginModel();
        presenter = new LoginPresenter(model,this);
        //model = LoginModel.getInstance();

        username_input = findViewById(R.id.username_input);
        password_input = findViewById(R.id.password_input);
        progressbar = findViewById(R.id.progressbar);
        Button login_button = findViewById(R.id.login_button);
        TextView forgotPassword_text = findViewById(R.id.forgotPassword_text);
        TextView backToSignup_text = findViewById(R.id.backToSignup_text);
        remember_me_checkbox = findViewById(R.id.remember_me_checkbox);
        if (remember_me_checkbox == null) {
            Log.e("Debug", "Checkbox is null");
        } else {
            Log.d("Debug", "Checkbox initialized successfully");
        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                String username = username_input.getText().toString();
                String password = password_input.getText().toString();

                editor.putBoolean("remember", remember_me_checkbox.isChecked());
                editor.putString("email", remember_me_checkbox.isChecked()? username : "");
                editor.putString("password", remember_me_checkbox.isChecked()? password : "");
                editor.apply();

                if(TextUtils.isEmpty(username)){
                    hideProgressBar();
                    showError("Please enter email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    hideProgressBar();
                    showError("Please enter password");
                    return;
                }

                presenter.login(username, password);
            }
        });

        forgotPassword_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToForgotPasswordActivity();
            }
        });

        backToSignup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "SignUp TextView clicked");
                jumpToSignUpActivity();
            }
        });


        preferences = getSharedPreferences("b07project", Context.MODE_PRIVATE);
        editor = preferences.edit();
        checkSharedPreferences();
    }

    public void jumpToDashboard(FirebaseUser currentUser) {
//        model.getUser(userID, (User user) -> {
////            if (user == null) {
////                Toast.makeText(this, "failed to jump to Dashboard.", Toast.LENGTH_LONG).show();
////                return;
////            }
//
//
//        });
        Intent intent = new Intent(this,Dashboard.class);//注意dashboard的activity名称
        intent.putExtra("user", currentUser);// 将 user 对象附加到 Intent，这样在目标页面的账户就是登录页面的账户
        startActivity(intent);
    }

    public void jumpToQuestionsActivity(FirebaseUser currentUser) {
        Intent intent = new Intent(this,StartActivity.class);
        Toast.makeText(this, "Let's get started! We will calculate your current carbon footprint " +
                "based on your lifestyle. You only need to do this once.", Toast.LENGTH_LONG).show();//注意dashboard的activity名称
        intent.putExtra("user", currentUser);// 将 user 对象附加到 Intent，这样在目标页面的账户就是登录页面的账户
        startActivity(intent);
    }



    public void failedToLogin() {
        Toast.makeText(this, "failed to login.", Toast.LENGTH_LONG).show();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (((LoginPresenter) presenter).model.isUserLoggedIn()) {
//            this.jumpToEcotrackerActivity();
//            finish();
//        }
//    }

    private void checkSharedPreferences() {
        boolean remember = preferences.getBoolean("remember", false);
        Log.d("SharedPreferences", "Remember: " + remember);
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        username_input.setText(email);
        password_input.setText(password);
        remember_me_checkbox.setChecked(remember);
    }

    @Override
    public void showProgressBar() {
        progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void jumpToEcotrackerActivity() {
        Intent intent = new Intent(MainActivity.this, ActivityMainLayout.class);
        startActivity(intent);
        finish();
    }

    public void jumpToSignUpActivity() {
        Log.d("MainActivity", "Jumping to SignUpActivity...");
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void jumpToForgotPasswordActivity() {
        Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

}