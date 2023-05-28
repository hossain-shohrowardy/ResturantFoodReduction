package com.example.resturantfoodreduction.view.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.resturantfoodreduction.FcmNotificationsSender;
import com.example.resturantfoodreduction.R;
import com.example.resturantfoodreduction.view.ResturantOwnerModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminHomeAdapter extends RecyclerView.Adapter<AdminHomeAdapter.AdminHomeViewHolder> {
    ArrayList<ResturantOwnerModel> list;
    Activity activity;

    public AdminHomeAdapter(ArrayList<ResturantOwnerModel> list,Activity activity) {
        this.list = list;
        this.activity=activity;
    }

    @NonNull
    @Override
    public AdminHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_restaurant_info,parent,false);
        return new AdminHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminHomeViewHolder holder, int position) {
        final ResturantOwnerModel temp=list.get(position);
        String approveState=temp.getApproveState();
        Glide.with(holder.name.getContext()).load(temp.getImguri()).into(holder.resImage);
        holder.name.setText("Name : "+temp.getName());
        holder.email.setText("Email : "+temp.getEmail());
        holder.phoneNo.setText("Phone No : "+temp.getPhoneNumber());
        holder.validity.setText("Validity : "+temp.getValidity());
        holder.foodToken.setText("Token : "+temp.getFoodToken()+" tokens");
        holder.payment.setText("Payment : "+temp.getResAmount()+" Tk");
        if(approveState.equals("yes")){
            holder.approve.setText("APPROVED");
            holder.approve.setEnabled(false);
        }
        else if(approveState.equals("no")) {
            holder.approve.setText("APPROVE");
            holder.approve.setEnabled(true);
        }

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                String uId=temp.getUserId();
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.approve.getContext());
                builder.setTitle("Approve Restaurant");
                builder.setMessage("Do you want to approve this restaurant?");
                builder.setPositiveButton("Yes", (dialogInterface, i) ->{
                    if(uId!=null) {
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(uId).child("approveState").setValue("yes");
                        FirebaseDatabase.getInstance().getReference().child("User").child(uId).child("approveState").setValue("yes");
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(uId).child("seenState").setValue("yes");
                        FirebaseDatabase.getInstance().getReference().child("User").child(uId).child("seenState").setValue("yes");
                        holder.approve.setText("APPROVED");
                        holder.approve.setEnabled(false);

                            FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(temp.getToken(), "Approved Account","Dear "+temp.getName()+", you can use your account now.",holder.approve.getContext(),activity);
                            fcmNotificationsSender.SendNotifications();



                    }
                   // list.remove(pos);
                   // notifyItemRemoved(pos);
                   // notifyItemRangeChanged(pos, list.size());


                });
               // Toast.makeText(holder.deleteImg.getContext(), "hi", Toast.LENGTH_SHORT).show();
                // FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(userId).child("Food").
                //child(foodId).setValue(null);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public void filterList(ArrayList<ResturantOwnerModel> filterList){
        list=filterList;
        notifyDataSetChanged();
    }

    class AdminHomeViewHolder extends RecyclerView.ViewHolder{
        ImageView resImage;
        TextView name,email,phoneNo,validity,foodToken,payment;
        Button approve;

        public AdminHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            resImage=(ImageView)itemView.findViewById(R.id.res_img);
            name=(TextView)itemView.findViewById(R.id.res_name);
            email=(TextView)itemView.findViewById(R.id.res_email);
            phoneNo=(TextView)itemView.findViewById(R.id.res_phone);
            validity=(TextView)itemView.findViewById(R.id.res_validity);
            foodToken=(TextView)itemView.findViewById(R.id.res_food_token);
            payment=(TextView)itemView.findViewById(R.id.res_payment);
            approve=(Button) itemView.findViewById(R.id.res_approve);

        }
    }
}
