package com.example.resturantfoodreduction.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.resturantfoodreduction.R;

public class Payment extends AppCompatActivity {
    Button button;
    String token;
    String year;
    String validityState;
    String amount;
    String resName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        button=(Button) findViewById(R.id.pay_method);
        token= getIntent().getStringExtra("token");
        year=getIntent().getStringExtra("year");
        amount= getIntent().getStringExtra("amount");
        resName=getIntent().getStringExtra("resName");

        validityState=getIntent().getStringExtra("validityState");
    }

    public void payWithStripe(View view) {
        if(token!=null && year!=null && validityState!=null) {
            Intent intent = new Intent(Payment.this, Pay.class);
            intent.putExtra("year",year);
            intent.putExtra("token",token);
            intent.putExtra("resName",resName);
            intent.putExtra("validityState", validityState);
            intent.putExtra("amount",amount);
            startActivity(intent);
        }
    }
}