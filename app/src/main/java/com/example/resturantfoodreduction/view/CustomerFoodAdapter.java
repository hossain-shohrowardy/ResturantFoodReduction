package com.example.resturantfoodreduction.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomerFoodAdapter extends RecyclerView.Adapter<CustomerFoodAdapter.CustomerFoodViewHolder> {
    ArrayList<ResturantInfoAndFoodInfoModel> list;
    Activity activity;

    public CustomerFoodAdapter(ArrayList<ResturantInfoAndFoodInfoModel> list, Activity activity) {
        this.list = list;
        this.activity=activity;
    }

    @NonNull
    @Override
    public CustomerFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_food_layout,parent,false);

        return new CustomerFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerFoodViewHolder holder, int position) {
        final ResturantInfoAndFoodInfoModel temp = list.get(position);
        String cusFoodId=temp.getFoodId();
        String resUserId=temp.getUserId();
        String cusUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String resToken=temp.getResToken();

         int pos=list.indexOf(temp);
        Glide.with(holder.foodImg.getContext()).load(list.get(position).getImguri()).into(holder.foodImg);
        holder.title.setText(list.get(position).getTitle()+" is going on "+ list.get(position).getDiscount().toString()
                +"% discount, quantity:- "+list.get(position).getQuentity().toString());
        holder.price.setText(list.get(position).getLatestPice().toString()+"TK");
        holder.foodView.setImageResource(R.drawable.view_food_icon_cus);
        holder.resturantImg.setImageResource(R.drawable.restaurant_view_icon);
       holder.deleteImg.setImageResource(R.drawable.delete_customer_icon);
        holder.foodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                String foodId=temp.getFoodId();
                if(userId!=null && foodId!=null) {
                    FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").
                            child(userId).child("Food").child(foodId).child("seenState").setValue("yes");
                }
                Intent intent=new Intent(holder.foodView.getContext(),ViewSingleFood.class);
                intent.putExtra("foodId",temp.getFoodId());
                holder.foodView.getContext().startActivity(intent);

            }
        });
        holder.resturantImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.resturantImg.getContext(),ViewResturantForCustomer.class);
                intent.putExtra("foodId",temp.getFoodId());
                holder.resturantImg.getContext().startActivity(intent);
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
                    FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").child(userId).child("Food").
                        child(foodId).removeValue();
                    list.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, list.size());


                });
                Toast.makeText(holder.deleteImg.getContext(), "hi", Toast.LENGTH_SHORT).show();
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
        holder.get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resUserId!=null && cusUserId !=null && cusFoodId != null) {
                    FirebaseDatabase.getInstance().getReference().child("FoodReceiveByCustomers").child(resUserId).child(cusFoodId).child("Customers").child(cusUserId).child("customerRequestState").setValue("yes");
                    FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(resToken, "Request To Get Food",cusUserId +" wants to take "+temp.getTitle()+".",holder.get.getContext(),activity);
                    fcmNotificationsSender.SendNotifications();
                }
            }
        });

    }
    public void filterList(ArrayList<ResturantInfoAndFoodInfoModel> filterList){
        list=filterList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CustomerFoodViewHolder extends RecyclerView.ViewHolder{
        ImageView foodImg,resturantImg,foodView,deleteImg;
        TextView title,price;
        Button get;

        public CustomerFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg=itemView.findViewById(R.id.customer_food_img);
            foodView=itemView.findViewById(R.id.customer_single_food_view_icon);
            resturantImg=itemView.findViewById(R.id.customer_view_resturant);
            deleteImg=itemView.findViewById(R.id.customer_single_food_delete);
            title=itemView.findViewById(R.id.customer_single_food_title);
            price=itemView.findViewById(R.id.customer_single_food_price);
            get=(Button) itemView.findViewById(R.id.cus_get_food);
        }
    }
}
