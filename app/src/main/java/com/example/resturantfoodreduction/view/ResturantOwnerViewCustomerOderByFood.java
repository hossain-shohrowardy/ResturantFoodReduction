package com.example.resturantfoodreduction.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.resturantfoodreduction.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResturantOwnerViewCustomerOderByFood extends AppCompatActivity {

    RecyclerView recyclerView;
    String userId;
    String foodId;
    String foodName;
    ArrayList<CustomerInfo> list;
    SearchView searchView;
    ResturantFoodWiseCustomer resturantFoodWiseCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_owner_view_customer_oder_by_food);
        searchView=(SearchView) findViewById(R.id.cuses_home_search);
        recyclerView=(RecyclerView) findViewById(R.id.customer_recycler_view_for_res);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        foodId=getIntent().getExtras().getString("foodId");
        foodName=getIntent().getExtras().getString("foodName");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("FoodReceiveByCustomers").child(userId).child(foodId).child("Customers").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            list=new ArrayList<CustomerInfo>();
                            for(DataSnapshot ds:snapshot.getChildren()){
                                CustomerInfo customerInfo=ds.getValue(CustomerInfo.class);
                                list.add(customerInfo);
                            }
                            resturantFoodWiseCustomer= new ResturantFoodWiseCustomer(list,userId,foodId,foodName,ResturantOwnerViewCustomerOderByFood.this);
                            recyclerView.setAdapter(resturantFoodWiseCustomer);
                           // recyclerView.setAdapter(new ResturantFoodWiseCustomer(list,userId,foodId,foodName,ResturantOwnerViewCustomerOderByFood.this));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public void filter(String query){
        ArrayList<CustomerInfo> filterList=new ArrayList<>();
        for (CustomerInfo item:list){
            if(item.getName().toLowerCase().contains(query.toLowerCase())){
                filterList.add(item);
            }
        }

        resturantFoodWiseCustomer.filterList(filterList);
    }
}