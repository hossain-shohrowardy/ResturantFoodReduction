package com.example.resturantfoodreduction.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.resturantfoodreduction.R;

public class TokenList extends AppCompatActivity {
    RadioButton oneYear,twoYear,threeYear,fourYear;
    Button tokenButton;
    String year;
    String token;
    //Integer token;
    String validityState;
    String amount;
    String resName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_list);
        oneYear=(RadioButton) findViewById(R.id.oneYear);
        twoYear=(RadioButton) findViewById(R.id.TwoYear);
        threeYear=(RadioButton) findViewById(R.id.ThreeYear);
        fourYear=(RadioButton) findViewById(R.id.fourYear);
        tokenButton=(Button) findViewById(R.id.tokenButton);
        validityState=getIntent().getStringExtra("validityState");
        resName=getIntent().getStringExtra("resName");
        //String validity=getIntent().getExtras().getString("validityState");
       // Toast.makeText(TokenList.this, "validity sate pre"+validityState, Toast.LENGTH_SHORT).show();
       // Toast.makeText(TokenList.this, "validity sate new"+validity, Toast.LENGTH_SHORT).show();
        tokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(year==null || year.equals("")){
                    Toast.makeText(TokenList.this, "Choose a token", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent=new Intent(TokenList.this,Payment.class);
                    intent.putExtra("year",year);
                    intent.putExtra("token",token);
                    intent.putExtra("amount",amount);
                    intent.putExtra("resName",resName);
                    if(validityState!=null || !validityState.equals("")) {
                        intent.putExtra("validityState", validityState);
                    }
                    startActivity(intent);
                }
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String str="";
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.oneYear:
                if(checked){
                    year="one";
                    token="200";
                    amount="1000";
                    break;
                }
            case R.id.TwoYear:
                if(checked){
                    year="two";
                    token="400";
                    amount="2000";
                    break;
                }
            case R.id.ThreeYear:
                if(checked){
                    year="three";
                    token="600";
                    amount="3000";
                    break;
                }
            case R.id.fourYear:
                if(checked){
                    year="four";
                    token="800";
                    amount="4000";
                    break;
                }
        }



        }

}