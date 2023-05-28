package com.example.resturantfoodreduction.view.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resturantfoodreduction.R;
import com.example.resturantfoodreduction.view.CustomerFoodAdapter;
import com.example.resturantfoodreduction.view.ResturantInfoAndFoodInfoModel;
import com.example.resturantfoodreduction.view.ResturantOwnerModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHomeFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomeFregment extends Fragment {

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
    ArrayList<ResturantOwnerModel> list;
    SearchView searchView;
    AdminHomeAdapter adminHomeAdapter;

    public AdminHomeFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminHomeFregment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHomeFregment newInstance(String param1, String param2) {
        AdminHomeFregment fragment = new AdminHomeFregment();
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
         view =inflater.inflate(R.layout.fragment_admin_home_fregment, container, false);
         searchView=view.findViewById(R.id.admin_home_search);
         recyclerView= (RecyclerView)view.findViewById(R.id.admin_recycler_view);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("ResturantOwner").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        list=new ArrayList<ResturantOwnerModel>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ResturantOwnerModel resturantOwnerModel = ds.getValue(ResturantOwnerModel.class);
                            String approveState=resturantOwnerModel.getApproveState();
                          // list.add(resturantOwnerModel);
                            //if(approveState=="no"){
                                list.add(resturantOwnerModel);
                           // }


                        }
                        // Toast.makeText(getActivity(), "size:" + list.size(), Toast.LENGTH_SHORT).show();
                       // recyclerView.setAdapter(new AdminHomeAdapter(list));
                        adminHomeAdapter=new AdminHomeAdapter(list,getActivity());
                        recyclerView.setAdapter(adminHomeAdapter);
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
        ArrayList<ResturantOwnerModel> filterList=new ArrayList<>();
        for (ResturantOwnerModel item:list){
            if(item.getName().toLowerCase().contains(query.toLowerCase())){
                filterList.add(item);
            }
        }

        adminHomeAdapter.filterList(filterList);
    }
}