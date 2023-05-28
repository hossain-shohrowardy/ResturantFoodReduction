package com.example.resturantfoodreduction.view;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.resturantfoodreduction.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;

public class SetResturantLocation extends AppCompatActivity {
    Button setlocation;
    String email,password,phoneNumber,resturentName;
    ImageView imageView;
    int PLACE_PICKER_REQUEST=1;
    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_resturant_location);
        setlocation = (Button) findViewById(R.id.location_choose_button);
        imageView=(ImageView)findViewById(R.id.location_image);
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        password = extras.getString("password");
        phoneNumber = extras.getString("phoneNumber");
        resturentName = extras.getString("resturantName");
        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                            if (result.getResultCode() == RESULT_OK /*&& result.getData()!=null*/) {
                                /*Bundle bundle = result.getData().getExtras();
                                Bitmap bitmap=(Bitmap) bundle.get("data");
                                imageView.setImageBitmap(bitmap);
                            }*/
                               Place place = Autocomplete.getPlaceFromIntent(result.getData());

                                //place.getLatLng();
                                place.getId();
                                place.getName();
                               // Toast.makeText(getApplicationContext(), "place"+"  "+ place.getName()+"  "+
                               // "placeID"+"  "+place.getName(), Toast.LENGTH_SHORT).show();

                            } /*else if ( result.getResultCode()== AutocompleteActivity.RESULT_ERROR) {
                                // TODO: Handle the error.
                               // Status status = Autocomplete.getStatusFromIntent(result.getData());
                               // Log.i(TAG, status.getStatusMessage());
                            } else if ( result.getResultCode() == RESULT_CANCELED) {
                                 //The user canceled the operation.
                            }*/

                        }


                });

    }





    public void fetchLocation(View view) {
       // Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,
        //Arrays.asList(Place.Field.ID,Place.Field.NAME)).build(this);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        
       // Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            activityResultLauncher.launch(intent);

        }
        //activityResultLauncher.launch(intent);






    }
    /*rotected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==PLACE_PICKER_REQUEST ) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
   // }

}