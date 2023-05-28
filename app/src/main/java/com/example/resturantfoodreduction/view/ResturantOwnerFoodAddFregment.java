package com.example.resturantfoodreduction.view;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.resturantfoodreduction.FcmNotificationsSender;
import com.example.resturantfoodreduction.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResturantOwnerFoodAddFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResturantOwnerFoodAddFregment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    TextInputEditText resturantFoodTitle,resturantFoodDescription,resturantFoodPrePrice;
    TextInputEditText resturantFoodLatestPrice,resturantFoodDiscount,resturantFoodQuentity;
    ImageView resturantFoodImage;
    Button button;
    String  title,description;
    String userId;
    Double longitude;
    Double latitude;
    Integer  prePrice,latestPice,discount,quentity;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    DatabaseReference ref;
    String downloadedImageUri;
    float distance=100;
    String randomNumber;
    FoodInfoModel foodInfoModel;
    String resturantId, resturantName,resturantEmail,resturantPassword;
    Integer resturantPhoneNumber;
    ActivityResultLauncher<String> activityResultLauncher;
    ResturantInfo resturant;
    Uri imageUri;
    int state=0;
    String resImg;
    String resToken;
    Integer notificationState=0;
    ResturantInfoAndFoodInfoModel resturantInfoAndFoodInfoModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ResturantOwnerFoodAddFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResturantOwnerFoodAddFregment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResturantOwnerFoodAddFregment newInstance(String param1, String param2) {
        ResturantOwnerFoodAddFregment fragment = new ResturantOwnerFoodAddFregment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mAuth=FirebaseAuth.getInstance();
            storage=FirebaseStorage.getInstance();
            ref=FirebaseDatabase.getInstance().getReference();




        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_resturant_owner_food_add_fregment, container, false);
        resturantFoodTitle=(TextInputEditText)view.findViewById(R.id.food_title);
        resturantFoodDescription=(TextInputEditText)view.findViewById(R.id.food_description);
        resturantFoodPrePrice=(TextInputEditText)view.findViewById(R.id.food_previous_price);
        resturantFoodLatestPrice=(TextInputEditText)view.findViewById(R.id.food_latest_price);
        resturantFoodDiscount=(TextInputEditText)view.findViewById(R.id.food_discount);
        resturantFoodQuentity=(TextInputEditText) view.findViewById(R.id.food_Quantity);
        resturantFoodImage=(ImageView) view.findViewById(R.id.resturant_food);
        button=(Button)view.findViewById(R.id.food_add_button);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "please add food"+ FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                title=resturantFoodTitle.getText().toString();
                description=resturantFoodDescription.getText().toString();
                prePrice=Integer.valueOf(resturantFoodPrePrice.getText().toString());
                latestPice=Integer.valueOf(resturantFoodLatestPrice.getText().toString());
                discount=Integer.valueOf(resturantFoodDiscount.getText().toString());
                quentity=Integer.valueOf(resturantFoodQuentity.getText().toString());
                userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                randomNumber=UUID.randomUUID().toString();
                Toast.makeText(getActivity(), "state="+state, Toast.LENGTH_SHORT).show();
                //imageUpload();
                if(imageUri!=null ) {
                    StorageReference reference =FirebaseStorage.getInstance().getReference().child("FoodImages/").child(userId).child(randomNumber);
                    reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadedImageUri = uri.toString();
                                        state = 1;
                                        //start
                                        //randomNumber=UUID.randomUUID().toString();
                                        foodInfoModel = new FoodInfoModel(downloadedImageUri, randomNumber, title, description, prePrice, latestPice, discount, quentity);
                                        //  FirebaseDatabase.getInstance().getReference().child("AllFoods").child( randomNumber).setValue(foodInfoModel);
                                        //FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child( randomNumber).setValue(foodInfoModel);
                                        //FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child( randomNumber).setValue(foodInfoModel);
                                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ResturantOwnerModel resturantOwnerModel = snapshot.getValue(ResturantOwnerModel.class);
                                                resturantEmail = resturantOwnerModel.getEmail();
                                                resturantName = resturantOwnerModel.getName();
                                                resturantPassword = resturantOwnerModel.getPassword();
                                                resturantPhoneNumber = resturantOwnerModel.getPhoneNumber();
                                                longitude = resturantOwnerModel.getLongitude();
                                                latitude = resturantOwnerModel.getLatitude();
                                                resImg = resturantOwnerModel.getImguri();
                                                resToken=resturantOwnerModel.getToken();

                                                //resturant=new ResturantInfo(userId, resturantName, resturantEmail, resturantPassword, resturantPhoneNumber, longitude,  latitude);
                                                resturantInfoAndFoodInfoModel = new ResturantInfoAndFoodInfoModel(downloadedImageUri, randomNumber, title, description, prePrice, latestPice, discount, quentity,
                                                        userId, resturantName, resturantEmail, resturantPassword, resturantPhoneNumber, longitude, latitude, resImg, "no",resToken);
                                                FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").child(randomNumber).setValue(foodInfoModel);


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        FirebaseDatabase.getInstance().getReference().child("Customer").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                float results[] = new float[1];
                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                   // if (notificationState == 0) {
                                                        notificationState=1;

                                                        ResturantOwnerModel customerRegistrationModel = ds.getValue(ResturantOwnerModel.class);
                                                        // CustomerRegistrationModel customerRegistrationModel=ds.getValue(CustomerRegistrationModel.class);
                                                        String customerId = customerRegistrationModel.getUserId();
                                                        String fcmToken = customerRegistrationModel.getToken();
                                                        String customerName = customerRegistrationModel.getName();
                                                        String customerEmail = customerRegistrationModel.getEmail();
                                                        String customerPassword = customerRegistrationModel.getPassword();
                                                        Integer customerPhoneNumber = customerRegistrationModel.getPhoneNumber();
                                                        Double customerLongitude = customerRegistrationModel.getLongitude();
                                                        Double customerLatitude = customerRegistrationModel.getLatitude();
                                                        String cusImg = customerRegistrationModel.getImguri();
                                                        Location.distanceBetween(latitude, longitude, customerLatitude, customerLongitude, results);
                                                        float customerDistance = results[0];
                                                        Toast.makeText(getActivity(), "distance:" + customerDistance + " length:" + distance, Toast.LENGTH_LONG).show();
                                                        if (customerDistance < distance) {
                                                            Toast.makeText(getActivity(), "distance geather:" + customerDistance, Toast.LENGTH_LONG).show();
                                                            CustomerInfo customerInfo = new CustomerInfo(customerId, fcmToken, customerName, customerEmail, customerPassword, customerPhoneNumber, customerLongitude, customerLatitude, customerDistance, cusImg,"no","no");
                                                            FirebaseDatabase.getInstance().getReference().child("FoodReceiveByCustomers").child(userId).child(randomNumber).child("Customers").child(customerId).setValue(customerInfo);
                                                            //FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(customerId).child("CustomerInfo").setValue(customerInfo);
                                                            FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(customerId).child("Food").child(randomNumber).setValue(resturantInfoAndFoodInfoModel);
                                                            //FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(customerId).child("Food").child(randomNumber).setValue(resturant);
                                                            FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(fcmToken, resturantName + " has added a new food", title + " is going on " + discount + "% discount,\n" + "Previous Price : " + prePrice + " TK Latest Price : " + latestPice + " TK.", resturantFoodTitle.getContext(), getActivity());
                                                            fcmNotificationsSender.SendNotifications();


                                                        }
                                                   // }


                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        //end

                                    }
                                });
                            }
                        }
                    });

                }
                if(state==1){
                    Toast.makeText(getActivity(), "state="+state, Toast.LENGTH_SHORT).show();
                }

                /*randomNumber=UUID.randomUUID().toString();
                foodInfoModel=new FoodInfoModel(randomNumber, title, description,  prePrice, latestPice,  discount,quentity);
                //  FirebaseDatabase.getInstance().getReference().child("AllFoods").child( randomNumber).setValue(foodInfoModel);
                //FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child( randomNumber).setValue(foodInfoModel);
                //FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child( randomNumber).setValue(foodInfoModel);
                FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ResturantOwnerModel resturantOwnerModel=snapshot.getValue(ResturantOwnerModel.class);
                        resturantEmail=resturantOwnerModel.getEmail();
                        resturantName=resturantOwnerModel.getName();
                        resturantPassword=resturantOwnerModel.getPassword();
                        resturantPhoneNumber=resturantOwnerModel.getPhoneNumber();
                        longitude=resturantOwnerModel.getLongitude();
                        latitude=resturantOwnerModel.getLatitude();
                        resturant=new ResturantInfo(userId, resturantName, resturantEmail, resturantPassword, resturantPhoneNumber, longitude,  latitude);
                        FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").child(userId).child("Foods").child(randomNumber).setValue( foodInfoModel);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/
               /* FirebaseDatabase.getInstance().getReference().child("Customer").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        float results[]=new float[1];
                        for(DataSnapshot ds:snapshot.getChildren()){
                            CustomerRegistrationModel customerRegistrationModel=ds.getValue(CustomerRegistrationModel.class);
                            String customerId=customerRegistrationModel.getUserId();
                            String customerName=customerRegistrationModel.getName();
                            String customerEmail=customerRegistrationModel.getEmail();
                            String customerPassword=customerRegistrationModel.getPassword();
                            Integer customerPhoneNumber=customerRegistrationModel.getPhoneNumber();
                            Double customerLongitude=customerRegistrationModel.getLongitude();
                            Double customerLatitude=customerRegistrationModel.getLatitude();
                            Location.distanceBetween(latitude,longitude,customerLatitude,customerLongitude,results);
                            float customerDistance=results[0];
                            Toast.makeText(getActivity(),"distance:"+customerDistance+" length:"+distance,Toast.LENGTH_LONG).show();
                            if(customerDistance<distance){
                                Toast.makeText(getActivity(),"distance geather:"+customerDistance,Toast.LENGTH_LONG).show();
                                CustomerInfo customerInfo=new CustomerInfo(customerId, customerName, customerEmail, customerPassword,customerPhoneNumber, customerLongitude, customerLatitude,customerDistance);
                                FirebaseDatabase.getInstance().getReference().child("FoodReceiveByCustomers").child(userId).child(randomNumber).child("Customers").child(customerId).setValue(customerInfo);
                                //FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(customerId).child("CustomerInfo").setValue(customerInfo);
                                FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(customerId).child("Food").child(randomNumber).setValue(foodInfoModel);
                                FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(customerId).child("Food").child(randomNumber).setValue(resturant);


                            }




                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/


            }
        });
        return view;
    }
    public void imageUpload(){
        if(imageUri!=null) {
            StorageReference reference =FirebaseStorage.getInstance().getReference().child("FoodImages/"+ userId);
            reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadedImageUri=uri.toString();
                                state=1;
                            }
                        });
                    }
                }
            });

        }

    }
}