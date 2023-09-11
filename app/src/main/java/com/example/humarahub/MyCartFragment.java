package com.example.humarahub;

import static com.example.humarahub.DBqueries.cartItemModelList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MyCartFragment extends Fragment {



    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemsRecyclerView;
    private Button checkOut;

    private RecyclerView wishListRecyclerView;
    private Dialog loadingDialog;
    public static cartItemAdapter cartAdapter;

  private  TextView totalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_cart, container, false);
        //        loading dialog

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();


//        loading dialog




        cartItemsRecyclerView=view.findViewById(R.id.cart_items_recyclerview);
//        checkOut=view.findViewById(R.id.delivery_checkout_btn);
//        checkOut=view.findViewById(R.id.cart_checkout_btn);
        checkOut=view.findViewById(R.id.cart_checkout_btn);
        totalAmount=view.findViewById(R.id.total_cart_amount);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);




          cartAdapter=new cartItemAdapter(DBqueries.cartItemModelList,totalAmount,true);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();





        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeliveryActivity.cartItemModelList = new ArrayList<>();
                DeliveryActivity.fromCart=true;
                for (int x = 0; x < cartItemModelList.size(); x++)  //jo stock me avaible product jhain wo nikal rhe hain
                {
                    cartItemModel cartItemModel = cartItemModelList.get(x);
                    if (cartItemModel.isInstock()) {
                        DeliveryActivity.cartItemModelList.add(cartItemModel);
                    }
                }
                DeliveryActivity.cartItemModelList.add(new cartItemModel(cartItemModel.TOTAL_AMOUNT));  //agr product hian to delivery me item or total amount
                //layout show krwaya hai
                loadingDialog.show();
                if (DBqueries.addressesModelList.size() == 0) {
                    DBqueries.loadAddresses(getContext(), loadingDialog);  //
                 }else{
                    loadingDialog.dismiss();
                    Intent deliveryIntent=new Intent(getContext(),DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(DBqueries.cartItemModelList.size()==0){
            DBqueries.cartList.clear();
            DBqueries.loadCartList(getContext(),loadingDialog,true,new TextView(getContext()),totalAmount);
        }else{
//            totalamount layout tabhi show hoga jab cart me items hain wo condition another place me hai

            if(DBqueries.cartItemModelList.get(DBqueries.cartItemModelList.size()-1).getType()==cartItemModel.TOTAL_AMOUNT);
            {
                LinearLayout parent=(LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();
        }
    }
}