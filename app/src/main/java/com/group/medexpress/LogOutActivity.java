package com.group.medexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LogOutActivity extends AppCompatActivity {

    private ImageButton logoutBackBtn;
    private ImageButton logoutCancelBtn;
    private ImageButton logoutYesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        logoutBackBtn = (ImageButton) findViewById(R.id.logoutBackBtn);
        logoutCancelBtn = (ImageButton)  findViewById(R.id.logoutCancelBtn);
        logoutYesBtn = (ImageButton)  findViewById(R.id.logoutYesBtn);

        setBackBtn();
        setCancelBtn();
        setYesBtn();

    }

    private void setBackBtn(){
        logoutBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });
    }

    private void setCancelBtn(){
        logoutCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });
    }

    private void setYesBtn(){
        logoutYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { sendToLoginActivity(); }
        });
    }

    private void sendToLoginActivity(){
        Intent intent = new Intent(LogOutActivity.this, LogInActivity.class);
        startActivity(intent);
    }

}