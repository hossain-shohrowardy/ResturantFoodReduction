package com.example.resturantfoodreduction.view.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.resturantfoodreduction.R;
import com.example.resturantfoodreduction.view.CustomerEditProfileFregment;
import com.example.resturantfoodreduction.view.CustomerHomeFregment;
import com.example.resturantfoodreduction.view.CustomerLogOutFregment;
import com.example.resturantfoodreduction.view.CustomerPanel;
import com.example.resturantfoodreduction.view.CustomerViewProfileFregment;
import com.example.resturantfoodreduction.view.LogIn;
import com.example.resturantfoodreduction.view.ResturantOwnerModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {
    BottomNavigationView navigation;
    final Fragment homeFragment = new AdminHomeFregment();
    final Fragment validityFregment=new AdminExtendValidityApproveList();
    final Fragment logOutFregment = new AdminLogOut();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;
    String userId;
    BadgeDrawable homePage;
    BadgeDrawable extendValidity;
    ArrayList<ResturantOwnerModel> homeList;
    ArrayList<ResturantOwnerModel> extendList;
    Integer homePageNotification=0;
    Integer extendValidityPageNotification=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        navigation = (BottomNavigationView) findViewById(R.id.admin_bottom);
        homePage=navigation.getOrCreateBadge(R.id.admin_home);
        extendValidity=navigation.getOrCreateBadge(R.id.admin_extend_validiy);
        fm.beginTransaction().add(R.id.admin_frame, logOutFregment, "3").hide(logOutFregment).commit();
        fm.beginTransaction().add(R.id.admin_frame, validityFregment, "2").hide(validityFregment).commit();
        fm.beginTransaction().add(R.id.admin_frame,homeFragment, "1").commit();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.admin_home:
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.admin_extend_validiy:
                        fm.beginTransaction().hide(active).show(validityFregment).commit();
                        active = validityFregment;
                        Toast.makeText(getApplicationContext(), "view", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.admin_logout:
                        if(userId!=null){
                            FirebaseAuth.getInstance().signOut();
                            Intent intent= new Intent(AdminPanel.this, LogIn.class);
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
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
           // userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("ResturantOwner").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        homeList=new ArrayList<ResturantOwnerModel>();
                        extendList=new ArrayList<ResturantOwnerModel>();
                        for(DataSnapshot ds:snapshot.getChildren()){
                            ResturantOwnerModel resturantOwnerModel=ds.getValue(ResturantOwnerModel.class);
                            String approveState=resturantOwnerModel.getApproveState();
                            String extendValidityState=resturantOwnerModel.getExtendValidityState();
                            if(approveState.equals("no")){
                                homeList.add(resturantOwnerModel);
                            }
                            if(approveState.equals("yes") && extendValidityState.equals("yes")){
                                extendList.add(resturantOwnerModel);
                            }

                        }

                        homePageNotification=homeList.size();
                        extendValidityPageNotification=extendList.size();
                        homePage.setNumber(homePageNotification);
                        extendValidity.setNumber(extendValidityPageNotification);


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}