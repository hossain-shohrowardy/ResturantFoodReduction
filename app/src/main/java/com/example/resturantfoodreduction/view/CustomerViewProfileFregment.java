package com.example.resturantfoodreduction.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Use the {@link CustomerViewProfileFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerViewProfileFregment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    ImageView imageView;
    TextView nameTitle,nameTextView,emailTitle,emailTextView,phoneNoTitle,phoneNoTextView,locationTextView;
    String imageuri, userId,name,email;
    String phoneNo;
    String latitude,longitude;
    ArrayList<ResturantOwnerModel> list;

    public CustomerViewProfileFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerViewProfileFregment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerViewProfileFregment newInstance(String param1, String param2) {
        CustomerViewProfileFregment fragment = new CustomerViewProfileFregment();
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
       view= inflater.inflate(R.layout.fragment_customer_view_profile_fregment, container, false);
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        imageView=(ImageView) view.findViewById(R.id.view_customer_image);
        nameTextView=(TextView)view.findViewById(R.id.cus_name);
        emailTextView=(TextView) view.findViewById(R.id.cus_email);
        phoneNoTextView=(TextView) view.findViewById(R.id.cus_phone);
        //locationTextView=(TextView) view.findViewById(R.id.cus_location);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       if (FirebaseAuth.getInstance().getUid()!=null) {
           FirebaseDatabase.getInstance().getReference().child("Customer").
                   child(userId).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()) {
                       CustomerRegistrationModel customerRegistrationModel = snapshot.getValue(CustomerRegistrationModel.class);
                       name = customerRegistrationModel.getName();
                       email = customerRegistrationModel.getEmail();
                       phoneNo = customerRegistrationModel.getPhoneNumber().toString();
                       latitude = customerRegistrationModel.getLatitude().toString();
                       latitude = customerRegistrationModel.getLatitude().toString();
                       imageuri = customerRegistrationModel.getImguri();
                       nameTextView.setText(name);
                       phoneNoTextView.setText(phoneNo);
                       emailTextView.setText(email);
                       if (imageuri != null && FirebaseAuth.getInstance().getUid()!=null) {
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
}