package com.example.resturantfoodreduction.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.resturantfoodreduction.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResturantOwnerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResturantOwnerHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    String userId;
    ArrayList<FoodInfoModel> list;
    RecyclerView recyclerView;
    ResturantFoodsAdapter resturantFoodsAdapter;
    ResturantFoodAdapter resturantFoodAdapter;
    DatabaseReference ref;
    SearchView searchView;

    public ResturantOwnerHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResturantOwnerHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResturantOwnerHomeFragment newInstance(String param1, String param2) {
        ResturantOwnerHomeFragment fragment = new ResturantOwnerHomeFragment();
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
      view= inflater.inflate(R.layout.fragment_resturant_owner_home, container, false);
       recyclerView= (RecyclerView)view.findViewById(R.id.resturant_home_recyclerview);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView=view.findViewById(R.id.res_home_search);
       userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
      // userId="W6gwP4DmPiTawqaTkZmys0YFNFQ2";
       //list=new ArrayList<FoodInfoModel>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("FoodOrderByResturant").
                child(userId).child("Foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    list=new ArrayList<FoodInfoModel>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        FoodInfoModel foodInfoModel = ds.getValue(FoodInfoModel.class);
                        //Toast.makeText(getActivity(), "foodname: "+foodsInfoModel.getFoodId(), Toast.LENGTH_SHORT).show();
                        list.add(foodInfoModel);



                    }
                    resturantFoodAdapter=new ResturantFoodAdapter(list);
                   // recyclerView.setAdapter(new ResturantFoodAdapter(list));
                    recyclerView.setAdapter(resturantFoodAdapter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        if(list.size()!=0){
//            recyclerView.setAdapter(new ResturantFoodAdapter(list));}

        //resturantFoodsAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        //resturantFoodsAdapter.stopListening();
    }

    public void filter(String query){
        ArrayList<FoodInfoModel> filterList=new ArrayList<>();
        for (FoodInfoModel item:list){
            if(item.getTitle().toLowerCase().contains(query.toLowerCase())){
                filterList.add(item);
            }
        }

        resturantFoodAdapter.filterList(filterList);
    }
}