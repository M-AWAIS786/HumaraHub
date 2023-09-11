package com.example.humarahub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link MyOrdersFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class MyOrdersFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment MyOrdersFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static MyOrdersFragment newInstance(String param1, String param2) {
//        MyOrdersFragment fragment = new MyOrdersFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    private RecyclerView myorderRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        myorderRecyclerView = view.findViewById(R.id.my_orders_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myorderRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.sofa, 2, "Classic Sofa", "Delivered on Mon,18th August 2023"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.classicarmchair, 3, "Classic Chair Arm ", "Delivered on Mon,15th May 2023"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.chairset, 1, "Chair Set", "Delivered on Mon,17th june 2023"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.singleseater, 0, "Single Seat", "Cancelled"));
        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myorderRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();
        return view;
    }
}