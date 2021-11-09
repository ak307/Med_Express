package com.group.medexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DetailedProductActivity extends AppCompatActivity {

    private ImageButton detailedBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_product);

        detailedBackBtn = (ImageButton) findViewById(R.id.detailedBackBtn);

        setDetailedBackBtn();

    }

    private void setDetailedBackBtn(){
        detailedBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}