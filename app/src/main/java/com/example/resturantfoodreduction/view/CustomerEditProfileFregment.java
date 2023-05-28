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
import android.widget.Toast;

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
 * Use the {@link CustomerEditProfileFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerEditProfileFregment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextInputEditText emailTextInputEditText, passwordTextInputEditText, reTypePasswordTextInputEditText, phoneNumberTextInputEditText, customerNameTextInputEditText;
    ImageView customerImageView, backImageView;
    ActivityResultLauncher<String> activityResultLauncher;
    Uri imageUri;
    String email,name, password, reTypePassword, resturantName,phoneNumberString;
    String userName,userId,userEmail;
    private FirebaseAuth mAuth;
    int  phoneNumber;
    String downloadedImageUri;
    FirebaseStorage storage;
    Button editButton;
    View view;
    CustomerRegistrationModel customerRegistrationModel;
    int state=0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerEditProfileFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerEditProfileFregment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerEditProfileFregment newInstance(String param1, String param2) {
        CustomerEditProfileFregment fragment = new CustomerEditProfileFregment();
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
        view=inflater.inflate(R.layout.fragment_customer_edit_profile_fregment, container, false);
        emailTextInputEditText = (TextInputEditText) view.findViewById(R.id.edit_customer_email);
        passwordTextInputEditText = (TextInputEditText)view. findViewById(R.id.edit_customer_password);
        customerNameTextInputEditText = (TextInputEditText)view. findViewById(R.id.edit_customer_name);
        phoneNumberTextInputEditText = (TextInputEditText)view. findViewById(R.id.edit_customer_phone_number);
        customerImageView = (ImageView)view. findViewById(R.id.edit_customer_image);
        editButton = (Button) view.findViewById(R.id.edit_customerEdit);
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();


        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if(result!=null){
                            customerImageView.setImageURI(result);
                            imageUri=result;

                        }


                    }
                });
        customerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch("image/*");



            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri!=null){
                   // Toast.makeText(getActivity(), "change image", Toast.LENGTH_SHORT).show();
                    StorageReference reference =FirebaseStorage.getInstance().getReference().child("CustomerImages").child(userId);
                    reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadedImageUri=uri.toString();
                                  //  Toast.makeText(getContext(), "uri:"+downloadedImageUri, Toast.LENGTH_SHORT).show();

                                    email=emailTextInputEditText.getText().toString();
                                   password=passwordTextInputEditText.getText().toString();
                                    name=customerNameTextInputEditText.getText().toString();
                                    phoneNumberString=phoneNumberTextInputEditText.getText().toString();
                                    if(!email.isEmpty() || !password.isEmpty()|| !name.isEmpty() || !phoneNumberString.isEmpty()){


                                        FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).
                                                child("email").setValue(email);
                                        FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).
                                               child("name") .setValue(name);
                                        FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).
                                               child("password").setValue(password);
                                        FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).
                                                child("phoneNumber").setValue(Integer.valueOf(phoneNumberString));
                                        FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).
                                                child("imguri").setValue(downloadedImageUri);
                                    }
                                }
                            });

                        }
                    });
                }
                else{
                    email=emailTextInputEditText.getText().toString();
                    password=passwordTextInputEditText.getText().toString();
                    name=customerNameTextInputEditText.getText().toString();
                    phoneNumberString=phoneNumberTextInputEditText.getText().toString();
                    if(!email.isEmpty() || !password.isEmpty()|| !name.isEmpty() || !phoneNumberString.isEmpty()){
                        FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).
                                child("email").setValue(email);
                        FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).
                                child("name") .setValue(name);
                        FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).
                                child("password").setValue(password);
                        FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).
                                child("phoneNumber").setValue(Integer.valueOf(phoneNumberString));

                    }


                }
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getUid()!=null) {
            FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (state == 0) {
                            customerRegistrationModel = snapshot.getValue(CustomerRegistrationModel.class);
                            emailTextInputEditText.setText(customerRegistrationModel.getEmail());
                            passwordTextInputEditText.setText(customerRegistrationModel.getPassword());
                            customerNameTextInputEditText.setText(customerRegistrationModel.getName());
                            phoneNumberTextInputEditText.setText(customerRegistrationModel.getPhoneNumber().toString());
                            imageUri = Uri.parse(customerRegistrationModel.getImguri());
                            if (imageUri != null && FirebaseAuth.getInstance().getUid() != null) {
                                Glide.with(getContext()).load(imageUri.toString()).into(customerImageView);
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
}