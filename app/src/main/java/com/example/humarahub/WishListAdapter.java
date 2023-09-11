package com.example.humarahub;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
    private static List<WishListModel> wishListModelList;
    private Boolean wishlist;
private int lastPosition=-1;

private boolean fromSearch;

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public WishListAdapter(List<WishListModel> wishListModelList, Boolean wishlist) {
        this.wishListModelList = wishListModelList;
        this.wishlist = wishlist;
    }

    public static List<WishListModel> getWishListModelList() {
        return wishListModelList;
    }

    public static void setWishListModelList(List<WishListModel> wishListModelList) {
        WishListAdapter.wishListModelList = wishListModelList;
    }
    //    public WishListAdapter(boolean b) {
//    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_item_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
        String productId=wishListModelList.get(position).getProductId();
        String resource = wishListModelList.get(position).getProductImage();
        String title = wishListModelList.get(position).getProductTitle();
        String rating = wishListModelList.get(position).getRating();
        long totalRating = wishListModelList.get(position).getTotalRating();
        String productPrice = wishListModelList.get(position).getProductPrice();
        String cuttedPrice = wishListModelList.get(position).getCutterPrice();
        Boolean paymentMethod = wishListModelList.get(position).isCOD();
        boolean inStock=wishListModelList.get(position).isInStock();

        holder.setData(productId,resource, title, rating, totalRating, productPrice, cuttedPrice, paymentMethod,position,inStock);

        if(lastPosition< holder.getAdapterPosition()) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = holder.getAdapterPosition(); //aik bar anumatin show krwane k bad dobara load nhi krna
        }
    }

    @Override
    public int getItemCount() {
      return wishListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle, productPrice, cuttedPrice, paymentMethod, rating, totalRatings;
        private View priceCut_divider;
        private ImageButton deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            paymentMethod = itemView.findViewById(R.id.payment_method);
            rating = itemView.findViewById(R.id.tv_product_rating);
            totalRatings = itemView.findViewById(R.id.tv_Total_ratings);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            priceCut_divider = itemView.findViewById(R.id.price_cut_divider);
            deleteBtn = itemView.findViewById(R.id.delete_btn);

        }

        private void setData(String productId,String resource, String title, String averageRate, long totalRatingsNo, String price, String cuttedPriceValue,  boolean COD,int index,boolean inStock) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.bydefaultimage)).into(productImage);
            productTitle.setText(title);


            LinearLayout linearLayout=(LinearLayout) rating.getParent();
            if(inStock)
            {
                rating.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setVisibility(View.VISIBLE);
                paymentMethod.setVisibility(View.VISIBLE);




                rating.setText(averageRate);
                totalRatings.setText("("+totalRatingsNo + ")ratings");
                productPrice.setText("Rs."+price+"/-");
                cuttedPrice.setText("Rs."+cuttedPriceValue+"/-");
                if(COD)
                {
                    paymentMethod.setVisibility(View.VISIBLE);
                }else{
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            }else{
                linearLayout.setVisibility(View.INVISIBLE);
                rating.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.primaryYellowBtn));
                cuttedPrice.setVisibility(View.INVISIBLE);
                paymentMethod.setVisibility(View.INVISIBLE);

            }




            if (wishlist) {
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
               deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  if(!ProductDetailsActivity.running_wishlist_query) {
                      ProductDetailsActivity.running_wishlist_query = true;
                      DBqueries.removeFromWishlist(index, itemView.getContext());
                  }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fromSearch)
                    {
                        ProductDetailsActivity.fromSearch=true;
                    }
                    Intent productDetailsIntent=new Intent(itemView.getContext(),ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("PRODUCT_ID",productId);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });

        }
    }
}
