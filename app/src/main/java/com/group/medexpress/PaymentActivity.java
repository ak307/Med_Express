package com.group.medexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }

    private String getTotalCost(){
        String totalCost = getIntent().getStringExtra("totalCost");
        return totalCost;
    }
}