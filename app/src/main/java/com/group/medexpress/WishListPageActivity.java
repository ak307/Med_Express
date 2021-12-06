package com.group.medexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group.medexpress.Datamodels.ProductsDataModel;
import com.group.medexpress.ListviewAdapters.WishListListViewAdapter;
import com.group.medexpress.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class WishListPageActivity extends AppCompatActivity {

    private ListView WishListListView;
    private WishListListViewAdapter Adapter;
    private ArrayList<ProductsDataModel> arraylist;
    private FirebaseFirestore firebaseFirestore;
    private Utils utils;
    private ImageButton wishlistBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_page);

        WishListListView = (ListView) findViewById(R.id.WishListListView);
        arraylist = new ArrayList<>();
        utils = new Utils();
        wishlistBackBtn = (ImageButton) findViewById(R.id.wishlistBackBtn);

        firebaseFirestore = FirebaseFirestore.getInstance();



        firebaseFirestore.collection("customers")
                .document(utils.getUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshots = task.getResult();
                            if (documentSnapshots.exists()) {
                                ArrayList<String> favorite_product = (ArrayList<String>) documentSnapshots.get("favorite_products");
                                if(!favorite_product.isEmpty())
                                    GetWishListProducts(favorite_product);
                            }
                        }
                    }
                });

        wishlistBackBtn();

    }

    private void wishlistBackBtn(){
        wishlistBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void GetWishListProducts(ArrayList<String> favorite_product){
        firebaseFirestore.collection("Products")
                .whereIn("id", favorite_product)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String productName = document.getString("name");
                        String price = document.getString("price");
                        String quantity = document.getString("stock");
                        String productDocID = document.getString("id");
                        String productID = document.getString("productID");
                        String productImg = document.getString("image_url");
                        String description = document.getString("description");


                        arraylist.add(new ProductsDataModel(productDocID, productID, productName, price, quantity, productImg, description));
                    }
                    setAdapter();
                }
            }
        });
    }

    public void setAdapter(){
        Adapter = new WishListListViewAdapter(WishListPageActivity.this, arraylist);
        WishListListView.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();
    }

}