package com.example.resturantfoodreduction.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.resturantfoodreduction.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResturantOwnerPanel extends AppCompatActivity {
    BottomNavigationView navigation;
    final Fragment homeFragment = new ResturantOwnerHomeFragment();
    final Fragment addFregment = new ResturantOwnerFoodAddFregment();
    final Fragment viewFregment=new ResturantViewProfileFregment();
    final Fragment editFregment = new EditResturantOwnerProfile();
    final Fragment logOutFregment = new ResturantLogOutFregment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;
    String userId;
    LocationListener locationListener;
    DatabaseReference ref;
    int locationPermissionStatus=0;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_owner_panel);
         navigation = (BottomNavigationView) findViewById(R.id.resturant_owner_bottom);
         if(FirebaseAuth.getInstance().getCurrentUser()!=null){
         userId= FirebaseAuth.getInstance().getCurrentUser().getUid();}

       // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.resurant_owner_frame, logOutFregment, "5").hide(logOutFregment).commit();
        fm.beginTransaction().add(R.id.resurant_owner_frame, editFregment, "4").hide(editFregment).commit();
        fm.beginTransaction().add(R.id.resurant_owner_frame,viewFregment,"3").hide(viewFregment).commit();
        fm.beginTransaction().add(R.id.resurant_owner_frame, addFregment, "2").hide(addFregment).commit();
        fm.beginTransaction().add(R.id.resurant_owner_frame,homeFragment, "1").commit();

        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.resturent_home:
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.resturent_add_food:
                        fm.beginTransaction().hide(active).show(addFregment).commit();
                        active = addFregment;
                        Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.resturent_view_profile:
                    fm.beginTransaction().hide(active).show(viewFregment).commit();
                    active = viewFregment;
                    Toast.makeText(getApplicationContext(), "view", Toast.LENGTH_SHORT).show();
                    return true;

                    case R.id.resturent_edit_profile:
                        fm.beginTransaction().hide(active).show(editFregment).commit();
                        active = editFregment;
                        Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.resturent_logout:
                        if(userId!=null){
                            FirebaseAuth.getInstance().signOut();
                            Intent intent=new Intent(ResturantOwnerPanel.this,LogIn.class);
                            startActivity(intent);
                            finish();
                        }
                       // fm.beginTransaction().hide(active).show(logOutFregment).commit();
                      //  active = logOutFregment;
                       // Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
                        return true;
                }


                return false;
            }
        });
    }

    @Override
    protected void onStart() {
       // CheckUserPermsions();
        super.onStart();

    }

    public void CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
               // locationPermissionStatus=0;
                return;
            }
        }

        //locationPermissionStatus=1;
        StartServices();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // locationPermissionStatus=1;
                    StartServices();
                } else {
                    // Permission D
                  //  locationPermissionStatus=0;

                    Toast.makeText(this, "permission has not been granted", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void StartServices() {


        Intent intent=new Intent(this,ResturantOwnerService.class);
        startService(intent);

    }

}