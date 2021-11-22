package com.group.medexpress;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
import com.group.medexpress.Datamodels.cartDataModel;
import com.group.medexpress.ListviewAdapters.CartListViewAdapter;
import com.group.medexpress.ListviewAdapters.WishListListViewAdapter;
import com.group.medexpress.Utils.Utils;

import java.util.ArrayList;

public class shopCartActivity extends AppCompatActivity {

    private ListView cartListView;
    private CartListViewAdapter Adapter;
    private ArrayList<cartDataModel> arraylist;
    private FirebaseFirestore firebaseFirestore;
    private Utils utils;
    private TextView totalCost;
    private double sum = 0;
    private ImageButton cartBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        totalCost = (TextView) findViewById(R.id.totalCost);
        cartListView = (ListView) findViewById(R.id.cartListView);
        totalCost = (TextView) findViewById(R.id.totalCost);
        cartBackBtn = (ImageButton) findViewById(R.id.cartBackBtn);

        arraylist = new ArrayList<>();
        utils = new Utils();

        firebaseFirestore = firebaseFirestore.getInstance();

        setCartBackBtn();


        if (utils.getUserID() != null)
            getCartProductIDs();
        else
            Toast.makeText(shopCartActivity.this, "Please sign in first.", Toast.LENGTH_SHORT).show();

    }


    public void getCartProductIDs(){
        firebaseFirestore.collection("customers")
                .document(utils.getUserID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.exists()){
                                ArrayList<String> shop_cart_product_id = (ArrayList<String>) value.get("shop_cart");
                                GetCartProducts(shop_cart_product_id);
                            }

                    }
                });

    }


    public void GetCartProducts(ArrayList<String> shop_cart_product_id) {
        if (shop_cart_product_id != null && shop_cart_product_id.size() > 0) {
            arraylist.clear();
            firebaseFirestore.collection("Products")
                    .whereIn("id", shop_cart_product_id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String productName = document.getString("name");
                                    String price = document.getString("price");
                                    String productDocID = document.getString("id");
                                    String productID = document.getString("productID");
                                    String productImg = document.getString("image_url");
                                    String description = document.getString("description");
                                    String productQty = "1";


                                    arraylist.add(new cartDataModel
                                            (productDocID, productID, productName, price, productQty, productImg, description, productQty));
                                }
                                setAdapter();

                            }
                        }
                    });
        }
    }



    private void setCartBackBtn(){
        cartBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void setAdapter() {
        Adapter = new CartListViewAdapter(shopCartActivity.this, arraylist, totalCost);
        cartListView.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();
    }


}