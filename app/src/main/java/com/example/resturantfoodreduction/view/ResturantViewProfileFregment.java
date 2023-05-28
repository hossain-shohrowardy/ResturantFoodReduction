package com.example.resturantfoodreduction.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.resturantfoodreduction.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResturantViewProfileFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResturantViewProfileFregment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    ImageView imageView;
    Button button;
    TextView nameTitle,nameTextView,emailTitle,emailTextView,phoneNoTitle,phoneNoTextView,locationTextView;
    TextView validityTextView,packageTextView;
    String imageuri, userId,name,email;
    String phoneNo;
    String validity,foodPackage;
    String latitude,longitude;
    ArrayList<ResturantOwnerModel> list;
    String validityState="extend";




    public ResturantViewProfileFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResturantViewProfileFregmet.
     */
    // TODO: Rename and change types and number of parameters
    public static ResturantViewProfileFregment newInstance(String param1, String param2) {
        ResturantViewProfileFregment fragment = new ResturantViewProfileFregment();
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
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_resturant_view_profile_fregment, container, false);
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        imageView=(ImageView) view.findViewById(R.id.view_resturant_image);
        nameTextView=(TextView)view.findViewById(R.id.res_name);
        emailTextView=(TextView) view.findViewById(R.id.res_email);
        phoneNoTextView=(TextView) view.findViewById(R.id.res_phone);
        locationTextView=(TextView) view.findViewById(R.id.res_location);
        validityTextView=(TextView) view.findViewById(R.id.res_location);
        packageTextView=(TextView)view.findViewById(R.id.res_package);
        button=(Button) view.findViewById(R.id.validity_extend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(button.getContext(),TokenList.class);
                intent.putExtra("validityState",validityState);
                intent.putExtra("resName",name);
                startActivity(intent);
            }
        });










        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").
                child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    ResturantOwnerModel resturantOwnerModel = snapshot.getValue(ResturantOwnerModel.class);
                    name = resturantOwnerModel.getName();
                    email = resturantOwnerModel.getEmail();
                    phoneNo = String.valueOf(resturantOwnerModel.getPhoneNumber());
                    latitude = resturantOwnerModel.getLatitude().toString();
                    latitude = resturantOwnerModel.getLatitude().toString();
                    imageuri = resturantOwnerModel.getImguri();
                    validity=resturantOwnerModel.getValidity();
                    foodPackage=resturantOwnerModel.getFoodToken();
                    nameTextView.setText(name);
                    phoneNoTextView.setText(phoneNo);
                    emailTextView.setText(email);
                    validityTextView.setText(validity);
                    packageTextView.setText(foodPackage+" Foods");
                    if (imageuri != null) {
                        Glide.with(emailTextView.getContext()).load(imageuri).into(imageView);
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}