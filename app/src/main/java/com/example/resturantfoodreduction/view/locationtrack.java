package com.example.resturantfoodreduction.view;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class locationtrack implements LocationListener {
    // public static final String chanelDescription="to_location_track";
    public double latitude;
    public double longitude;
    public int i;
    DatabaseReference ref;
    DatabaseReference reference;

    @Override
    public void onLocationChanged(Location location) {
        i=0;
        ref = FirebaseDatabase.getInstance().getReference();
        this.latitude=location.getLatitude();
        this.longitude=location.getLongitude();
        ref.child("admin").child("1").
                child("latitude").setValue(this.latitude);
        ref.child("admin").child("1").
                child("longitude").setValue(this.longitude);



               /* NotificationCompat.Builder mbuilder=
                        new NotificationCompat.Builder(MainActivity.mContext.getApplicationContext(),MainActivity.chanelId)
                                .setSmallIcon(R.drawable.gpsoff)
                                .setContentTitle("Gps off")
                                .setContentText("gps has disabled")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManager notificationManager=(NotificationManager)MainActivity.mContext.getApplicationContext().
                        getSystemService(MainActivity.mContext.getApplicationContext().NOTIFICATION_SERVICE);
                notificationManager.notify(1,mbuilder.build());
                Log.d(TAG, "run:  "+i++);*/



    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
       // Toast.makeText(getApplicationContext(),
              //  "  GPS turned on from "+"1",
               // Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String s) {
       // Toast.makeText(MainActivity.mContext.getApplicationContext(),
              //  "  GPS turned off from "+"1",
              //  Toast.LENGTH_LONG).show();

    }
}
