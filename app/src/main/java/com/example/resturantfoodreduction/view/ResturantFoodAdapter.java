package com.example.resturantfoodreduction.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.resturantfoodreduction.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResturantFoodAdapter extends RecyclerView.Adapter<ResturantFoodAdapter.ResturantViewHolder> {
    ArrayList<FoodInfoModel>list;

    public ResturantFoodAdapter(ArrayList<FoodInfoModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ResturantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.resturant_food_layout,parent,false);

        return new ResturantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResturantViewHolder holder, int position) {

        //holder.foodImg.setImageResource(R.drawable.edit_profile_all);
        // ArrayList<CustomerInfo> customerList;
       final FoodInfoModel temp = list.get(position);
       //final ArrayList<CustomerInfo> customerList;
        String resUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String resFoodId=temp.getFoodId();
        int pos=list.indexOf(temp);
        Glide.with(holder.foodImg.getContext()).load(list.get(position).getImguri()).into(holder.foodImg);
        holder.title.setText(list.get(position).getTitle()+" is going on "+list.get(position).getDiscount().toString()
        +"% discount, quantity:- "+list.get(position).getQuentity().toString());
       holder.price.setText(list.get(position).getLatestPice().toString()+"TK");
       holder.foodView.setImageResource(R.drawable.view_food_resturant_customer);
        holder.editImg.setImageResource(R.drawable.edit_best);
        holder.userImg.setImageResource(R.drawable.view_customers);
        holder.deleteImg.setImageResource(R.drawable.delete_icon);
        if(resUserId!=null && resUserId!=null) {

            FirebaseDatabase.getInstance().getReference().child("FoodReceiveByCustomers").child(resUserId).child(resFoodId).child("Customers").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                         ArrayList<CustomerInfo>customerInfoArrayList=new ArrayList<CustomerInfo>();
                        for (DataSnapshot ds:snapshot.getChildren()){
                            CustomerInfo customerInfo=ds.getValue(CustomerInfo.class);
                           String requestState= customerInfo.getCustomerRequestState();
                           String approveState=customerInfo.getCustomerApproveState();
                            if (requestState.equals("yes") && approveState.equals("yes")){
                                customerInfoArrayList.add(customerInfo);
                            }
                            Integer total=customerInfoArrayList.size();
                            holder.totalApproveCustomer.setText(total.toString());
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.editImg.getContext(),ResturentFoodEdit.class);
                intent.putExtra("foodId",temp.getFoodId());
                holder.editImg.getContext().startActivity(intent);
            }
        });
        holder.foodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.foodView.getContext(),ResturantViewFood.class);
                intent.putExtra("foodId",temp.getFoodId());
                holder.foodView.getContext().startActivity(intent);

            }
        });
        holder.userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.userImg.getContext(),
                        ResturantOwnerViewCustomerOderByFood.class);
                intent.putExtra("foodId",temp.getFoodId());
                intent.putExtra("foodName",temp.getTitle());
                holder.userImg.getContext().startActivity(intent);
            }
        });
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                String foodId=temp.getFoodId();
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.deleteImg.getContext());
                builder.setTitle("Delete Food");
                builder.setMessage("Do you want to delete this food?");
                builder.setPositiveButton("Yes", (dialogInterface, i) ->{
                    if(userId!=null && foodId!=null) {
                       // FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                //child(userId).child("Foods").child(foodId).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("FoodReceiveByCustomers").child(userId).child(foodId).
                                child("Customers").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        CustomerInfo cusInfo = ds.getValue(CustomerInfo.class);
                                        String uId = cusInfo.getUserId();
                                        Toast.makeText(holder.deleteImg.getContext(), "userId:"+uId, Toast.LENGTH_SHORT).show();
                                        if (uId != null && foodId != null) {
                                            FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(uId).child("Food").
                                                   child(foodId).removeValue();
                                        }


                                    }
                                    FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                                            child(userId).child("Foods").child(foodId).removeValue();
                                    list.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, list.size());

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    //list.remove(pos);
                   //notifyItemRemoved(pos);
                   //notifyItemRangeChanged(pos, list.size());


                });
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
    public void filterList(ArrayList<FoodInfoModel> filterList){
        list=filterList;
        notifyDataSetChanged();
    }

    class ResturantViewHolder extends RecyclerView.ViewHolder{
        ImageView foodImg,editImg,userImg,deleteImg,foodView;
        TextView title,price,totalApproveCustomer;



        public ResturantViewHolder(@NonNull View itemView) {
            super(itemView);
            foodView=itemView.findViewById(R.id.single_food_view);
            foodImg=itemView.findViewById(R.id.food_img);
            editImg=itemView.findViewById(R.id.single_food_edit);
            userImg=itemView.findViewById(R.id.view_customers);
            deleteImg=itemView.findViewById(R.id.single_food_delete);
            title=itemView.findViewById(R.id.single_food_title);
            totalApproveCustomer=itemView.findViewById(R.id.cus_total);
            price=itemView.findViewById(R.id.single_food_res_price);


        }
    }
}
