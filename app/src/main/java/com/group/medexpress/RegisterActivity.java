package com.group.medexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group.medexpress.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private EditText passwordEditText;
    private TextView loginNowBtn;
    private Button createBtn;
    private FirebaseAuth Auth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    private TextView errorText;
    private Utils utils;
    private ArrayList<String> favorite_product;
    private ArrayList<String> shop_cart;
    private ArrayList<String> payment_history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) findViewById(R.id.registerEmailEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        passwordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        loginNowBtn = (TextView) findViewById(R.id.loginNowBtn);
        createBtn = (Button) findViewById(R.id.createBtn);
        progressBar = (ProgressBar) findViewById(R.id.registerPageProgerssBar);
        errorText = (TextView) findViewById(R.id.errorText);

        Auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        utils = new Utils();
        favorite_product = new ArrayList<>();
        shop_cart = new ArrayList<>();
        payment_history = new ArrayList<>();

        setLoginNowBtn();
        setCreateBtn();

    }


    private void setCreateBtn(){
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorText.setVisibility(View.INVISIBLE);
                checkAllFieldAreValidOrNot();
            }
        });
    }

    private void checkAllFieldAreValidOrNot(){
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();


        if(TextUtils.isEmpty(firstName)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Add first name.");
        }
        else if (TextUtils.isEmpty(lastName)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Add last name.");
        }

        else if(TextUtils.isEmpty(phone)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Add phone number.");
        }
        else if(TextUtils.isEmpty(email)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Add email.");
        }
        else if(TextUtils.isEmpty(address)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Add address.");
        }
        else if(TextUtils.isEmpty(password)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Add password.");
        }

        else {
            progressBar.setVisibility(View.VISIBLE);
            registerWithEmail(firstName, lastName, phone, email, address, password);
        }

    }


    private void registerWithEmail(String firstName, String lastname,
                                   String phone, String email,
                                   String address, String password){
        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    addProfileInfoToFirestore(firstName, lastname, phone, email, address, password);
                    sendToMainActivity();
                }
                else {
                    String error = task.getException().getMessage();
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(error);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }



    private void addProfileInfoToFirestore(String firstName, String lastname,
                                           String phone, String email,
                                           String address, String password){
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("first_name", firstName);
        dataMap.put("last_name", lastname);
        dataMap.put("phone", phone);
        dataMap.put("email", email);
        dataMap.put("address", address);
        dataMap.put("favorite_products", favorite_product);
        dataMap.put("profile_image", "");
        dataMap.put("uid", utils.getUserID());
        dataMap.put("shop_cart", shop_cart);
        dataMap.put("payment_history", payment_history);


        firebaseFirestore.collection("customers").document(utils.getUserID())
                .set(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error = e.getMessage();
                Toast.makeText(RegisterActivity.this, "Register unsuccessful: " + error, Toast.LENGTH_SHORT).show();

                // for test only (no need in production)
                progressBar.setVisibility(View.INVISIBLE);

            }
        });

    }


    private void setLoginNowBtn(){
        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }




    private void sendToMainActivity(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }




}