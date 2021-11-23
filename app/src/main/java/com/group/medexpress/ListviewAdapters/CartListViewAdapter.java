package com.group.medexpress.ListviewAdapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group.medexpress.Datamodels.cartDataModel;
import com.group.medexpress.PaymentActivity;
import com.group.medexpress.R;
import com.group.medexpress.Utils.Checker;
import com.group.medexpress.Utils.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<cartDataModel> list;
    private Checker checker;
    private double sum = 0;
    private TextView total;
    private FirebaseFirestore firebaseFirestore;
    private Utils utils;
    private Button goToPayBtn;




    public CartListViewAdapter(Context context, ArrayList<cartDataModel> list,
                               TextView total, Button goToPayBtn) {
        this.context = context;
        this.list = list;
        this.total = total;
        this.goToPayBtn = goToPayBtn;
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


        checker = new Checker(context);
        utils = new Utils();
        firebaseFirestore = FirebaseFirestore.getInstance();


        Uri imageUrl = Uri.parse(list.get(position).getProductImage());
        productName.setText(list.get(position).getProductName());
        productPrice.setText(list.get(position).getProductPrice());
        productQty.setText(list.get(position).getProductQty());


        if (!imageUrl.equals("")) {
            Glide.with(context.getApplicationContext())
                    .load(imageUrl)
                    .into(productImg);
        }



        setTotalPrice(position);

        setReduceBtn(qtyReduceBtn, position, productQty);
        setPlusBtn(qtyPlusBtn, position, productQty);
        setDeleteBtn(deleteCartProductBtn, position);
        setGoToPayBtn();

        return convertView;

    }

    @SuppressLint("SetTextI18n")
    private void setTotalPrice(int position){
        double eachPrice = Double.parseDouble(list.get(position).getProductPrice());
        sum = sum + eachPrice;

        total.setText(Double.toString(sum));
    }


    private void setReduceBtn(Button qtyReduceBtn, int position, TextView productQty) {
        qtyReduceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(list.get(position).getProductQty());
                int price = Integer.parseInt(list.get(position).getProductPrice());

                if(qty >= 1) {
                    qty = qty - 1;
                    list.get(position).setProductQty(Integer.toString(qty));


                    sum = 0;
                    for(int sumCalculate=0; sumCalculate<list.size(); sumCalculate++) {

                        double eachPrice = Double.parseDouble(list.get(sumCalculate).getProductPrice());
                        double eachQty = Double.parseDouble(list.get(sumCalculate).getProductQty());
                        sum = sum +  (eachPrice*eachQty);
                    }

                    total.setText(Double.toString(sum));
                    productQty.setText(Integer.toString(qty));


                }

            }
        });
    }


    private void setPlusBtn(Button qtyPlusBtn, int position, TextView productQty) {
        qtyPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(list.get(position).getProductQty());
                int price = Integer.parseInt(list.get(position).getProductPrice());


                qty = qty + 1;
                list.get(position).setProductQty(Integer.toString(qty));


                sum = 0;
                for(int sumCalculate=0; sumCalculate<list.size(); sumCalculate++) {

                    double eachPrice = Double.parseDouble(list.get(sumCalculate).getProductPrice());
                    double eachQty = Double.parseDouble(list.get(sumCalculate).getProductQty());
                    sum = sum +  (eachPrice * eachQty);
                }



                total.setText(Double.toString(sum));
                productQty.setText(Integer.toString(qty));
            }
        });
    }


    private void setDeleteBtn(ImageButton deleteBtn, int position){
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String docID = list.get(position).getProductDocID();
                firebaseFirestore.collection("customers")
                        .document(utils.getUserID())
                        .update("shop_cart", FieldValue.arrayRemove(docID))
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

    private void setGoToPayBtn(){
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra("totalCost", sum);
        context.startActivity(intent);
    }


}
