package com.example.humarahub;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MyWishListFragment extends Fragment {


    public MyWishListFragment() {
        // Required empty public constructor
    }

private RecyclerView wishListRecyclerView;
    private Dialog loadingDialog;
    public static WishListAdapter wishListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_wish_list, container, false);
        //        loading dialog

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();


//        loading dialog



        wishListRecyclerView=view.findViewById(R.id.my_wishlist_recyclerView);

// Create a GridLayoutManager object with 2 columns
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
// Set the layout manager for the RecyclerView
        wishListRecyclerView.setLayoutManager(layoutManager);


if(DBqueries.wishListModelList.size()==0){
    DBqueries.wishList.clear();
    DBqueries.loadWishList(getContext(),loadingDialog,true);
}else{
    loadingDialog.dismiss();
}
         wishListAdapter=new WishListAdapter(DBqueries.wishListModelList,true);
        wishListRecyclerView.setAdapter(wishListAdapter);
        wishListAdapter.notifyDataSetChanged();



// Create an adapter object and set it to the RecyclerView
//        MyAdapter adapter = new MyAdapter(data);
//        recyclerView.setAdapter(adapter);


        return view;
    }
}