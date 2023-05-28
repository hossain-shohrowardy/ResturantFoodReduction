package com.example.resturantfoodreduction.view;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.resturantfoodreduction.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditResturantOwnerProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditResturantOwnerProfile extends Fragment {
    TextInputEditText emailTextInputEditText,passwordTextInputEditText,phoneNumberTextInputEditText,resturantNameTextInputEditText;
    ImageView resturantImageView;
    View view;
    Button editButton;
    ActivityResultLauncher<String> activityResultLauncher;
    Uri imageUri;
    String downloadedImageUri;
    String userId;
    int state=0;
    ResturantOwnerModel resturantOwnerModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditResturantOwnerProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditResturantOwnerProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static EditResturantOwnerProfile newInstance(String param1, String param2) {
        EditResturantOwnerProfile fragment = new EditResturantOwnerProfile();
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
        view=inflater.inflate(R.layout.fragment_edit_resturant_owner_profile, container, false);
        emailTextInputEditText= (TextInputEditText)view.findViewById(R.id.edit_resturant_email);
        passwordTextInputEditText=(TextInputEditText)view.findViewById(R.id.edit_resturant_password);
        resturantNameTextInputEditText=(TextInputEditText)view.findViewById(R.id.edit_resturant_name);
        phoneNumberTextInputEditText=(TextInputEditText)view.findViewById(R.id.edit_resturant_phone_number);
        resturantImageView=(ImageView)view.findViewById(R.id.edit_resturant_image);
        editButton=(Button)view.findViewById(R.id.edit_resturantSignUp);
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if(result!=null){
                            resturantImageView.setImageURI(result);
                            imageUri=result;

                        }


                    }
                });
        resturantImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch("image/*");



            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri!=null) {
                    // Toast.makeText(getActivity(), "change image", Toast.LENGTH_SHORT).show();
                    StorageReference reference = FirebaseStorage.getInstance().getReference().child("ResturantImages").child(userId);
                    reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadedImageUri=uri.toString();
                                    String email=emailTextInputEditText.getText().toString();
                                   String password= passwordTextInputEditText.getText().toString();
                                   String phoneNo= phoneNumberTextInputEditText.getText().toString();
                                   String resName= resturantNameTextInputEditText.getText().toString();
                                    if(!email.isEmpty() || !password.isEmpty()|| !resName.isEmpty() || !phoneNo.isEmpty()){
                                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("name").setValue(resName);
                                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("email").setValue(email);
                                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("password").setValue(password);
                                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("phoneNumber").setValue(Integer.valueOf(phoneNo));
                                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("imguri").setValue(downloadedImageUri);



                                    }
                                }
                            });
                        }
                    });
                }
                else {
                    String email=emailTextInputEditText.getText().toString();
                    String password= passwordTextInputEditText.getText().toString();
                    String phoneNo= phoneNumberTextInputEditText.getText().toString();
                    String resName= resturantNameTextInputEditText.getText().toString();
                    if(!email.isEmpty() || !password.isEmpty()|| !resName.isEmpty() || !phoneNo.isEmpty()){
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("name").setValue(resName);
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("email").setValue(email);
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("password").setValue(password);
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).child("phoneNumber").setValue(Integer.valueOf(phoneNo));



                    }



                }
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(state==0){
                         resturantOwnerModel=snapshot.getValue(ResturantOwnerModel.class);
                        emailTextInputEditText.setText(resturantOwnerModel.getEmail());
                        passwordTextInputEditText.setText(resturantOwnerModel.getPassword());
                        phoneNumberTextInputEditText.setText(resturantOwnerModel.getPhoneNumber().toString());
                        resturantNameTextInputEditText.setText(resturantOwnerModel.getName());
                        if (imageUri != null && FirebaseAuth.getInstance().getUid() != null) {
                            Glide.with(getContext()).load(imageUri.toString()).into(resturantImageView);
                        }
                        state=1;

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}