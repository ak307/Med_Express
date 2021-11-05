package com.group.medexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DeleteProductActivity extends AppCompatActivity {
    private ImageButton deleteBackBtn;
    private ImageButton deleteCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        deleteBackBtn = (ImageButton) findViewById(R.id.deleteBackBtn);
        deleteCancelBtn = (ImageButton) findViewById(R.id.deleteCancelBtn);


        setDeleteBackBtn();
        setDeleteCancelBtn();
    }

    private void setDeleteBackBtn(){
        deleteBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setDeleteCancelBtn(){
        deleteCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}