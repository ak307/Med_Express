package com.group.medexpress.ListviewAdapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.group.medexpress.Datamodels.ProductsDataModel;
import com.group.medexpress.R;

import java.util.ArrayList;

public class ProductsListviewAdapter extends BaseAdapter {
    private Context context;
    private  ArrayList<ProductsDataModel> list;

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


        Uri imageUrl = Uri.parse(list.get(position).getProductImage());
        productName.setText(list.get(position).getProductName());
        price.setText("$ " + list.get(position).getProductPrice());

        if (!imageUrl.equals("")) {
            Glide.with(context.getApplicationContext())
                    .load(imageUrl)
                    .into(productImg);
        }



        return convertView;
    }

}
