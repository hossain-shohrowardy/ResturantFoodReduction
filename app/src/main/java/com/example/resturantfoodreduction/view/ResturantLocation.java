package com.example.resturantfoodreduction.view;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ResturantLocation implements LocationListener {
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude;
        double longitude;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("latitude").setValue(latitude);
            FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("longitude").setValue(longitude);}
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
