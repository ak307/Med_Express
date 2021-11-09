package com.group.medexpress;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.group.medexpress.Utils.Utils;


public class MeFragment extends Fragment {
    private Button logoutBtn;
    private FirebaseAuth auth;
    private Button loginBtn;
    private LinearLayout cart;
    private LinearLayout wishList;
    private LinearLayout myRating;
    private LinearLayout trackOrderDelivery;
    private LinearLayout purchasedHistory;
    private Utils utils;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);


        logoutBtn = (Button) view.findViewById(R.id.logoutBtn);
        loginBtn = (Button) view.findViewById(R.id.meLogInBtn);
        cart = (LinearLayout) view.findViewById(R.id.cartLLayout);
        wishList = (LinearLayout) view.findViewById(R.id.wishListLLayout);
        myRating = (LinearLayout) view.findViewById(R.id.myRatingLLayout);
        trackOrderDelivery = (LinearLayout) view.findViewById(R.id.trackOrderLLayout);
        purchasedHistory = (LinearLayout) view.findViewById(R.id.purchaseHistoryLLayout);


        auth = FirebaseAuth.getInstance();
        utils = new Utils();

        if (utils.getUserID() != null)
            loginBtn.setVisibility(View.INVISIBLE);

        else
            logoutBtn.setVisibility(View.INVISIBLE);



        setLoginBtn();
        setLogoutBtn();
        setCart();
        setWishList();
        setMyRating();
        setTrackOrderDelivery();
        setPurchasedHistory();




        return view;
    }


    private void setLoginBtn(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LogInActivity.class);
                startActivity(intent);
            }
        });
    }


    private void setLogoutBtn(){
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                sendToLoginActivity();
            }
        });
    }


    private void setCart(){
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void setWishList(){
        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void setMyRating(){
        myRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void setTrackOrderDelivery(){
        trackOrderDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void setPurchasedHistory(){
        purchasedHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void sendToLoginActivity(){
        Intent intent = new Intent(getContext(), LogInActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}