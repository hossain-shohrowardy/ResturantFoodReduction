package com.example.resturantfoodreduction.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.resturantfoodreduction.FcmNotificationsSender;
import com.example.resturantfoodreduction.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Pay extends AppCompatActivity {

    // 10.0.2.2 is the Android emulator's alias to localhost
    // 192.168.1.6 If you are testing in real device with usb connected to same network then use your IP address
   // private static final String BACKEND_URL = "http://192.168.0.104:4242/"; //4242 is port mentioned in server i.e index.js
    private static final String BACKEND_URL = "http://192.168.1.105:4242/"; //4242 is port mentioned in server i.e index.js
    EditText amountText;
    CardInputWidget cardInputWidget;
    Button payButton;
    String resName;
    String generalAdminToken;


    // we need paymentIntentClientSecret to start transaction
    private String paymentIntentClientSecret;
    //declare stripe
    private Stripe stripe;

    Double amountDouble=null;

    private OkHttpClient httpClient;

    static ProgressDialog progressDialog;
    String token;
    String totalToken;
    String year;
    String validityState;
    String userId;
    String remianing;
    String amount;
    String resultAmount;
   // Date validity;
    String validity;
    int extendState=0;
    int IntentState=0;
    String staticToken="els_88AHSw6dWicsqFtcZI:APA91bHDy7nvxik21pPEFiFEBhdrv6vOCS2sFB764FGsusXQ-I3qHYfGdQvqtQURxkfrWdQaaV4F7XAmfQzK3wnuhFXxzexguDq69PF3qqUbPDGszbD0VYKcQp05yOka5tCet9J7KxrZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        amountText = findViewById(R.id.amount_id);
        cardInputWidget = findViewById(R.id.cardInputWidget);
        payButton = findViewById(R.id.payButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Transaction in progress");
        progressDialog.setCancelable(false);
        httpClient = new OkHttpClient();
       token= getIntent().getStringExtra("token");
        year=getIntent().getStringExtra("year");
        amount=getIntent().getStringExtra("amount");
        resName=getIntent().getStringExtra("resName");
        validityState=getIntent().getStringExtra("validityState");
        amountText.setText(amount);
        userId= FirebaseAuth.getInstance().getUid();
        //Toast.makeText(Pay.this, "year:"+year+" token:"+token+" validityState:"+validityState, Toast.LENGTH_LONG).show();



        //Initialize
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51Kb5FlC80cf2KsgxAXVUDT6MfosbNgLKIHxbzq7qy0K3fBzMijSz4UyMuFEcg2V2XrtNp2GiVLPBKarIIoTAhDEU00id0FiZQh")
               // Objects.requireNonNull("pk_test_51ISLoeDrYpYnN0xnqW7bZ0tJKmtxUEdYOhD8AXoO10S9aMSXZ8Hk6e7EXJvKpn476isXZXgdG5R5TAj7aVXceJZo00bIx1MjgM")
                // Objects.requireNonNull("pk_test_51KXUocIRtyrGPTKY0dXS56URhBVCVt8c4a5xCVvyTatESqhiSww0nTOmw3i5d0JIstq8RTuMojCP14D2TuFRC1Ld00qyot6q5e")
        );
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get Amount
                amountDouble = Double.valueOf(amountText.getText().toString());
                //call checkout to get paymentIntentClientSecret key
               // progressDialog.show();
               // startCheckout();
                Calendar cal = Calendar.getInstance();
                Date today = cal.getTime();
                if(year.equals("one")) {
                    cal.add(Calendar.YEAR, 1);// to get previous year add -1
                }
                else if (year.equals("two")){
                    cal.add(Calendar.YEAR,2);
                }
                else if (year.equals("three")){
                    cal.add(Calendar.YEAR,3);
                }
                else if (year.equals("four")){
                    cal.add(Calendar.YEAR,4);
                }
               // Date validity = cal.getTime();
                Date validityDate = cal.getTime();
                String validityInString=validityDate.toString();
                validity=validityInString.replace("GMT+06:00","");
                if(validityState.equals("choose")){
                    totalToken=token;
                    if(totalToken!=null && validity!=null) {
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").
                                child(userId).child("foodToken").setValue(totalToken);
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").
                                child(userId).child("validity").setValue(validity);
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").
                                child(userId).child("resAmount").setValue(amount);
                        FirebaseDatabase.getInstance().getReference().child("User").
                                child(userId).child("foodToken").setValue(totalToken);
                        FirebaseDatabase.getInstance().getReference().child("User").
                                child(userId).child("validity").setValue(validity);
                        FirebaseDatabase.getInstance().getReference().child("User").
                                child(userId).child("resAmount").setValue(amount);
                        FirebaseDatabase.getInstance().getReference().child("User").child("kq6VfICew3Pdxkighpfh4wKWka23").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    ResturantOwnerModel resturantOwnerModel = snapshot.getValue(ResturantOwnerModel.class);
                                    String adminToken=resturantOwnerModel.getToken();
                                    if(IntentState==0){
                                        FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(staticToken,"Approval Request for Restaurant Account",resName+ " has requested to approve account with \n validity : " +validity.toString()+" and token : "+totalToken+" foods",Pay.this,Pay.this);
                                        fcmNotificationsSender.SendNotifications();
                                        AlertDialog.Builder builder=new AlertDialog.Builder(amountText.getContext());
                                        builder.setTitle("Accepted Account Registration Request");
                                        builder.setMessage(" your resgistration has been successfully completed,soon you will be confirmed to use your account.");
                                        builder.setPositiveButton("OK", (dialogInterface, i) ->{
                                            IntentState=1;
                                            Intent intent =new Intent(Pay.this,LogIn.class);
                                            startActivity(intent);

                                        });
                                       /* builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });*/
                                        builder.show();
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                }
                else if(validityState.equals("extend")){
                    if(userId!=null) {
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").
                                child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    ResturantOwnerModel resturantOwnerModel=snapshot.getValue(ResturantOwnerModel.class);
                                    remianing=resturantOwnerModel.getFoodToken();
                                    if((remianing.equals("")|| remianing!=null) && extendState==0 && token!=null){
                                        Integer remain=Integer.valueOf(remianing);
                                        Integer previous=Integer.valueOf(token);
                                       Integer t=remain+previous;
                                        Toast.makeText(Pay.this, "remain:"+remain+" token:"+token+" total:"+t, Toast.LENGTH_SHORT).show();
                                       if(t!=null && validity!=null) {
                                           extendState=1;
                                           FirebaseDatabase.getInstance().getReference().child("ResturantOwner").
                                                   child(userId).child("foodToken").setValue(t.toString());
                                           FirebaseDatabase.getInstance().getReference().child("ResturantOwner").
                                                   child(userId).child("validity").setValue(validity);
                                           FirebaseDatabase.getInstance().getReference().child("ResturantOwner").
                                                   child(userId).child("resAmount").setValue(amount);
                                           FirebaseDatabase.getInstance().getReference().child("User").
                                                   child(userId).child("foodToken").setValue(t.toString());
                                           FirebaseDatabase.getInstance().getReference().child("User").
                                                   child(userId).child("validity").setValue(validity);
                                           FirebaseDatabase.getInstance().getReference().child("User").
                                                   child(userId).child("resAmount").setValue(amount);
                                           FirebaseDatabase.getInstance().getReference().child("User").
                                                   child(userId).child("extendValidityState").setValue("yes");
                                           FirebaseDatabase.getInstance().getReference().child("ResturantOwner").
                                                   child(userId).child("extendValidityState").setValue("yes");
                                           FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(staticToken,"Approval Request for Restaurant Account",resName+ " has requested to extend   \n validity : " +validity.toString()+" and token : "+t+" foods",Pay.this,Pay.this);
                                           fcmNotificationsSender.SendNotifications();
                                           AlertDialog.Builder builder=new AlertDialog.Builder(amountText.getContext());
                                           builder.setTitle("Accepted Validity Extention Request");
                                           builder.setMessage(" your validity extention has been successfully completed,soon you will be confirmed with a approval message.");
                                           builder.setPositiveButton("OK", (dialogInterface, i) ->{
                                              // IntentState=1;
                                               Intent intent =new Intent(Pay.this,ResturantOwnerPanel.class);
                                               startActivity(intent);

                                           });
                                           builder.show();


                                          /* FirebaseDatabase.getInstance().getReference().child("User").child("RkS8R67x97TUF8XEFXkdnJiYOgi1").addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   if(snapshot.exists()){
                                                       ResturantOwnerModel resturantOwnerModel = snapshot.getValue(ResturantOwnerModel.class);
                                                       String adminToken=resturantOwnerModel.getToken();
                                                       if(IntentState==0){
                                                           FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(adminToken,"Approval Request for Restaurant Account",resName+ " has requested to extend   \n validity : " +validity.toString()+" and token : "+totalToken+" foods",Pay.this,Pay.this);
                                                           fcmNotificationsSender.SendNotifications();
                                                           AlertDialog.Builder builder=new AlertDialog.Builder(amountText.getContext());
                                                           builder.setTitle("Accepted Validity Extention Request");
                                                           builder.setMessage(" your validity extention has been successfully completed,soon you will be confirmed with a approval message.");
                                                           builder.setPositiveButton("OK", (dialogInterface, i) ->{
                                                               IntentState=1;
                                                               Intent intent =new Intent(Pay.this,ResturantOwner.class);
                                                               startActivity(intent);

                                                           });
                                                           builder.show();
                                                       }

                                                   }

                                               }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError error) {

                                               }
                                           });*/





                                       }

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
    private void startCheckout() {
        {
            // Create a PaymentIntent by calling the server's endpoint.
            MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        String json = "{"
//                + "\"currency\":\"usd\","
//                + "\"items\":["
//                + "{\"id\":\"photo_subscription\"}"
//                + "]"
//                + "}";
            double amount=amountDouble;
            Map<String,Object> payMap=new HashMap<>();
            Map<String,Object> itemMap=new HashMap<>();
            List<Map<String,Object>> itemList =new ArrayList<>();
            payMap.put("currency","INR");
            itemMap.put("id","photo_subscription");
            itemMap.put("amount",amount);
            itemList.add(itemMap);
            payMap.put("items",itemList);
            String json = new Gson().toJson(payMap);
            RequestBody body = RequestBody.create(json, mediaType);
            Request request = new Request.Builder()
                    .url(BACKEND_URL + "create-payment-intent")
                    .post(body)
                    .build();
            httpClient.newCall(request)
                    .enqueue(new PayCallback(this));

        }
    }


    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<Pay> activityRef;
        PayCallback(@NonNull Pay activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            progressDialog.dismiss();
            final Pay activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error1: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final Pay activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error2: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");

        //once you get the payment client secret start transaction
        //get card detail
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
        if (params != null) {
            //now use paymentIntentClientSecret to start transaction
            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
            //start payment
            stripe.confirmPayment(Pay.this, confirmParams);
        }
        Log.i("TAG", "onPaymentSuccess: "+paymentIntentClientSecret);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));

    }

    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<Pay> activityRef;
        PaymentResultCallback(@NonNull Pay activity) {
            activityRef = new WeakReference<>(activity);
        }
        //If Payment is successful
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            progressDialog.dismiss();
            final Pay activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Toast toast =Toast.makeText(activity, "Ordered Successful", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
        //If Payment is not successful
        @Override
        public void onError(@NonNull Exception e) {
            progressDialog.dismiss();
            final Pay activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("User").child("kq6VfICew3Pdxkighpfh4wKWka23").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ResturantOwnerModel resturantOwnerModel = snapshot.getValue(ResturantOwnerModel.class);
                    generalAdminToken=resturantOwnerModel.getToken();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}