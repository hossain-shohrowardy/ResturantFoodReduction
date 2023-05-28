package com.example.resturantfoodreduction.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.resturantfoodreduction.FcmNotificationsSender;
import com.example.resturantfoodreduction.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ResturantFoodWiseCustomer extends RecyclerView.Adapter<ResturantFoodWiseCustomer.ResturantFoodWiseCustomerViewHolder> {
    ArrayList<CustomerInfo> list;
    String userId;
    String foodId;
    String foodName;
    Activity activity;

    public ResturantFoodWiseCustomer(ArrayList<CustomerInfo> list, String userId, String foodId,String foodName, Activity activity) {
        this.list = list;
        this.userId=userId;
        this.foodId=foodId;
        this.foodName=foodName;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ResturantFoodWiseCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_customer_info,parent,false);
        return new ResturantFoodWiseCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResturantFoodWiseCustomerViewHolder holder, int position) {
       final CustomerInfo temp= list.get(position);
       String requestState=temp.getCustomerRequestState();
       String approveState=temp.getCustomerApproveState();
       String customerId=temp.getUserId();
      String cusToken =temp.getToken();
        Glide.with(holder.imageView.getContext()).load(list.get(position).getCusImg()).into(holder.imageView);
        holder.customerName.setText(" Name: "+list.get(position).getName());
        holder.customerPhoneNo.setText(" Phone No: "+list.get(position).getPhoneNumber().toString());
        holder.customerEmail.setText(" Email: "+list.get(position).getEmail());
        if(requestState.equals("no") && approveState.equals("no")){
            holder.approve.setVisibility(View.INVISIBLE);
        }
        else{
            if(requestState.equals("yes") && approveState.equals("yes")){
                holder.approve.setVisibility(View.VISIBLE);
                holder.approve.setText("APPROVED");
                holder.approve.setEnabled(false);
            }
            else if(requestState.equals("yes") && approveState.equals("no")){
                holder.approve.setVisibility(View.VISIBLE);
                holder.approve.setText("APPROVE");
                holder.approve.setEnabled(true);
            }
        }

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userId !=null && foodId !=null && customerId !=null){
                    FirebaseDatabase.getInstance().getReference().child("FoodReceiveByCustomers").child(userId).child(foodId).child("Customers").child(customerId).child("customerApproveState").setValue("yes");
                    holder.approve.setText("APPROVED");
                    holder.approve.setEnabled(false);
                    FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(cusToken, "Approved Requested Food","Dear "+customerId +" now you can take your resquested food "+foodName+".",holder.approve.getContext(),activity);
                    fcmNotificationsSender.SendNotifications();


                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<CustomerInfo> filterList){
        list=filterList;
        notifyDataSetChanged();
    }

    class ResturantFoodWiseCustomerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView customerName,customerEmail,customerPhoneNo;
        Button approve;

        public ResturantFoodWiseCustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.img_customer);
            customerEmail=itemView.findViewById(R.id.email_customer);
            customerPhoneNo=itemView. findViewById(R.id.phone_customer);
            customerName=itemView.findViewById(R.id.name_customer);
            approve=(Button) itemView.findViewById(R.id.approve_customer);

        }
    }
}
