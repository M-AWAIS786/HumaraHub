package com.example.humarahub;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder> {

    private List<MyOrderItemModel> myOrderItemModelList;


    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }


    @NonNull
    @Override
    public MyOrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_item_layout, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.Viewholder holder, int position) {
        int resource = myOrderItemModelList.get(position).getProductImage();
        int rating = myOrderItemModelList.get(position).getRating();
        String title = myOrderItemModelList.get(position).getProductTitle();
        String deliverdate = myOrderItemModelList.get(position).getDeliveryDate();

        holder.setData(resource, title, deliverdate, rating);

    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView productImage, orderDefaultDot;
        private TextView productTitle, orderdeliveryDate;
        private LinearLayout rateNowContainer;
        ImageView starBtn;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.order_productImage);
            productTitle = itemView.findViewById(R.id.order_product_title);
            orderDefaultDot = itemView.findViewById(R.id.order_defaultDot);
            orderdeliveryDate = itemView.findViewById(R.id.order_deliveryDate);
            rateNowContainer = itemView.findViewById(R.id.rate_now_container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent orderDetailsintent=new Intent(itemView.getContext(),OrderDetailsActivity.class);
                    itemView.getContext().startActivity(orderDetailsintent);
                }
            });
        }

        private void setData(int resource, String title, String deliveryDate, int rating) {
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if (deliveryDate.equals("Cancelled")) {
                orderDefaultDot.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.highergray)));
            } else {
                orderDefaultDot.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.primaryYellowBtn)));

            }

            orderdeliveryDate.setText(deliveryDate);
            setRating(rating);

            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                final int starPosition = x;
                rateNowContainer.getChildAt(x).setOnClickListener(view -> {
                    setRating(starPosition);
                });
            }

        }

        private void setRating(int starPosition) {
            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                 starBtn = (ImageView) rateNowContainer.getChildAt(x);
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if (x <= starPosition) {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }
            }
        }

    }
}
