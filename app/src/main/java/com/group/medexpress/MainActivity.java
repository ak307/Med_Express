package com.group.medexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationview;
    private Fragment homeFragment;
    private Fragment meFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationview = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        homeFragment = new HomeFragment();
        meFragment = new MeFragment();

        //This is for automatically home page with Fragment when we open the app
        replaceFragment(homeFragment);

        setBottomNavigationView();
    }

    private void setBottomNavigationView(){
        bottomNavigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.homeNav :
                        replaceFragment(homeFragment);
                        return true;
                    case R.id.classroomNav:
                        replaceFragment(meFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, fragment);
        fragmentTransaction.commit();
    }



}