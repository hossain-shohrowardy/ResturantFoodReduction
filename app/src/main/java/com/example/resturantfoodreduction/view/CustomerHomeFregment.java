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
import android.widget.Toast;

import com.example.resturantfoodreduction.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerHomeFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerHomeFregment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    RecyclerView recyclerView;
    String userId;
    ArrayList<ResturantInfoAndFoodInfoModel> list;
    SearchView searchView;
    CustomerFoodAdapter customerFoodAdapter;

    public CustomerHomeFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerHomeFregment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerHomeFregment newInstance(String param1, String param2) {
        CustomerHomeFregment fragment = new CustomerHomeFregment();
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
        view=inflater.inflate(R.layout.fragment_customer_home_fregment, container, false);
        recyclerView= (RecyclerView)view.findViewById(R.id.customer_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView=view.findViewById(R.id.cus_home_search);
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

       // userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
       // userId="An5EuosTNSYQ5OOKoNOj0O8nFqv2";
       // list=new ArrayList<ResturantInfoAndFoodInfoModel>();

        //recyclerView.setAdapter(new CustomerFoodAdapter(list));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("FoodOrderByCustomer").
                child(userId).child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    list=new ArrayList<ResturantInfoAndFoodInfoModel>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ResturantInfoAndFoodInfoModel resturantInfoAndFoodInfoModel = ds.getValue(ResturantInfoAndFoodInfoModel.class);
                        list.add(resturantInfoAndFoodInfoModel);

                    }
                   // Toast.makeText(getActivity(), "size:" + list.size(), Toast.LENGTH_SHORT).show();
                    customerFoodAdapter=new CustomerFoodAdapter(list,getActivity());
                    //recyclerView.setAdapter(new CustomerFoodAdapter(list));
                    recyclerView.setAdapter(customerFoodAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}
//        if(list.size()!=0){
//        recyclerView.setAdapter(new CustomerFoodAdapter(list));}
       // super.onStart();
    }
    public void filter(String query){
        ArrayList<ResturantInfoAndFoodInfoModel> filterList=new ArrayList<>();
        for (ResturantInfoAndFoodInfoModel item:list){
            if(item.getTitle().toLowerCase().contains(query.toLowerCase())){
                filterList.add(item);
            }
        }

        customerFoodAdapter.filterList(filterList);
    }
}