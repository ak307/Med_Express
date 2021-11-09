package com.group.medexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
                firebaseFirestore
                        .collection("Products")
                        .document(getProductDocID())
                        .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DeleteProductActivity.this, "delete successful", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    }
                });
            }
        });
    }


    private String getProductDocID(){
        String productDocID = getIntent().getStringExtra("productDocID");

        return productDocID;
    }

}