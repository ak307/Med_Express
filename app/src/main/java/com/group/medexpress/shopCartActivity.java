package com.group.medexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        totalCost = (TextView) findViewById(R.id.totalCost);
        cartListView = (ListView) findViewById(R.id.cartListView);
        arraylist = new ArrayList<>();
        utils = new Utils();

        firebaseFirestore = firebaseFirestore.getInstance();

        firebaseFirestore.collection("customers")
                .document(utils.getUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshots = task.getResult();
                            if (documentSnapshots.exists()) {
                                ArrayList<String> shop_cart = (ArrayList<String>) documentSnapshots.get("shop_cart");
                                GetCartProducts(shop_cart);
                            }
                        }
                    }
                });
    }

    public void GetCartProducts(ArrayList<String> shop_cart) {
        firebaseFirestore.collection("Products")
                .whereIn("id", shop_cart)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String productName = document.getString("name");
                                String price = document.getString("price");
                                String quantity = document.getString("stock");
                                String productDocID = document.getString("id");
                                String productID = document.getString("productID");
                                String productImg = document.getString("image_url");
                                String description = document.getString("description");
                                String productQty = "1";


                                arraylist.add(new cartDataModel
                                        (productDocID, productID, productName, price, quantity, productImg, description, productQty));
                            }
                            setAdapter();
                        }
                    }
                });
    }

    public void setAdapter() {
        Adapter = new CartListViewAdapter(shopCartActivity.this, arraylist);
        cartListView.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();
    }


}