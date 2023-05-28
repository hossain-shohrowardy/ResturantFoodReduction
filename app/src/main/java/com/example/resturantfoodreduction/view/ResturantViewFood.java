package com.example.resturantfoodreduction.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.resturantfoodreduction.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ResturantViewFood extends AppCompatActivity {
    TextInputEditText resturantFoodTitle,resturantFoodDescription,resturantFoodPrePrice;
    TextInputEditText resturantFoodLatestPrice,resturantFoodDiscount,resturantFoodQuentity;
    ImageView resturantFoodImage;
    Button button;
    String  title,description;
    String userId;
    Double longitude;
    Double latitude;
    int  prePrice,latestPice,discount,quentity;
    String foodId;
    Uri imageUri;
    FoodInfoModel foodInfoModel;
    ActivityResultLauncher<String> activityResultLauncher;
    String downloadedImageUri;
    FirebaseStorage storage;
    int state=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_view_food);
        resturantFoodTitle=(TextInputEditText)findViewById(R.id.food_title_res);
        resturantFoodDescription=(TextInputEditText)findViewById(R.id.food_description_res);
        resturantFoodPrePrice=(TextInputEditText)findViewById(R.id.food_previous_price_res);
        resturantFoodLatestPrice=(TextInputEditText)findViewById(R.id.food_latest_price_res);
        resturantFoodDiscount=(TextInputEditText)findViewById(R.id.food_discount_res);
        resturantFoodQuentity=(TextInputEditText)findViewById(R.id.food_Quantity_res);
        resturantFoodImage=(ImageView)findViewById(R.id.food_img_res);
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        storage= FirebaseStorage.getInstance();
        foodId=getIntent().getExtras().getString("foodId");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    foodInfoModel=snapshot.getValue(FoodInfoModel.class);
                    resturantFoodTitle.setText(foodInfoModel.getTitle());
                    resturantFoodDescription.setText(foodInfoModel.getDescription());
                    resturantFoodDiscount.setText(foodInfoModel.getDiscount().toString());
                    resturantFoodQuentity.setText(foodInfoModel.getQuentity().toString());
                    resturantFoodPrePrice.setText(foodInfoModel.getPrePrice().toString());
                    resturantFoodLatestPrice.setText(foodInfoModel.getLatestPice().toString());
                    Glide.with(getApplicationContext()).load(foodInfoModel.getImguri()).into(resturantFoodImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}