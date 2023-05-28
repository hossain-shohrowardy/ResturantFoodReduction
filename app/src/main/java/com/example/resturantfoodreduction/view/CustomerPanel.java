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
import android.widget.Toast;

import com.example.resturantfoodreduction.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerPanel extends AppCompatActivity {
    BottomNavigationView navigation;
    final Fragment homeFragment = new CustomerHomeFregment();
    final Fragment viewFregment=new CustomerViewProfileFregment();
    final Fragment editFregment = new CustomerEditProfileFregment();
    final Fragment logOutFregment = new CustomerLogOutFregment();
    final FragmentManager fm = getSupportFragmentManager();
    String userId;
    Fragment active = homeFragment;
    LocationListener locationListener;
    DatabaseReference ref;
    int locationPermissionStatus=0;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    BadgeDrawable badgeDrawable;
    Integer countNotification=0;
    ArrayList<ResturantInfoAndFoodInfoModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_panel);
        navigation = (BottomNavigationView) findViewById(R.id.customer_bottom);
        badgeDrawable=navigation.getOrCreateBadge(R.id.customer_home);
       // badgeDrawable.setNumber(10);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


        // userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.customer_frame, logOutFregment, "4").hide(logOutFregment).commit();
        fm.beginTransaction().add(R.id.customer_frame, editFregment, "3").hide(editFregment).commit();
        fm.beginTransaction().add(R.id.customer_frame, viewFregment, "2").hide(viewFregment).commit();
        fm.beginTransaction().add(R.id.customer_frame,homeFragment, "1").commit();

        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.customer_home:
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.customer_view_profile:
                        fm.beginTransaction().hide(active).show(viewFregment).commit();
                        active = viewFregment;
                        Toast.makeText(getApplicationContext(), "view", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.customer_edit_profile:
                        fm.beginTransaction().hide(active).show(editFregment).commit();
                        active = editFregment;
                        Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.customer_logout:
                        if(userId!=null){
                            FirebaseAuth.getInstance().signOut();
                            Intent intent= new Intent(CustomerPanel.this,LogIn.class);
                           // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                       // fm.beginTransaction().hide(active).show(logOutFregment).commit();
                       // active = logOutFregment;
                       // Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();

                        return true;
                }


                return false;
            }
        });
    }
    @Override
    protected void onStart() {
       //CheckUserPermsions();
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").
                    child(userId).child("Food").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        list=new ArrayList<ResturantInfoAndFoodInfoModel>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ResturantInfoAndFoodInfoModel resturantInfoAndFoodInfoModel = ds.getValue(ResturantInfoAndFoodInfoModel.class);
                            String seenState=resturantInfoAndFoodInfoModel.getSeenState();
                            if(seenState.equals("no")){
                                list.add(resturantInfoAndFoodInfoModel);
                            }

                        }
                        // Toast.makeText(getActivity(), "size:" + list.size(), Toast.LENGTH_SHORT).show();
                        countNotification=list.size();
                        badgeDrawable.setNumber(countNotification);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

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


        Intent intent=new Intent(this,CustomerService.class);
        startService(intent);

    }

}