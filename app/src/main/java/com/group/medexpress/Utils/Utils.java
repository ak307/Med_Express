package com.group.medexpress.Utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public String getUserID(){
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null) {
            return firebaseUser.getUid();
        }
        return null;
    }
}
