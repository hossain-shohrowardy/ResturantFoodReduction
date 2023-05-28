package com.example.resturantfoodreduction.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturantfoodreduction.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class ResturantFoodsAdapter extends FirebaseRecyclerAdapter<FoodsInfoModel,ResturantFoodsAdapter.ResturantFoodsViewHolder> {


    public ResturantFoodsAdapter(@NonNull FirebaseRecyclerOptions<FoodsInfoModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ResturantFoodsViewHolder holder, int position, @NonNull FoodsInfoModel model) {
        //holder.foodImg.setImageResource(R.drawable.food_default);
       // String s=model.getTitle();
        //holder.title.setText(model.getTitle());//+" "+model.getDiscount()+"% Discount"
       // holder.price.setText(model.getLatestPice());
       // holder.editImg.setImageResource(R.drawable.edit_best);
        //holder.userImg.setImageResource(R.drawable.view_customers);
       // holder.deleteImg.setImageResource(R.drawable.delete_icon);
    }

    @NonNull
    @Override
    public ResturantFoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.resturant_food_layout,parent,false);
        return new ResturantFoodsViewHolder(view);
    }

    public class ResturantFoodsViewHolder extends RecyclerView.ViewHolder{
        ImageView foodImg,editImg,userImg,deleteImg;
        TextView title,price;

        public ResturantFoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg=itemView.findViewById(R.id.food_img);
            editImg=itemView.findViewById(R.id.single_food_edit);
            userImg=itemView.findViewById(R.id.view_customers);
            deleteImg=itemView.findViewById(R.id.single_food_delete);
            title=itemView.findViewById(R.id.single_food_title);
            price=itemView.findViewById(R.id.single_food_res_price);
        }
    }
}
