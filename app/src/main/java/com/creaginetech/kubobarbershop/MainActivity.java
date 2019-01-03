package com.creaginetech.kubobarbershop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.creaginetech.kubobarbershop.fragment.HomeFragment;
import com.creaginetech.kubobarbershop.fragment.OrderFragment;
import com.creaginetech.kubobarbershop.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inisialisasi bottomnav
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);

        //on item selected bottomnav
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //load fragment default (HomeFragment)
        loadFragment(new HomeFragment());

    }

    //method on item selected listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_inprogress:
                    fragment = new OrderFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
//                case R.id.navigation_cart:
//                    fragment = new CartFragment();
//                    loadFragment(fragment);
//                    return true;
            }

            return false;

        }
    };

    //method load fragment
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            finishAffinity();
    }

}
