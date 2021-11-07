package com.group.medexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class DeleteProductActivity extends AppCompatActivity {
    private ImageButton deleteBackBtn;
    private ImageButton deleteCancelBtn;
    private ImageButton deleteYesBtn;

    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        deleteBackBtn = (ImageButton) findViewById(R.id.deleteBackBtn);
        deleteCancelBtn = (ImageButton) findViewById(R.id.deleteCancelBtn);
        deleteYesBtn = (ImageButton) findViewById(R.id.deleteYesBtn);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        setDeleteBackBtn();
        setDeleteCancelBtn();
        setDeleteYesBtn();
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

    private void setDeleteYesBtn(){
        deleteYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Products").document().delete();
                startActivity(new Intent(DeleteProductActivity.this, MainActivity.class));
            }
        });
    }

}