package com.group.medexpress.ListviewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.group.medexpress.CreateProductActivity;
import com.group.medexpress.Datamodels.ProductsDataModel;
import com.group.medexpress.DeleteProductActivity;
import com.group.medexpress.DetailedProductActivity;
import com.group.medexpress.LogInActivity;
import com.group.medexpress.MainActivity;
import com.group.medexpress.R;
import com.group.medexpress.RegisterActivity;
import com.group.medexpress.UpdateProductActivity;
import com.group.medexpress.Utils.Checker;

import java.util.ArrayList;

public class ProductsListviewAdapter extends BaseAdapter {
    private Context context;
    private  ArrayList<ProductsDataModel> list;
    private Checker checker;

    public ProductsListviewAdapter(Context context, ArrayList<ProductsDataModel> list) {
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



        Uri imageUrl = Uri.parse(list.get(position).getProductImage());
        productName.setText(list.get(position).getProductName());
        price.setText("$ " + list.get(position).getProductPrice());

        if (!imageUrl.equals("")) {
            Glide.with(context.getApplicationContext())
                    .load(imageUrl)
                    .into(productImg);
        }


        if (!checker.isAdmin()){
            updateBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }

        setUpdateBtn(updateBtn, position);
        setDeleteBtn(deleteBtn, position);


        return convertView;
    }


    private void setUpdateBtn(ImageButton updateBtn, int position){
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra("productDocID", list.get(position).getProductDocID());
                intent.putExtra("productID", list.get(position).getProductID());
                intent.putExtra("productName", list.get(position).getProductName());
                intent.putExtra("productPrice", list.get(position).getProductPrice());
                intent.putExtra("productDesc", list.get(position).getProductDesc());
                intent.putExtra("productImg", list.get(position).getProductImage());
                context.startActivity(intent);
            }
        });
    }


    private void setDeleteBtn(ImageButton deleteBtn, int position){
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeleteProductActivity.class);
                intent.putExtra("productDocID", list.get(position).getProductDocID());

                context.startActivity(intent);
            }
        });
    }

}
