package com.group.medexpress.Utils;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group.medexpress.AdminInfoDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Checker {
    private Utils utils = new Utils();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ArrayList<String> arrayList  =  new ArrayList<>();
    private List<DocumentSnapshot> docs = new ArrayList<>();
    private AdminInfoDatabaseHelper adminInfoDatabaseHelper;
    private Context context;

    public Checker(Context context) {
        this.context = context;
        adminInfoDatabaseHelper = new AdminInfoDatabaseHelper(context);
    }


    public boolean isAdmin(){
        Cursor data = adminInfoDatabaseHelper.getData();

        if (data.moveToFirst()) {
            do {
                String id = data.getString(1);
                if (id != null && !id.equals("")){
                    if (utils.getUserID() == null){
                        return false;
                    }
                    else {
                        if (utils.getUserID().equals(id)){
                            return true;
                        }
                    }
                }
            } while (data.moveToNext());
        }

        return false;
    }



}
