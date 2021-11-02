package com.group.medexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private TextView registerNowBtn;
    private ProgressBar progressBar;
    private TextView errorText;
    private ImageButton backBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerNowBtn = (TextView) findViewById(R.id.loginNowBtn);
        progressBar = (ProgressBar) findViewById(R.id.registerPageProgerssBar);
        errorText = (TextView)  findViewById(R.id.loginErrorText);

        backBtn = (ImageButton) findViewById(R.id.backBtn);

        auth = FirebaseAuth.getInstance();

        setLoginBtn();
        setRegisterNowBtn();
        setBackBtn();


    }


    private void setLoginBtn(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFieldAreEmptyOrNot();
            }
        });
    }


    private void checkFieldAreEmptyOrNot(){
        String email = emailEditText.getText().toString().trim();
        String pass = passwordEditText.getText().toString().trim();



        if(TextUtils.isEmpty(email)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Add email.");
        }
        else if (TextUtils.isEmpty(pass)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Add password.");
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            signInWithEmail(email, pass);
        }
    }


    private void signInWithEmail(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            sendToMainActivity();
                        }

                        else  {
                            String errorMessage = task.getException().getMessage();
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText(errorMessage);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }


    private void setRegisterNowBtn(){
        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRegisterActivity();
            }
        });
    }




    private void sendToRegisterActivity(){
        Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


    private void sendToMainActivity(){
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setBackBtn(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToMainActivity();
            }
        });
    }

}