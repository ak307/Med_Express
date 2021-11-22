package com.group.medexpress;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group.medexpress.Datamodels.ProductsDataModel;
import com.group.medexpress.ListviewAdapters.ProductsListviewAdapter;
import com.group.medexpress.SQLite.AdminInfoDatabaseHelper;
import com.group.medexpress.Utils.Checker;
import com.group.medexpress.Utils.Utils;

import org.jetbrains.annotations.NotNull;

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

        setDetailedMove();


//        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(getContext());
//
//        cartDatabaseHelper.deleteAll();
//
//        cartDatabaseHelper.addProduct("0000000000");
//        cartDatabaseHelper.addProduct("111111");
//        cartDatabaseHelper.addProduct("222222");
//
//        getClassCode(cartDatabaseHelper);

        return view;
    }



//    private ArrayList<String> getClassCode(CartDatabaseHelper cartDatabaseHelper){
//        ArrayList<String> classCodeList = new ArrayList<>();
//        Cursor data = cartDatabaseHelper.getData();
//
//        if (data.moveToFirst()) {
//            do {
//                String code = data.getString(1);
//                classCodeList.add(code);
//
//            } while (data.moveToNext());
//        }
//
//        Toast.makeText(getContext(), "d" + classCodeList.get(0), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "d" + classCodeList.get(1), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "d" + classCodeList.get(2), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "d" + classCodeList.size(), Toast.LENGTH_SHORT).show();
//
//        return classCodeList;
//    }



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
                                String productDocID = document.getString("id");
                                String productID = document.getString("productID");
                                String productImg = document.getString("image_url");
                                String description = document.getString("description");


                                productsDataModelArrayList.add(new ProductsDataModel(productDocID, productID, productName, price, quantity, productImg, description));
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
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            List<DocumentSnapshot> data = value.getDocuments();
                            productsDataModelArrayList.clear();
                            for (DocumentSnapshot document : data) {
                                String productName = document.getString("name");
                                String price = document.getString("price");
                                String quantity = document.getString("stock");
                                String productDocID = document.getString("id");
                                String productID = document.getString("productID");
                                String productImg = document.getString("image_url");
                                String description = document.getString("description");




                                productsDataModelArrayList.add(new ProductsDataModel(productDocID, productID, productName, price, quantity, productImg, description));

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
                               String productDocID = doc.getString("id");
                               String productImg = doc.getString("image_url");
                               String productID = doc.getString("productID");
                               String description = doc.getString("description");



                               productsDataModelArrayList.add(new ProductsDataModel(productDocID, productID, productName, price, quantity, productImg, description));

                           }

                           setListViewAdapter();
                        }
                    }
                });

    }

    private void setDetailedMove(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendToDetailedProductActivity(position);
            }
        });
    }


    private void sendToDetailedProductActivity(int position){
        Intent intent = new Intent(getContext(), DetailedProductActivity.class);
        intent.putExtra("productDocID", productsDataModelArrayList.get(position).getProductDocID());
        intent.putExtra("productID", productsDataModelArrayList.get(position).getProductID());
        intent.putExtra("productName", productsDataModelArrayList.get(position).getProductName());
        intent.putExtra("productPrice", productsDataModelArrayList.get(position).getProductPrice());
        intent.putExtra("productDesc", productsDataModelArrayList.get(position).getProductDesc());
        intent.putExtra("productImg", productsDataModelArrayList.get(position).getProductImage());
        startActivity(intent);
    }


}