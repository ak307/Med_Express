package com.group.medexpress.ListviewAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group.medexpress.Datamodels.ProductsDataModel;
import com.group.medexpress.DeleteProductActivity;
import com.group.medexpress.R;
import com.group.medexpress.UpdateProductActivity;
import com.group.medexpress.Utils.Checker;
import com.group.medexpress.Utils.Utils;

import java.util.ArrayList;

public class WishListListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProductsDataModel> list;
    private Checker checker;
    private FirebaseFirestore firebaseFirestore;
    private Utils utils;

    public WishListListViewAdapter(Context context, ArrayList<ProductsDataModel> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_list_layout, parent, false);

        }


        ImageView productImg = (ImageView) convertView.findViewById(R.id.productImageView);
        TextView productName = (TextView) convertView.findViewById(R.id.productNameTextView);
        TextView price = (TextView) convertView.findViewById(R.id.productPriceTextview);
        ImageButton updateBtn = (ImageButton) convertView.findViewById(R.id.updateBtn);
        ImageButton deleteBtn = (ImageButton) convertView.findViewById(R.id.deleteBtn);

        checker = new Checker(context);
        utils = new Utils();
        firebaseFirestore = FirebaseFirestore.getInstance();



        Uri imageUrl = Uri.parse(list.get(position).getProductImage());
        productName.setText(list.get(position).getProductName());
        price.setText("$ " + list.get(position).getProductPrice());

        if (!imageUrl.equals("")) {
            Glide.with(context.getApplicationContext())
                    .load(imageUrl)
                    .into(productImg);
        }

//        setDeleteBtn(deleteBtn, position);
        updateBtn.setVisibility(View.INVISIBLE);
        setDeleteBtn(deleteBtn, position);

        return convertView;
    }

    private void setDeleteBtn(ImageButton deleteBtn, int position){
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String docID = list.get(position).getProductDocID();
                firebaseFirestore.collection("customers")
                        .document(utils.getUserID())
                        .update("favorite_products", FieldValue.arrayRemove(docID))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    notifyDataSetChanged();
                                    // success
                                }
                            }
                        });
            }
        });
    }

//    private void setDeleteBtn(ImageButton deleteBtn, int position){
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, DeleteProductActivity.class);
//                intent.putExtra("productDocID", list.get(position).getProductDocID());
//
//                context.startActivity(intent);
//            }
//        });
//    }
}
