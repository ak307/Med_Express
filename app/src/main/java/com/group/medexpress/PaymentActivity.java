package com.group.medexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {

    private TextView totalAmountText;
    private Button paymentBtn;
    private ImageButton paymentBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentBtn = (Button) findViewById(R.id.paymentBtn);
        paymentBackBtn = (ImageButton) findViewById(R.id.paymentBackBtn);

        totalAmountText = (TextView) findViewById(R.id.totalAmountText);
        totalAmountText.setText(getTotalCost());

        setPaymentBackBtn();
        setPaymentBtn();
    }

    private String getTotalCost(){
        String totalCost = getIntent().getStringExtra("totalCost");
        return totalCost;
    }

    private void setPaymentBackBtn(){
        paymentBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setPaymentBtn(){
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                Toast.makeText(PaymentActivity.this, "Payment Successful" , Toast.LENGTH_SHORT).show();
            }
        });
    }


}