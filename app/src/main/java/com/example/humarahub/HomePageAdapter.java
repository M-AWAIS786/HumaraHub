package com.example.humarahub;

import static com.example.humarahub.SeeAllActivity.horizontalProductScrollModelList;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int lastPosition=-1;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;
            case 2:
                return HomePageModel.GRID_PRODUCT_VIEW;

            default:
                return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout, parent, false);
                return new BannerSliderViewholder(view);
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizonatalProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductViewholder(horizonatalProductView);
            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);
                return new GridProductViewholder(gridProductView);
            default:
                return null;
        }
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewholder) holder).setBannerSliderViewPager(sliderModelList);
                break;
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                String layoutcolor = homePageModelList.get(position).getBackgroundColor();
                String horizontalLayouttitle = homePageModelList.get(position).getTitle();
                List<HorizontalProductScrollModel> horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                List<WishListModel> seeAllProductList = homePageModelList.get(position).getSeeAllProductList();
                ((HorizontalProductViewholder) holder).setHorizontalProductLayout(horizontalProductScrollModelList, horizontalLayouttitle, layoutcolor, seeAllProductList);
                break;
            case HomePageModel.GRID_PRODUCT_VIEW:
                String gridLayoutColor = homePageModelList.get(position).getBackgroundColor();
                String gridlayouttitle = homePageModelList.get(position).getTitle();
                List<HorizontalProductScrollModel> gridProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((GridProductViewholder) holder).setGridProductLayout(gridProductScrollModelList, gridlayouttitle, gridLayoutColor);
                break;

            default:
                return;
        }

        if(lastPosition< holder.getAdapterPosition()) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = holder.getAdapterPosition(); //aik bar anumatin show krwane k bad dobara load nhi krna
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }


    public class BannerSliderViewholder extends RecyclerView.ViewHolder {
        private ViewPager bannerSliderViewPager;
        private int currentPage = 2;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;

        public BannerSliderViewholder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.banner_slider_viewPager);


        }


        private void setBannerSliderViewPager(List<SliderModel> sliderModelList) {
            startbannerSlideshow(sliderModelList);
            SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setCurrentItem(currentPage);
        }

        public void startbannerSlideshow(List<SliderModel> sliderModelList) {
            Handler handler = new Handler();
            Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size()) {
                        currentPage = 1;
                    }

                    bannerSliderViewPager.setCurrentItem(currentPage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIME, PERIOD_TIME);

        }

        private void stopBannerSlideshow() {
            timer.cancel();
        }

    }

    public class HorizontalProductViewholder extends RecyclerView.ViewHolder {
        private TextView horizontalLayoutTitle;
        private Button horizontalSeeAllBtn;
        private RecyclerView horizontalRecyclerView;
        private ConstraintLayout container;

        public HorizontalProductViewholder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.containerhorizontal);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalSeeAllBtn = itemView.findViewById(R.id.horizontal_scroll_see_all_button);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recyclerview);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);
        }

        private void setHorizontalProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList, String title, String color, List<WishListModel> seeAllProductList) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontalLayoutTitle.setText(title);

            if (horizontalProductScrollModelList.size() > 8) {
                horizontalSeeAllBtn.setVisibility(View.VISIBLE);
                horizontalSeeAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SeeAllActivity.wishListModelList = seeAllProductList;
                        Intent seeallIntent = new Intent(itemView.getContext(), SeeAllActivity.class);
                        seeallIntent.putExtra("layout_code", 0);
                        seeallIntent.putExtra("title", title);
                        itemView.getContext().startActivity(seeallIntent);
                    }
                });
            }
            else {
                horizontalSeeAllBtn.setVisibility(View.INVISIBLE);
    }
            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalRecyclerView.setLayoutManager(linearLayoutManager);
            horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }
    }


    public class GridProductViewholder extends RecyclerView.ViewHolder {
        private TextView gridLayoutTitle;
        private Button gridLayoutSeeAllBtn;
        private GridLayout gridProductLayout;
        private ConstraintLayout container;

        public GridProductViewholder(@NonNull View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.container);
            gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutSeeAllBtn = itemView.findViewById(R.id.grid_product_layout_seeall_btn);
            gridProductLayout = itemView.findViewById(R.id.grid_layout);
        }

        private void setGridProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList, String title,String color) {
            gridLayoutTitle.setText(title);
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));

           for(int x=0; x<4; x++)
           {
               ImageView productImage=gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_image);
               TextView productTitle=gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_title);
               TextView productDescription=gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_description);
               TextView productPrice=gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_price);

               Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.bydefaultimage)).into(productImage);
               productTitle.setText(horizontalProductScrollModelList.get(x).getProductTitle());
               productDescription.setText(horizontalProductScrollModelList.get(x).getProductDescription());
               productPrice.setText("Rs."+horizontalProductScrollModelList.get(x).getProductPrice()+"/-");
                gridProductLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#ffffff"));

                if(!title.equals("")) {
                  final  int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                            productDetailsIntent.putExtra("PRODUCT_ID",horizontalProductScrollModelList.get(finalX).getProductID());
                            itemView.getContext().startActivity(productDetailsIntent);
                        }
                    });
                }
           }

            if(!title.equals("")) {
                gridLayoutSeeAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SeeAllActivity.horizontalProductScrollModelList = horizontalProductScrollModelList;
                        Intent seeallIntent = new Intent(itemView.getContext(), SeeAllActivity.class);
                        seeallIntent.putExtra("layout_code", 1);
                        seeallIntent.putExtra("title", title);
                        itemView.getContext().startActivity(seeallIntent);
                    }
                });
            }
        }
    }
}



