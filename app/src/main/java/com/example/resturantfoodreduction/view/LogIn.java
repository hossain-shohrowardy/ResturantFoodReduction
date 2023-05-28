package com.example.resturantfoodreduction.view;

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
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resturantfoodreduction.FcmNotificationsSender;
import com.example.resturantfoodreduction.FirebaseMessagingService;
import com.example.resturantfoodreduction.R;
import com.example.resturantfoodreduction.view.Admin.AdminPanel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class LogIn extends AppCompatActivity {
TextInputEditText emailTextInputEditText,passwordTextInputEditText;
TextView forgotPasswordTextview,createAccountTextView;
ProgressBar progressBar;
Button login;
String email,password;
FirebaseAuth mAuth;
String apiKey;
String emailPattern;
 String userId;
 int state=3;
 int sta;
    LocationListener locationListener;
    String userStaus;
    DatabaseReference ref;
    int locationPermissionStatus=0;
    int logInIntentStatus=0;
    DataSnapshot dataSnapshot;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String tokenNew;
    String fcmToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(LogIn.this, "no token found", Toast.LENGTH_SHORT).show();
                }
                else{
                    String token=task.getResult();
                    // Storing data into SharedPreferences
                   /* SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("token", token);
                    myEdit.commit();*/
                  //  Toast.makeText(LogIn.this, "Token:"+token, Toast.LENGTH_LONG).show();

                   // FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(token,"ovi","what are doing",LogIn.this,LogIn.this);
                   // fcmNotificationsSender.SendNotifications();

                }
            }
        });
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        tokenNew= sh.getString("token","");
       // Toast.makeText(LogIn.this, "TokenNew:"+tokenNew, Toast.LENGTH_LONG).show();
        emailTextInputEditText=(TextInputEditText) findViewById(R.id.email);
        passwordTextInputEditText=(TextInputEditText) findViewById(R.id.password);
        forgotPasswordTextview=(TextView) findViewById(R.id.forgot_password);
        createAccountTextView=(TextView) findViewById(R.id.create_account);
        login=(Button) findViewById(R.id.login);
        progressBar=(ProgressBar) findViewById(R.id.login_progressbar);

        mAuth=FirebaseAuth.getInstance();
         emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        //apiKey="AIzaSyBUqS7rVI3qdiOL0WymF6ze7qZXwcB2HBA";
        //Places.initialize(getApplicationContext(),apiKey);
       // PlacesClient placesClient= Places.createClient(this);
        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this,ChooseRegistration.class);
                startActivity(intent);
            }
        });
        forgotPasswordTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, AdminPanel.class);
                startActivity(intent);

            }
        });




    }

    public void signIn(View view) {
        //state=0;
        email=emailTextInputEditText.getText().toString();
        password=passwordTextInputEditText.getText().toString();
        if(!(email.isEmpty()) && !(password.isEmpty())) {
            if((email.matches(emailPattern)) && (password.length())>=6) {

                progressBar.setVisibility(View.VISIBLE);
               mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           state=0;
                           //if(FirebaseAuth.getInstance().getCurrentUser()!=null){

                           userId= FirebaseAuth.getInstance().getCurrentUser().getUid();//}
                           if(userId!=null){
                               //userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                           FirebaseDatabase.getInstance().getReference().child("User").child(userId).
                                   addValueEventListener(new ValueEventListener() {

                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                                           if(snapshot.exists()){
                                               //CustomerRegistrationModel customerRegistrationModel= snapshot.getValue(CustomerRegistrationModel.class);
                                               //String status=customerRegistrationModel.getStatus();
                                              // userStaus=status;
                                               ResturantOwnerModel resturantOwnerModel=snapshot.getValue(ResturantOwnerModel.class);
                                               String status=resturantOwnerModel.getStatus();
                                               String approveState=resturantOwnerModel.getApproveState();
                                              // Toast.makeText(LogIn.this,"status:"+status,Toast.LENGTH_LONG).show();
                                               if(status.equals("customer")){
                                                   progressBar.setVisibility(View.GONE);
                                                   CheckUserPermsions(status);
                                                 //  Intent intent=new Intent(LogIn.this,CustomerPanel.class);
                                                 //  startActivity(intent);
                                               }
                                             else if(status.equals("admin")){
                                                   progressBar.setVisibility(View.GONE);
                                                   CheckUserPermsions(status);
                                                   //  Intent intent=new Intent(LogIn.this,CustomerPanel.class);
                                                   //  startActivity(intent);
                                               }

                                               else if(status.equals("resturant")){
                                                   progressBar.setVisibility(View.GONE);
                                                   if(approveState.equals("yes")){
                                                   CheckUserPermsions(status);
                                                   }
                                                   else{
                                                       Toast.makeText(LogIn.this, "You Registered Account is not approved yet, Very Soon you will be notified with an approval message..", Toast.LENGTH_LONG).show();
                                                   }
                                                  // Intent intent=new Intent(LogIn.this,ResturantOwnerPanel.class);
                                                   //startActivity(intent);
                                               }
                                               else if(status==null)
                                               {
                                                   progressBar.setVisibility(View.GONE);
                                                   Toast.makeText(LogIn.this,"Application found no user,Please complete registration",Toast.LENGTH_LONG).show();

                                               }
                                           }

                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError error) {

                                       }
                                   });



                                   }



                       }


                    }
               });

            }
            else{
               if (!(email.matches(emailPattern)) || (password.length())<6){
                  if(!(email.matches(emailPattern))){
                     emailTextInputEditText.setError("Invalid email");
                  }
                  if((password.length())<6){
                      passwordTextInputEditText.setError("Password must be at least 6 digits");
                  }
               }

            }
        }
        else{
          if(email.isEmpty()){
              emailTextInputEditText.setError("Email is empty");
          }
           if( password.isEmpty()){
              passwordTextInputEditText.setError("Password is empty");
          }
        }
    }

    public void CheckUserPermsions(String status) {
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
        getLocation(status);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionStatus=1;
                    if(userStaus!=null) {
                        getLocation(userStaus);
                    }
                } else {
                    // Permission D
                    locationPermissionStatus=0;

                    Toast.makeText(this, "permission has not been granted", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getLocation( String statusOfUser) {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double latitude;
                double longitude;
                ref = FirebaseDatabase.getInstance().getReference();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if(statusOfUser.equals("customer")){
                    if(userId!=null){
                        ref.child("Customer").child(userId).child("latitude").setValue(latitude);
                        ref.child("Customer").child(userId).child("longitude").setValue(longitude);
                        ref.child("User").child(userId).child("latitude").setValue(latitude);
                        ref.child("User").child(userId).child("longitude").setValue(longitude);
                    }
                    if(logInIntentStatus==0) {
                        Intent intent = new Intent(LogIn.this, CustomerPanel.class);
                        startActivity(intent);
                        logInIntentStatus=1;
                    }
                }
                else if(statusOfUser.equals("resturant")){
                    if(userId!=null){
                        ref.child("ResturantOwner").child(userId).child("latitude").setValue(latitude);
                        ref.child("ResturantOwner").child(userId).child("longitude").setValue(longitude);
                        ref.child("User").child(userId).child("latitude").setValue(latitude);
                        ref.child("User").child(userId).child("longitude").setValue(longitude);
                    }
                    if(logInIntentStatus==0) {
                        Intent intent = new Intent(LogIn.this, ResturantOwnerPanel.class);
                        startActivity(intent);
                        logInIntentStatus=1;
                    }

                }
                else if(statusOfUser.equals("admin")){
                    if(userId!=null){
                        ref.child("Admin").child(userId).child("latitude").setValue(latitude);
                        ref.child("Admin").child(userId).child("longitude").setValue(longitude);
                        ref.child("User").child(userId).child("latitude").setValue(latitude);
                        ref.child("User").child(userId).child("longitude").setValue(longitude);
                    }
                    if(logInIntentStatus==0) {
                        Intent intent = new Intent(LogIn.this, AdminPanel.class);
                        startActivity(intent);
                        logInIntentStatus=1;
                    }

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

    @Override
    protected void onStart() {

        super.onStart();
    }
}