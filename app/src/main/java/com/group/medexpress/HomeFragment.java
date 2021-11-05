package com.group.medexpress;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group.medexpress.Datamodels.ProductsDataModel;
import com.group.medexpress.ListviewAdapters.ProductsListviewAdapter;
import com.group.medexpress.Utils.Checker;
import com.group.medexpress.Utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private EditText searchInputEditText;
    private ImageButton searchBtn;
    private ImageButton filterBtn;
    private ImageButton createBtn;
    private ListView listView;
    private FirebaseFirestore firebaseFirestore;
    private Utils utils;
    private ArrayList<ProductsDataModel> productsDataModelArrayList;
    private Checker checker;
    private AdminInfoDatabaseHelper adminInfoDatabaseHelper;

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
        filterBtn = (ImageButton) view.findViewById(R.id.filterBtn);
        createBtn = (ImageButton) view.findViewById(R.id.createProductBtn);

        listView = (ListView) view.findViewById(R.id.listview);


        firebaseFirestore = FirebaseFirestore.getInstance();
        adminInfoDatabaseHelper = new AdminInfoDatabaseHelper(getContext()) ;
        utils = new Utils();
        productsDataModelArrayList = new ArrayList<>();


        checker = new Checker(getContext());

        if (!checker.isAdmin())
           createBtn.setVisibility(View.GONE);

        setSearchBtn();
        setFilterBtn();
        retrieveProductsFromFirebase();
        setSearchBox();
        checkAdmin();
        setCreateBtn();

        return view;
    }

    private void setCreateBtn(){
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateProductActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkAdmin(){
        firebaseFirestore
                .collection("Admins")
                .whereEqualTo("id", utils.getUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            QuerySnapshot documentSnapshots = task.getResult();
                            List<DocumentSnapshot> docs = documentSnapshots.getDocuments();
                            if (!docs.isEmpty()) {
                                for (DocumentSnapshot doc : docs){
                                    String id = doc.getString("id");
                                    adminInfoDatabaseHelper.addData(id);
                                }
                            }
                        }
                    }
                });

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


    private void setFilterBtn(){
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FilterActivity.class);
                startActivityForResult(intent, 101);
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
        firebaseFirestore
                .collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            productsDataModelArrayList.clear();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101){
            ArrayList<String> selectedChips = data.getStringArrayListExtra("selectedChipData");

            if (selectedChips != null && selectedChips.size() != 0 && !selectedChips.get(0).equals("~")){
                retrieveProductsWithFilter(selectedChips);
            }
        }
    }


    private void retrieveProductsWithFilter(ArrayList<String> dataList){
        productsDataModelArrayList.clear();
        firebaseFirestore
                .collection("Products")
                .whereArrayContainsAny("Category", dataList)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                           List<DocumentSnapshot> docs = task.getResult().getDocuments();
                           for (DocumentSnapshot doc: docs){
                               String productName = doc.getString("name");
                               String price = doc.getString("price");
                               String quantity = doc.getString("stock");
                               String productID = doc.getString("id");
                               String productImg = doc.getString("image_url");


                               productsDataModelArrayList.add(new ProductsDataModel(productID, productName, price, quantity, productImg));

                           }

                           setListViewAdapter();
                        }
                    }
                });

    }




}