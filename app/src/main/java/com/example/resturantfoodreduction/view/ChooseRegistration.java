package com.example.resturantfoodreduction.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.resturantfoodreduction.R;

public class ChooseRegistration extends AppCompatActivity {
    Button resturantOwner,customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_registration);
        resturantOwner=(Button) findViewById(R.id.resturant_owner_signup_button);
        customer=(Button) findViewById(R.id.customer_signup_button);
        resturantOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ChooseRegistration.this,ResturantOwner.class);
                startActivity(intent);
            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseRegistration.this,CustomerRegistration.class);
                startActivity(intent);

            }
        });
    }
}