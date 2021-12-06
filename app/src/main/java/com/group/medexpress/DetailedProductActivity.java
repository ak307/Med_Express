package com.group.medexpress;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group.medexpress.Datamodels.ProductsDataModel;
import com.group.medexpress.Utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailedProductActivity extends AppCompatActivity {
    private ImageButton returnBtn;
    private ImageView productImgView;
    private TextView productIDField;
    private TextView productNameField;
    private TextView productPriceField;
    private TextView productDescField;
    private TextView error;
    private Uri uri;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> categoriesList;
    private ProgressBar progressBar;
    private Spinner dropDown;

    private Button addWishlistBtn;
    private Button addCartBtn;
    private Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_product);


        returnBtn = (ImageButton) findViewById(R.id.detailedProductReturnBtn);
        productImgView = (ImageView) findViewById(R.id.detailedProductImgView);
        productIDField = (TextView) findViewById(R.id.detailedProductProductID);
        productNameField = (TextView) findViewById(R.id.detailedProductProductName);
        productPriceField = (TextView) findViewById(R.id.detailedProductProductPrice);
        productDescField = (TextView) findViewById(R.id.detailedProductProductDescription);
        error = (TextView) findViewById(R.id.detailedProductErrorText);
        progressBar = (ProgressBar) findViewById(R.id.detailedProductProgressBar);
        dropDown = (Spinner) findViewById(R.id.detailedProductDropDown);

        addWishlistBtn = (Button) findViewById(R.id.addWishlistBtn);
        addCartBtn = (Button) findViewById(R.id.addCartBtn);
        utils = new Utils();

        error.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);


        String[] items = new String[]{"surgical ", "alcohol", "test kit"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropDown.setAdapter(adapter);


        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        categoriesList = new ArrayList<>();


        setReturnBtn();
//        setProductImgView();
        setOldProductFields();

        setAddWishListBtn();
        setAddCartBtn();

    }
    
    private boolean isGuestUser(){
        return utils.getUserID() == null;
    }


    private void setOldProductFields(){
        productIDField.setText(getProductID());

        if (getProductImg() != null && !getProductImg().equals("")) {
            Glide.with(getApplicationContext())
                    .load(getProductImg())
                    .into(productImgView);
        }
        else
            Toast.makeText(DetailedProductActivity.this, "product image is null", Toast.LENGTH_SHORT).show();

        productNameField.setText(getProductName());
        productPriceField.setText(getProductPrice());
        productDescField.setText(getProductDesc());

    }


    private void setReturnBtn(){
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void test(){
        //can delete
    }


//    private void setProductImgView(){
//        productImgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
//                if(ContextCompat.checkSelfPermission(DetailedProductActivity.this, permission)
//                        != PackageManager.PERMISSION_GRANTED){
//
//                    String[] permissions = {permission};
//                    ActivityCompat.requestPermissions(DetailedProductActivity.this, permissions, 1);
//                }
//                else {
//                    CropImage.activity()
//                            .setGuidelines(CropImageView.Guidelines.ON)
//                            .start(DetailedProductActivity.this);
//
//                }
//            }
//        });
//    }

    public void setAddWishListBtn(){
        addWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isGuestUser()){
                    firebaseFirestore.collection("customers")
                            .document(utils.getUserID())
                            .update("favorite_products", FieldValue.arrayUnion(getProductDocID()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(DetailedProductActivity.this, "Add to Wishlist successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else
                    Toast.makeText(DetailedProductActivity.this, "Please Sign in first.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setAddCartBtn() {
        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isGuestUser()){
                    firebaseFirestore.collection("customers")
                            .document(utils.getUserID())
                            .update("shop_cart", FieldValue.arrayUnion(getProductDocID()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(DetailedProductActivity.this, "Add to Cart successfully", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                }
                else
                    Toast.makeText(DetailedProductActivity.this, "Please Sign in first.", Toast.LENGTH_SHORT).show();

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

                Glide.with(DetailedProductActivity.this.getApplicationContext())
                        .load(uri)
                        .into(productImgView);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(DetailedProductActivity.this, (CharSequence) error, Toast.LENGTH_LONG).show();
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


    private String getProductDocID(){
        String productDocID = getIntent().getStringExtra("productDocID");
        return productDocID;
    }


    private String getProductID(){
        String productID = getIntent().getStringExtra("productID");
        return productID;
    }

    private String getProductName(){
        String productName = getIntent().getStringExtra("productName");
        return productName;
    }

    private String getProductPrice(){
        String productPrice = getIntent().getStringExtra("productPrice");
        return productPrice;
    }

    private String getProductDesc(){
        String productDesc = getIntent().getStringExtra("productDesc");
        return productDesc;
    }

    private String getProductImg(){
        String productImg = getIntent().getStringExtra("productImg");
        return productImg;
    }

}

