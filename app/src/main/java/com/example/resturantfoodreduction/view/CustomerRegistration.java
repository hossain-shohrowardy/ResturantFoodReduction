package com.example.resturantfoodreduction.view;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.resturantfoodreduction.FcmNotificationsSender;
import com.example.resturantfoodreduction.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CustomerRegistration extends AppCompatActivity {
    TextInputEditText emailTextInputEditText, passwordTextInputEditText, reTypePasswordTextInputEditText, phoneNumberTextInputEditText, customerNameTextInputEditText;
    ImageView customerImageView, backImageView;
    Button signUp;
    String email,name, password, reTypePassword, resturantName,phoneNumberString;
    String userName,userId,userEmail;
    private FirebaseAuth mAuth;
    int  phoneNumber;
    int locationPermissionStatus=0;
    int authStatus=0;
    DatabaseReference ref;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    LocationListener locationListener;
    CustomerRegistrationModel customerRegistrationModel;
    ActivityResultLauncher<String> activityResultLauncher;
    Uri imageUri;
    String downloadedImageUri;
    FirebaseStorage storage;
    int state= 0;
    String fcmToken;
    ResturantOwnerModel resturantOwnerModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);
        emailTextInputEditText = (TextInputEditText) findViewById(R.id.customer_email);
        passwordTextInputEditText = (TextInputEditText) findViewById(R.id.customer_password);
        reTypePasswordTextInputEditText = (TextInputEditText) findViewById(R.id.customer_retype_password);
        customerNameTextInputEditText = (TextInputEditText) findViewById(R.id.customer_name);
        phoneNumberTextInputEditText = (TextInputEditText) findViewById(R.id.customer_phone_number);
        customerImageView = (ImageView) findViewById(R.id.customer_image);
        backImageView = (ImageView) findViewById(R.id.customer_back);

        signUp = (Button) findViewById(R.id.customerSignUp);
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        storage=FirebaseStorage.getInstance();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    // Toast.makeText(CustomerRegistration.this, "no token found", Toast.LENGTH_SHORT).show();
                }
                else{
                    fcmToken=task.getResult();
                    // Storing data into SharedPreferences
                   // SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    //SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    //myEdit.putString("token", token);
                   // myEdit.commit();
                    //Toast.makeText(CustomerRegistration.this, "Token:"+token, Toast.LENGTH_LONG).show();

                    // FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(token,"ovi","what are doing",ResturantOwner.this,ResturantOwner.this);
                    //fcmNotificationsSender.SendNotifications();

                }
            }
        });


        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if(result!=null){
                            customerImageView.setImageURI(result);
                            imageUri=result;

                        }


                    }
                });


        customerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch("image/*");



            }
        });

    }

    public void customerRegistration(View view) {
        name=customerNameTextInputEditText.getText().toString();
        email = emailTextInputEditText.getText().toString();
        password = passwordTextInputEditText.getText().toString();
        reTypePassword = reTypePasswordTextInputEditText.getText().toString();
       phoneNumberString = phoneNumberTextInputEditText.getText().toString();
      phoneNumber=Integer.valueOf(phoneNumberString);
        //imageUpload();
       // createUser();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CustomerRegistration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    authStatus=1;
                    if(mAuth.getCurrentUser()!=null){
                        userId=mAuth.getCurrentUser().getUid();
                        //Toast.makeText(CustomerRegistration.this,"hello"+userId,Toast.LENGTH_LONG).show();
                        imageUpload();
                        //CheckUserPermsions();
                       // Intent intent= new Intent(CustomerRegistration.this,CustomerPanel.class);
                       // startActivity(intent);
                    }



                }
                else{
                    String fail=task.getException().toString();
                    Toast.makeText(CustomerRegistration.this,"fail "+fail,Toast.LENGTH_LONG).show();
                    authStatus=0;
                }

            }
        });


        //CheckUserPermsions();
        // Intent intent =new Intent(this,CustomerPanel.class);
        //startActivity(intent);


    }

    public void CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                locationPermissionStatus=0;
                return;
            }
        }

        locationPermissionStatus=1;
        getLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionStatus=1;
                    getLocation();
                } else {
                    locationPermissionStatus=0;
                    // Permission D

                    Toast.makeText(this, "permission has not been granted", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getLocation() {


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double latitude;
                double longitude;
                ref = FirebaseDatabase.getInstance().getReference();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
               // if(imageUri!=null){
                //customerRegistrationModel=new CustomerRegistrationModel( userId,name, email,downloadedImageUri, password, phoneNumber, longitude, latitude,"customer");
                resturantOwnerModel = new ResturantOwnerModel(userId,name, email,downloadedImageUri, password, phoneNumber, longitude, latitude,"customer",fcmToken,"200","2022-02-22","yes","yes","no","yes","1000");
               // }
               // else {
                  //  customerRegistrationModel=new CustomerRegistrationModel( userId,name, email, password, phoneNumber, longitude, latitude);
                //}
                ref.child("Customer").child(userId).setValue(resturantOwnerModel);
                ref.child("User").child(userId).setValue(resturantOwnerModel);
                if(state==1){
                     Intent intent= new Intent(CustomerRegistration.this,CustomerPanel.class);
                     startActivity(intent);
                     state=2;
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }
    public void createUser(){
        mAuth = FirebaseAuth.getInstance();
        name=customerNameTextInputEditText.getText().toString();
        email = emailTextInputEditText.getText().toString();
        password = passwordTextInputEditText.getText().toString();
       // reTypePassword = reTypePasswordTextInputEditText.getText().toString();
        //phoneNumberString = phoneNumberTextInputEditText.getText().toString();
       // phoneNumber=Integer.valueOf(phoneNumberString);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CustomerRegistration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    authStatus=1;
                   //userId=mAuth.getUid();
                    //userEmail=.getUid();
                    //imageUpload();
                   // Toast.makeText(CustomerRegistration.this,"Your registration has successfully completed",Toast.LENGTH_LONG).show();
                   // Toast.makeText(CustomerRegistration.this,"Your registration has successfully completed"+" userId:"+mAuth.getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
                   // CheckUserPermsions();
                   // authStatus=1;


                }
                else{
                    String fail=task.getException().toString();
                    Toast.makeText(CustomerRegistration.this,"fail "+fail,Toast.LENGTH_LONG).show();
                    authStatus=0;
                }

            }
        });


    }
    public void imageUpload(){
        if(imageUri!=null) {
        //UUID.randomUUID().toString()
        StorageReference reference =storage.getReference().child("CustomerImages/"+ userId);

            reference.putFile(imageUri).addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CustomerRegistration.this, "Image has been Uploaded Successfully..", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadedImageUri=uri.toString();
                                state=1;
                                CheckUserPermsions();

                            }
                        });
                    } else {
                        Toast.makeText(CustomerRegistration.this, "Image has not Uploaded Successfully..", Toast.LENGTH_SHORT).show();
                    }

                }

            });
        }

    }



}