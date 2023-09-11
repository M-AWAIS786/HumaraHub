package com.example.humarahub.ui.home;

import static com.example.humarahub.DBqueries.categoryModelList;
import static com.example.humarahub.DBqueries.lists;
import static com.example.humarahub.DBqueries.loadCategories;
import static com.example.humarahub.DBqueries.loadFragmentData;
import static com.example.humarahub.DBqueries.loadedCategoriesNames;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.humarahub.CategoryAdapter;
import com.example.humarahub.CategoryModel;
import com.example.humarahub.DBqueries;
import com.example.humarahub.HomePageAdapter;
import com.example.humarahub.HomePageModel;
import com.example.humarahub.HorizontalProductScrollModel;
import com.example.humarahub.MainActivity;
import com.example.humarahub.R;
import com.example.humarahub.SliderModel;
import com.example.humarahub.WishListModel;
import com.example.humarahub.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView categoryRecyclerView;
    private List<CategoryModel> categoryModelFakeList=new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private List<HomePageModel> homePageModelFakeList=new ArrayList<>();
    private HomePageAdapter adapter;
    private ImageView notInternetConnection;
    public static SwipeRefreshLayout swipeRefreshLayout;

    private   ConnectivityManager connectivityManager;
    private   NetworkInfo networkInfo;
    private Button retryBtn;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        notInternetConnection = view.findViewById(R.id.no_internet_connection);
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);
        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerView);
        retryBtn=view.findViewById(R.id.retry_btn);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.primaryYellowBtn),getContext().getResources().getColor(R.color.primaryYellowBtn),getContext().getResources().getColor(R.color.primaryYellowBtn));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        adapter = new HomePageAdapter(homePageModelFakeList);



        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);
        //categories fake list
        categoryModelFakeList.add(new CategoryModel("null",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));

        //categories fake list


        //home page fake list
        List<SliderModel> sliderModelFakeList=new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));


        List<HorizontalProductScrollModel>horizontalProductScrollModelFakeList=new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#dfdfdf",horizontalProductScrollModelFakeList,new ArrayList<WishListModel>()));
        homePageModelFakeList.add(new HomePageModel(2,"","#dfdfdf",horizontalProductScrollModelFakeList));
        //home page fake list
        categoryAdapter = new CategoryAdapter(categoryModelFakeList);

//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

       connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
       networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            notInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);


            if(categoryModelList.size()==0)
            {
                loadCategories(categoryRecyclerView,getContext());
            }else{
                categoryAdapter=new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }

            categoryRecyclerView.setAdapter(categoryAdapter);




            if(lists.size()==0)
            {
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                loadFragmentData(homePageRecyclerView,getContext(),0,"Home");
            }else{
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(adapter);

        } else {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.nointernetconnection).into(notInternetConnection);
            notInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);

        }

///////////////////Refresh Layout

swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        reloadPage();

    }
});

///////////////////Refresh Layout
    retryBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            reloadPage();
        }
    });
        return view;
    }


    private void reloadPage()
        {
            networkInfo = connectivityManager.getActiveNetworkInfo();


//            categoryModelList.clear(); // data override nhi hota main page pr clear Func se
//            lists.clear();
//            loadedCategoriesNames.clear();

            DBqueries.clearData();
            if (networkInfo != null && networkInfo.isConnected() == true) {
                MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                notInternetConnection.setVisibility(View.GONE);
                retryBtn.setVisibility(View.GONE);
                categoryRecyclerView.setVisibility(View.VISIBLE);
                homePageRecyclerView.setVisibility(View.VISIBLE);

                categoryAdapter = new CategoryAdapter(categoryModelFakeList);
                adapter = new HomePageAdapter(homePageModelFakeList);
                categoryRecyclerView.setAdapter(categoryAdapter);//ya fake list wala adapter hai same adapter dbqueries me alag se use kia hai
                homePageRecyclerView.setAdapter(adapter);

                loadCategories(categoryRecyclerView, getContext());

                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                loadFragmentData(homePageRecyclerView, getContext(), 0, "Home");


            } else {
                MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Toast.makeText(getContext(), "No internet Connection!", Toast.LENGTH_SHORT).show();
                categoryRecyclerView.setVisibility(View.GONE);
                homePageRecyclerView.setVisibility(View.GONE);
                Glide.with(getContext()).load(R.drawable.nointernetconnection).into(notInternetConnection);
                notInternetConnection.setVisibility(View.VISIBLE);
                retryBtn.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }


//            categoryModelList.clear();
//        lists.clear();
//        loadedCategoriesNames.clear();



