package com.group.medexpress;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class MeFragment extends Fragment {
    private Button logoutBtn;
    private FirebaseAuth auth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);


        logoutBtn = (Button) view.findViewById(R.id.logoutBtn);
        auth = FirebaseAuth.getInstance();


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                sendToLoginActivity();
            }
        });



        return view;
    }


    private void sendToLoginActivity(){
        Intent intent = new Intent(getContext(), LogInActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}