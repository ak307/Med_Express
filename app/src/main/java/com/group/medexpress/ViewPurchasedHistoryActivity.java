package com.group.medexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ViewPurchasedHistoryActivity extends AppCompatActivity {

    private ImageButton purchaseHistoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_purchased_history);

        purchaseHistoryBtn = (ImageButton) findViewById(R.id.purchaseHistoryBtn);

    }

    private void setPurchaseHistoryBtn(){
        purchaseHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}