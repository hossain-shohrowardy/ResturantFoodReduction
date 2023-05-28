package com.example.resturantfoodreduction.view;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class
CustomerService extends Service {
    String userId;
    LocationListener locationListener;
    DatabaseReference ref;
    public CustomerService() {
    }
    @Override
    public void onCreate() {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            userId= FirebaseAuth.getInstance().getCurrentUser().getUid();}
        super.onCreate();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getLocation();
        //ResturantLocation resturantLocation =new ResturantLocation();
        //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, resturantLocation);


        return super.onStartCommand(intent, flags, startId);

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
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    ref.child("Customer").child(userId).child("latitude").setValue(latitude);
                    ref.child("Customer").child(userId).child("longitude").setValue(longitude);}




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
}