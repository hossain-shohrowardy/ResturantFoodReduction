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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.resturantfoodreduction.FcmNotificationsSender;
import com.example.resturantfoodreduction.R;
import com.example.resturantfoodreduction.view.ResturantOwnerModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminExtendValidityAdapter extends RecyclerView.Adapter<AdminExtendValidityAdapter.AdminExtendValidityViewHolder> {
    ArrayList<ResturantOwnerModel> list;
    Activity activity;

    public AdminExtendValidityAdapter(ArrayList<ResturantOwnerModel> list,Activity activity) {
        this.list = list;
        this.activity=activity;
    }

    @NonNull
    @Override
    public AdminExtendValidityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_res_extend_validity,parent,false);

        return new AdminExtendValidityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminExtendValidityViewHolder holder, int position) {
        final ResturantOwnerModel temp=list.get(position);
        String extendValidityState=temp.getExtendValidityState();
        String approveState=temp.getApproveState();
        Glide.with(holder.name.getContext()).load(temp.getImguri()).into(holder.resImage);
        holder.name.setText("Name : "+temp.getName());
        holder.email.setText("Email : "+temp.getEmail());
        holder.phoneNo.setText("Phone No : "+temp.getPhoneNumber());
        holder.validity.setText("Validity : "+temp.getValidity());
        holder.foodToken.setText("Token : "+temp.getFoodToken()+" tokens");
        holder.payment.setText("Payment : "+temp.getResAmount()+" Tk");
        /*if(extendValidityState.equals("yes")){
            holder.extend.setText("EXTEND");
            holder.extend.setEnabled(true);

        }
        else if (extendValidityState.equals("no")) {
            holder.extend.setText("EXTENDED");
            holder.extend.setEnabled(false);

        }*/

        holder.extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uId=temp.getUserId();
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.extend.getContext());
                builder.setTitle("Approval of Extend Validity");
                builder.setMessage("Do you want to appove the applied extend validity of this restaurant");
                builder.setPositiveButton("Yes", (dialogInterface, i) ->{
                    if(uId!=null) {
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(uId).child("extendValidityState").setValue("no");
                        FirebaseDatabase.getInstance().getReference().child("User").child(uId).child("extendValidityState").setValue("no");
                        FirebaseDatabase.getInstance().getReference().child("ResturantOwner").child(uId).child("seenState").setValue("yes");
                        FirebaseDatabase.getInstance().getReference().child("User").child(uId).child("seenState").setValue("yes");
                        //holder.extend.setText("EXTENDED");
                       // holder.extend.setEnabled(false);
                        FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(temp.getToken(), "Approved Extend Validity","Dear "+temp.getName()+",  your extention of validity is successfully approved.",holder.extend.getContext(),activity);
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


    class AdminExtendValidityViewHolder extends RecyclerView.ViewHolder{
        ImageView resImage;
        TextView name,email,phoneNo,validity,foodToken,payment;
        Button extend;

        public AdminExtendValidityViewHolder(@NonNull View itemView) {
            super(itemView);
            resImage=(ImageView)itemView.findViewById(R.id.res_extend_img);
            name=(TextView)itemView.findViewById(R.id.res_name);
            email=(TextView)itemView.findViewById(R.id.res_email);
            phoneNo=(TextView)itemView.findViewById(R.id.res_phone);
            validity=(TextView)itemView.findViewById(R.id.res_extend);
            foodToken=(TextView)itemView.findViewById(R.id.res_extend_food);
            payment=(TextView)itemView.findViewById(R.id.res_extend_payment);
            extend=(Button) itemView.findViewById(R.id.res_extend_approve);

        }
    }
}
