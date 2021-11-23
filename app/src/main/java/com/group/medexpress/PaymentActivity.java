package com.group.medexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {

    private TextView totalAmountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        totalAmountText = (TextView) findViewById(R.id.totalAmountText);

        Toast.makeText(PaymentActivity.this, ": " + getTotalCost(), Toast.LENGTH_SHORT).show();
    }

    private String getTotalCost(){
        String totalCost = getIntent().getStringExtra("totalCost");
        return totalCost;
    }



}