package com.example.resturantfoodreduction.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.resturantfoodreduction.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ViewResturantForCustomer extends AppCompatActivity {
    View view;
    ImageView imageView;
    TextView nameTitle,nameTextView,emailTitle,emailTextView,phoneNoTitle,phoneNoTextView,locationTextView;
    String imageuri, userId,name,email;
    String phoneNo;
    String latitude,longitude;
    ArrayList<ResturantOwnerModel> list;
    String foodId;
    FirebaseStorage storage;
    ResturantInfoAndFoodInfoModel foodInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resturant_for_customer);
        imageView=(ImageView) findViewById(R.id.cus_view_resturant_image);
        nameTextView=(TextView)findViewById(R.id.cus_res_name);
        emailTextView=(TextView) findViewById(R.id.cus_res_email);
        phoneNoTextView=(TextView) findViewById(R.id.cus_res_phone);
        locationTextView=(TextView) findViewById(R.id.cus_res_location);
        foodId=getIntent().getExtras().getString("foodId");
        //userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        storage= FirebaseStorage.getInstance();


    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(userId).child("Food").
                child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                   foodInfoModel = snapshot.getValue(ResturantInfoAndFoodInfoModel.class);
                    foodInfoModel=snapshot.getValue(ResturantInfoAndFoodInfoModel.class);
                    nameTextView.setText(foodInfoModel.getName());
                    emailTextView.setText(foodInfoModel.getEmail());
                    phoneNoTextView.setText(foodInfoModel.getPhoneNumber().toString());

                    Glide.with(getApplicationContext()).load(foodInfoModel.getResImg()).into(imageView);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}