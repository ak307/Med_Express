package com.group.medexpress;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group.medexpress.Datamodels.ProductsDataModel;
import com.group.medexpress.ListviewAdapters.ProductsListviewAdapter;
import com.group.medexpress.Utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private EditText searchInputEditText;
    private ImageButton searchBtn;
    private ListView listView;
    private FirebaseFirestore firebaseFirestore;
    private Utils utils;
    private ArrayList<ProductsDataModel> productsDataModelArrayList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentV
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchInputEditText = (EditText) view.findViewById(R.id.searchEditText);
        searchBtn = (ImageButton) view.findViewById(R.id.searchBtn);
        listView = (ListView) view.findViewById(R.id.listview);

        firebaseFirestore = FirebaseFirestore.getInstance();
        utils = new Utils();
        productsDataModelArrayList = new ArrayList<>();


        setSearchBtn();
        retrieveProductsFromFirebase();
        setSearchBox();

        return view;
    }


    private void setSearchBtn(){
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = searchInputEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(inputText)){
                    retrieveCustomerSearch(inputText);
                }
            }
        });
    }


    private void retrieveCustomerSearch(String input){
        productsDataModelArrayList.clear();
        firebaseFirestore
                .collection("Products")
                .whereGreaterThanOrEqualTo("name",  input)
                .whereLessThan("name", input + "~")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String productName = document.getString("name");
                                String price = document.getString("price");
                                String quantity = document.getString("stock");
                                String productID = document.getString("id");
                                String productImg = document.getString("image_url");


                                productsDataModelArrayList.add(new ProductsDataModel(productID, productName, price, quantity, productImg));

                            }

                            setListViewAdapter();
                        }
                    }
                });
    }


    private void setSearchBox(){
        searchInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchInputEditText.getText().toString().trim().equals("")){
                    productsDataModelArrayList.clear();
                    retrieveProductsFromFirebase();
                }
                else {

                }

            }
        });
    }




    private void retrieveProductsFromFirebase(){
        productsDataModelArrayList.clear();

        firebaseFirestore
                .collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               String productName = document.getString("name");
                               String price = document.getString("price");
                               String quantity = document.getString("stock");
                               String productID = document.getString("id");
                               String productImg = document.getString("image_url");


                               productsDataModelArrayList.add(new ProductsDataModel(productID, productName, price, quantity, productImg));

                            }

                            setListViewAdapter();
                        }
                    }
                });
    }


    private void setListViewAdapter(){
        ProductsListviewAdapter adapter = new ProductsListviewAdapter(getContext(), productsDataModelArrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }




}