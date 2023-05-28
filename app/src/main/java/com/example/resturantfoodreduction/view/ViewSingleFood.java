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

public class ViewSingleFood extends AppCompatActivity {
    TextInputEditText customerFoodTitle, customerFoodDescription, customerFoodPrePrice;
    TextInputEditText  customerFoodLatestPrice, customerFoodDiscount, customerFoodQuentity;
    ImageView  customerFoodImage;
    Button button;
    String  title,description;
    String userId;
    Double longitude;
    Double latitude;
    int  prePrice,latestPice,discount,quentity;
    String foodId;
    Uri imageUri;
    ResturantInfoAndFoodInfoModel foodInfoModel;
    ActivityResultLauncher<String> activityResultLauncher;
    String downloadedImageUri;
    FirebaseStorage storage;
    int state=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_food);
        customerFoodTitle=(TextInputEditText)findViewById(R.id.cus_food_title);
        customerFoodDescription=(TextInputEditText)findViewById(R.id.cus_food_description);
        customerFoodPrePrice=(TextInputEditText)findViewById(R.id.cus_food_previous_price);
        customerFoodLatestPrice=(TextInputEditText)findViewById(R.id.cus_food_latest_price);
        customerFoodDiscount=(TextInputEditText)findViewById(R.id.cus_food_discount);
        customerFoodQuentity=(TextInputEditText)findViewById(R.id.cus_food_Quantity);
        customerFoodImage=(ImageView)findViewById(R.id.cus_food_img);
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        storage= FirebaseStorage.getInstance();
        foodId=getIntent().getExtras().getString("foodId");
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(userId).child("Food").
                child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    foodInfoModel=snapshot.getValue(ResturantInfoAndFoodInfoModel.class);
                    customerFoodTitle.setText(foodInfoModel.getTitle());
                    customerFoodDescription.setText(foodInfoModel.getDescription());
                    customerFoodDiscount.setText(foodInfoModel.getDiscount().toString());
                    customerFoodQuentity.setText(foodInfoModel.getQuentity().toString());
                    customerFoodPrePrice.setText(foodInfoModel.getPrePrice().toString());
                    customerFoodLatestPrice.setText(foodInfoModel.getLatestPice().toString());
                    Glide.with(getApplicationContext()).load(foodInfoModel.getImguri()).into( customerFoodImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}