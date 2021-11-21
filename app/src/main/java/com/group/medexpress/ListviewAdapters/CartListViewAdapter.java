package com.group.medexpress.ListviewAdapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.group.medexpress.Datamodels.cartDataModel;
import com.group.medexpress.R;
import com.group.medexpress.Utils.Checker;

import java.util.ArrayList;

public class CartListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<cartDataModel> list;
    private Checker checker;
    double sum = 0;
    private TextView totalCost;

    public CartListViewAdapter(Context context, ArrayList<cartDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }

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
            convertView = LayoutInflater.from(context).inflate(R.layout.shop_cart_layout, parent, false);

        }

        ImageView productImg = (ImageView) convertView.findViewById(R.id.cartProductView);
        TextView productName = (TextView)  convertView.findViewById(R.id.cartProductNameView);
        TextView productPrice = (TextView)  convertView.findViewById(R.id.cartProductPriceView);
        TextView productQty = (TextView)  convertView.findViewById(R.id.cartProductQuantityView);

        ImageButton deleteCartProductBtn = (ImageButton) convertView.findViewById(R.id.deleteCartProductBtn);
        Button qtyReduceBtn = (Button) convertView.findViewById(R.id.qtyReduceBtn);
        Button qtyPlusBtn = (Button) convertView.findViewById(R.id.qtyPlusBtn);

        totalCost = (TextView) convertView.findViewById(R.id.totalCost);

        checker = new Checker(context);


        Uri imageUrl = Uri.parse(list.get(position).getProductImage());
        productName.setText(list.get(position).getProductName());
        productPrice.setText(list.get(position).getProductPrice());
        productQty.setText(list.get(position).getProductQty());

        if (!imageUrl.equals("")) {
            Glide.with(context.getApplicationContext())
                    .load(imageUrl)
                    .into(productImg);
        }


        for(int sumCalculate=0; sumCalculate<list.size(); sumCalculate++) {

            double eachPrice = Double.parseDouble(list.get(sumCalculate).getProductPrice());
            double eachQty = Double.parseDouble(list.get(sumCalculate).getProductQty());
            sum =+ eachPrice*eachQty;
        }

        totalCost.setText(Double.toString(sum));

        setReduceBtn(qtyReduceBtn, position);
        setPlusBtn(qtyPlusBtn, position);

        return convertView;

    }

    private void setReduceBtn(Button qtyReduceBtn, int position) {
        qtyReduceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(list.get(position).getProductQty());
                if(qty != 1) {
                    qty = qty-1;
                    list.get(position).setProductQty(Integer.toString(qty));


                    for(int sumCalculate=0; sumCalculate<list.size(); sumCalculate++) {

                        double eachPrice = Double.parseDouble(list.get(sumCalculate).getProductPrice());
                        double eachQty = Double.parseDouble(list.get(sumCalculate).getProductQty());
                        sum =+ eachPrice*eachQty;
                    }

                    totalCost.setText(Double.toString(sum));
                }
            }
        });
    }

    private void setPlusBtn(Button qtyPlusBtn, int position) {
        qtyPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(list.get(position).getProductQty());
                qty = qty+1;
                list.get(position).setProductQty(Integer.toString(qty));


                for(int sumCalculate=0; sumCalculate<list.size(); sumCalculate++) {

                    double eachPrice = Double.parseDouble(list.get(sumCalculate).getProductPrice());
                    double eachQty = Double.parseDouble(list.get(sumCalculate).getProductQty());
                    sum =+ eachPrice * eachQty;
                }

                totalCost.setText(Double.toString(sum));

            }
        });
    }


}
