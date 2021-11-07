package com.group.medexpress;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group.medexpress.Datamodels.ProductsDataModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateProductActivity extends AppCompatActivity {
    private ImageButton cancelBtn;
    private ImageButton updateBtn;
    private ImageView productImgView;
    private EditText productIDField;
    private EditText productNameField;
    private EditText productPriceField;
    private EditText productDescField;
    private TextView error;
    private Uri uri;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> categoriesList;
    private ProgressBar progressBar;
    private Spinner dropDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);


        cancelBtn = (ImageButton) findViewById(R.id.createProductBackBtn);
        updateBtn = (ImageButton) findViewById(R.id.updateBtn);
        productImgView = (ImageView) findViewById(R.id.createProductImgView);
        productIDField = (EditText) findViewById(R.id.createProductProductID);
        productNameField = (EditText) findViewById(R.id.createProductProductName);
        productPriceField = (EditText) findViewById(R.id.createProductProductPrice);
        productDescField = (EditText) findViewById(R.id.createProductProductDescription);
        error = (TextView) findViewById(R.id.createProductErrorText);
        progressBar = (ProgressBar) findViewById(R.id.createProductProgressBar);
        dropDown = (Spinner) findViewById(R.id.createProductDropDown);


        error.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        String[] items = new String[]{"surgical ", "alcohol", "test kit"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropDown.setAdapter(adapter);


        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        categoriesList = new ArrayList<>();


        setCancelBtn();
        setUpdateBtn();
        setProductImgView();


    }

    private void setCancelBtn(){
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void setUpdateBtn(){
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productID = productIDField.getText().toString().trim();
                String productName = productNameField.getText().toString().trim();
                String price = productPriceField.getText().toString().trim();
                String desc = productDescField.getText().toString().trim();
                String dropdown = dropDown.getSelectedItem().toString();


                if(TextUtils.isEmpty(productID)){
                    error.setVisibility(View.VISIBLE);
                    error.setText("Add Product ID");
                }
                else if (TextUtils.isEmpty(productName)){
                    error.setVisibility(View.VISIBLE);
                    error.setText("Add Product Name");
                }
                else if (TextUtils.isEmpty(price)){
                    error.setVisibility(View.VISIBLE);
                    error.setText("Add Product's price");
                }
                else if (price.contains("$")){
                    error.setVisibility(View.VISIBLE);
                    error.setText("Please do not add $");
                }
                else if (TextUtils.isEmpty(dropdown)){
                    error.setVisibility(View.VISIBLE);
                    error.setText("Please select categories");
                }
                else if (TextUtils.isEmpty(desc)){
                    error.setVisibility(View.VISIBLE);
                    error.setText("Add Product Description");
                }
                else if (uri == null || uri.equals("")){
                    error.setVisibility(View.VISIBLE);
                    error.setText("Add Product Image");
                }
                else {
                    error.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    setImageToFirebaseStorage(productID, productName, price, desc, dropdown);
                }
            }
        });
    }

    private void test(){
        //can delete
    }



    private void setImageToFirebaseStorage(String productID,
                                           String productName, String price,
                                           String desc, String dropdown){

        String timestamp = "" + System.currentTimeMillis();
        String  filePathAndName  = "Product_image/" + "img_" + timestamp;


        StorageReference storageReference = firebaseStorage.getReference(filePathAndName);

        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getImageUrl(storageReference, productID, productName, price, desc, dropdown);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void getImageUrl(StorageReference image_path, String productID,
                             String productName, String price,
                             String desc, String dropdown){
        image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                addProductToFirebase(uri.toString(), productID, productName, price, desc, dropdown);
            }
        });

    }


    private void addProductToFirebase(String Uri, String productID,
                                      String productName, String price,
                                      String desc, String dropdown){
        categoriesList.add(dropdown);

        String docId = firebaseFirestore.collection("Products")
                .document().getId();


        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", docId);
        dataMap.put("productID", productID);
        dataMap.put("name", productName);
        dataMap.put("price", price);
        dataMap.put("description", desc);
        dataMap.put("image_url", Uri);
        dataMap.put("Category", categoriesList);

//        firebaseStorage.getReference().addValueEventListener(new ValueEventListener(){
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        })

//        firebaseFirestore.collection("Products")
//                .document(docId)
//                .set(dataMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            onBackPressed();
//                        }
//                        else
//                            progressBar.setVisibility(View.INVISIBLE);
//
//                    }
//                });

    }


    private void setProductImgView(){
        productImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                if(ContextCompat.checkSelfPermission(UpdateProductActivity.this, permission)
                        != PackageManager.PERMISSION_GRANTED){

                    String[] permissions = {permission};
                    ActivityCompat.requestPermissions(UpdateProductActivity.this, permissions, 1);
                }
                else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(UpdateProductActivity.this);

                }
            }
        });
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();

                Glide.with(UpdateProductActivity.this.getApplicationContext())
                        .load(uri)
                        .into(productImgView);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(UpdateProductActivity.this, (CharSequence) error, Toast.LENGTH_LONG).show();
            }
        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (requestCode == 1) {
                boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (readExternalStorage) {

                }
            }
        }
    }


}

