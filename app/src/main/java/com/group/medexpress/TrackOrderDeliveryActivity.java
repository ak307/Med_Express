package com.group.medexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TrackOrderDeliveryActivity extends AppCompatActivity {

    private ImageButton trackOrderBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order_delivery);

        trackOrderBackBtn = (ImageButton) findViewById(R.id.trackOrderBackBtn);

        setTrackOrderBackBtn();
    }

    private void setTrackOrderBackBtn(){
        trackOrderBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}