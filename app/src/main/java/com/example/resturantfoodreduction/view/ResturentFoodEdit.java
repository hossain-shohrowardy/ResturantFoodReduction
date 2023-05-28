package com.example.resturantfoodreduction.view;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.resturantfoodreduction.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ResturentFoodEdit extends AppCompatActivity {
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
        setContentView(R.layout.activity_resturent_food_edit);
        resturantFoodTitle=(TextInputEditText)findViewById(R.id.res_food_title);
        resturantFoodDescription=(TextInputEditText)findViewById(R.id.res_food_description);
        resturantFoodPrePrice=(TextInputEditText)findViewById(R.id.res_food_previous_price);
        resturantFoodLatestPrice=(TextInputEditText)findViewById(R.id.res_food_latest_price);
        resturantFoodDiscount=(TextInputEditText)findViewById(R.id.res_food_discount);
        resturantFoodQuentity=(TextInputEditText)findViewById(R.id.res_food_Quantity);
        resturantFoodImage=(ImageView)findViewById(R.id.res_food_img);
        button=(Button)findViewById(R.id.res_food_edit_of_rs_button);
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        storage= FirebaseStorage.getInstance();
        foodId=getIntent().getExtras().getString("foodId");
        Toast.makeText(this, "userId:"+userId+"foodId"+foodId, Toast.LENGTH_SHORT).show();
        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if(result!=null){
                            resturantFoodImage.setImageURI(result);
                            imageUri=result;

                        }


                    }
                });

        resturantFoodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch("image/*");



            }
        });
      /* button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(imageUri!=null){
                   StorageReference reference =FirebaseStorage.getInstance().getReference().child("FoodImages/"+ userId);
                   reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                       reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               downloadedImageUri=uri.toString();
                              String title= resturantFoodTitle.getText().toString();
                              String description= resturantFoodDescription.getText().toString();
                              String discount=resturantFoodDiscount.getText().toString();
                              String quentity=resturantFoodQuentity.getText().toString();
                              String prePrince=resturantFoodPrePrice.getText().toString();
                              String latestPrice=resturantFoodLatestPrice.getText().toString();
                               if(!title.isEmpty() || !description.isEmpty() || !discount.isEmpty() || !quentity.isEmpty() || !prePrince.isEmpty() || !latestPrice.isEmpty()){
                                   FirebaseDatabase.getInstance().getReference().child("FoodReceiveByCustomers").child(userId).child(foodId).
                                           child("Customers").addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                           if(snapshot.exists()){
                                               for (DataSnapshot ds : snapshot.getChildren()) {
                                                   CustomerInfo cusInfo = ds.getValue(CustomerInfo.class);
                                                   String uId = cusInfo.getUserId();

                                                  // if (uId != null && foodId != null) {
                                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                                               child(foodId).child("title").setValue(title);
                                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                                               child(foodId).child("description").setValue(description);
                                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                                               child(foodId).child("discount").setValue(discount);
                                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                                               child(foodId).child("quentity").setValue(quentity);
                                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                                               child(foodId).child(foodId).child("prePrice").setValue(prePrince);
                                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                                               child(foodId).child("latestPrice").setValue(latestPrice);
                                                   FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                                           child(foodId).child("imguri").setValue(downloadedImageUri);

                                                   }

                                           }
                                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                                   child(userId).child("Foods").child(foodId).child("title").setValue(title);
                                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                                   child(userId).child("Foods").child(foodId).child("description").setValue(description);
                                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                                   child(userId).child("Foods").child(foodId).child("discount").setValue(discount);
                                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                                   child(userId).child("Foods").child(foodId).child("quentity").setValue(quentity);
                                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                                   child(userId).child("Foods").child(foodId).child("prePrice").setValue(prePrince);
                                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                                   child(userId).child("Foods").child(foodId).child("latestPrice").setValue(latestPrice);
                                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                                   child(userId).child("Foods").child(foodId).child("imguri").setValue(downloadedImageUri);
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError error) {

                                       }
                                   });

                               }

                           }
                       });
                       }
                   });
               }
               else{
                   String title= resturantFoodTitle.getText().toString();
                   String description= resturantFoodDescription.getText().toString();
                   String discount=resturantFoodDiscount.getText().toString();
                   String quentity=resturantFoodQuentity.getText().toString();
                   String prePrince=resturantFoodPrePrice.getText().toString();
                   String latestPrice=resturantFoodLatestPrice.getText().toString();
                  /* FirebaseDatabase.getInstance().getReference().child("FoodReceiveByCustomers").child(userId).child(foodId).
                           child("Customers").addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if(snapshot.exists()){
                               for (DataSnapshot ds : snapshot.getChildren()) {
                                   CustomerInfo cusInfo = ds.getValue(CustomerInfo.class);
                                   String uId = cusInfo.getUserId();

                                   // if (uId != null && foodId != null) {
                                   FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                           child(foodId).child("title").setValue(title);
                                   FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                           child(foodId).child("description").setValue(description);
                                   FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                           child(foodId).child("discount").setValue(discount);
                                   FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                           child(foodId).child("quentity").setValue(quentity);
                                   FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                           child(foodId).child(foodId).child("prePrice").setValue(prePrince);
                                   FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                           child(foodId).child("latestPrice").setValue(latestPrice);


                               }

                           }
                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                   child(userId).child("Foods").child(foodId).child("title").setValue(title);
                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                   child(userId).child("Foods").child(foodId).child("description").setValue(description);
                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                   child(userId).child("Foods").child(foodId).child("discount").setValue(discount);
                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                   child(userId).child("Foods").child(foodId).child("quentity").setValue(quentity);
                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                   child(userId).child("Foods").child(foodId).child("prePrice").setValue(prePrince);
                           FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                   child(userId).child("Foods").child(foodId).child("latestPrice").setValue(latestPrice);

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });

               }
               

           }
       });*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri!=null){
                    StorageReference reference =FirebaseStorage.getInstance().getReference().child("FoodImages").child(userId);
                    reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadedImageUri=uri.toString();
                                   String resTile= resturantFoodTitle.getText().toString();
                                   String resDes=  resturantFoodDescription.getText().toString();
                                   Integer resFoodDis= Integer.valueOf(resturantFoodDiscount.getText().toString());
                                   Integer resFoodQuen= Integer.valueOf(resturantFoodQuentity.getText().toString());
                                    Integer  resFoodPrePrice=Integer.valueOf(resturantFoodPrePrice.getText().toString());
                                    Integer resFoodLatPrice=Integer.valueOf(resturantFoodLatestPrice.getText().toString());
                                //   if(!resTile.isEmpty() || !resDes.isEmpty() || !resFoodDis.isEmpty() || !resFoodQuen.isEmpty() || !resFoodPrePrice.isEmpty() || !resFoodLatPrice.isEmpty()){
                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                                               child(foodId).child("title").setValue(resTile);
                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                                               child(foodId).child("description").setValue(resDes);
                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                                               child(foodId).child("discount").setValue(resFoodDis);
                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                                               child(foodId).child("quentity").setValue(resFoodQuen);
                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                                               child(foodId).child("prePrice").setValue(resFoodPrePrice);
                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                                               child(foodId).child("latestPice").setValue(resFoodLatPrice);
                                       FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                                               child(foodId).child("imguri").setValue(downloadedImageUri);
                                  // }
                                }
                            });
                        }
                    });
                }
                else{
                    String resTile= resturantFoodTitle.getText().toString();
                    String resDes=  resturantFoodDescription.getText().toString();
                    Integer resFoodDis= Integer.valueOf(resturantFoodDiscount.getText().toString());
                    Integer resFoodQuen= Integer.valueOf(resturantFoodQuentity.getText().toString());
                    Integer  resFoodPrePrice=Integer.valueOf(resturantFoodPrePrice.getText().toString());
                    Integer resFoodLatPrice=Integer.valueOf(resturantFoodLatestPrice.getText().toString());
                    //   if(!resTile.isEmpty() || !resDes.isEmpty() || !resFoodDis.isEmpty() || !resFoodQuen.isEmpty() || !resFoodPrePrice.isEmpty() || !resFoodLatPrice.isEmpty()){
                    FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                            child(foodId).child("title").setValue(resTile);
                    FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                            child(foodId).child("description").setValue(resDes);
                    FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                            child(foodId).child("discount").setValue(resFoodDis);
                    FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                            child(foodId).child("quentity").setValue(resFoodQuen);
                    FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                            child(foodId).child("prePrice").setValue(resFoodPrePrice);
                    FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                            child(foodId).child("latestPice").setValue(resFoodLatPrice);


                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").
                child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if (state == 0) {
                        foodInfoModel = snapshot.getValue(FoodInfoModel.class);
                        resturantFoodTitle.setText(foodInfoModel.getTitle());
                        resturantFoodDescription.setText(foodInfoModel.getDescription());
                        resturantFoodDiscount.setText(foodInfoModel.getDiscount().toString());
                        resturantFoodQuentity.setText(foodInfoModel.getQuentity().toString());
                        resturantFoodPrePrice.setText(foodInfoModel.getPrePrice().toString());
                        resturantFoodLatestPrice.setText(foodInfoModel.getLatestPice().toString());
                        if (imageUri != null && FirebaseAuth.getInstance().getUid() != null) {
                            Glide.with(getApplicationContext()).load(foodInfoModel.getImguri()).into(resturantFoodImage);
                        }
                        state=1;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}