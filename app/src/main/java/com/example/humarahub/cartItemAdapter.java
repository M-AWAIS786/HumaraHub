package com.example.humarahub;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class cartItemAdapter extends RecyclerView.Adapter {

    private List<cartItemModel> cartItemModelList;
    private int lastPosition=-1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public cartItemAdapter(List<cartItemModel> cartItemModelList,TextView cartTotalAmount,boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount=cartTotalAmount;
        this.showDeleteBtn=showDeleteBtn;
    }

    //custom made getItemviewtype
    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return cartItemModel.CART_ITEM;
            case 1:
                return cartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case cartItemModel.CART_ITEM:
                View cartItemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new CartItemViewholder(cartItemview);

            case cartItemModel.TOTAL_AMOUNT:
                View cartTotalview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new CartTotalAmountViewholder(cartTotalview);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType())
        {
            case cartItemModel.CART_ITEM:
                String productID=cartItemModelList.get(position).getProductID();
                String resource=cartItemModelList.get(position).getProductImage();
                String title=cartItemModelList.get(position).getProductTitle();
                String productPriceText=cartItemModelList.get(position).getProductPrice();
                String cuttedPriceText=cartItemModelList.get(position).getCuttedPrice();
                boolean instock=cartItemModelList.get(position).isInstock();

                ((CartItemViewholder)viewHolder).setItemDetails(productID,resource,title,productPriceText,cuttedPriceText,position,instock);
                break;
            case cartItemModel.TOTAL_AMOUNT:

                int totalItemPriceText=0;
                int totalAmount=0;
                int totalSaveAmount=0;
                for(int x=0; x<cartItemModelList.size(); x++)
                {
                    if(cartItemModelList.get(x).getType()==cartItemModel.CART_ITEM && cartItemModelList.get(x).isInstock())
                    {
                        totalItemPriceText=totalItemPriceText+Integer.parseInt(cartItemModelList.get(x).getProductPrice());


                    }
                }

                if(totalItemPriceText>500)
                {
                    totalAmount=totalItemPriceText;
                }

//                totalAmount=totalAmount+Integer.parseInt(cartItemModelList.get(x).getProductPrice());
//                totalSaveAmount=totalSaveAmount+Integer.parseInt(cartItemModelList.get(x).getProductPrice());
                cartItemModelList.get(position).setTotalItemPriceText(totalItemPriceText);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setTotalSaveAmount(totalSaveAmount);
                ((CartTotalAmountViewholder) viewHolder).setTotalAmount(totalItemPriceText,totalAmount,totalSaveAmount);
                break;
            default:
                return ;
        }
        if(lastPosition< viewHolder.getAdapterPosition()) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = viewHolder.getAdapterPosition(); //aik bar anumatin show krwane k bad dobara load nhi krna
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    //custom made class
    class CartItemViewholder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle, productPrice, cuttedPrice, productQuantity;
        private LinearLayout deleteBtn;
        private LinearLayout couponRedemptionLayout;

        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productPrice = itemView.findViewById(R.id.productPrice);
            cuttedPrice=itemView.findViewById(R.id.cutted_price);
            productTitle = itemView.findViewById(R.id.product_titles);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            deleteBtn=itemView.findViewById(R.id.remove_item_btn);
            couponRedemptionLayout=itemView.findViewById(R.id.coupon_redemption_layout);

        }

        private void setItemDetails(String productID,String resource, String title,String productPriceText,String cuttedPriceText,int position,boolean instock) {

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.bydefaultimage)).into(productImage);
            productTitle.setText(title);

            if(instock) {

                productPrice.setText("Rs."+productPriceText+"/-");
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setText("Rs."+cuttedPriceText+"/-");
                couponRedemptionLayout.setVisibility(View.VISIBLE);


                //  ***************************
                //  ***********************************************
                //  product quantity btn click listener user quenatity dialoger
                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialogue);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText quantityno = quantityDialog.findViewById(R.id.quantity_no);
                        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                quantityDialog.dismiss();
                            }
                        });

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



//                                productQuantity.setText("Qty: " + quantityno.getText());
//                                quantityDialog.dismiss();
                            }
                        });
                        quantityDialog.show();
                    }
                });
            }else {
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.primaryYellowBtn));
                cuttedPrice.setText("");
                couponRedemptionLayout.setVisibility(View.GONE);

                productQuantity.setVisibility(View.INVISIBLE);
//                productQuantity.setText("Qty: " + 0);
//                TextViewCompat.setCompoundDrawableTintList(productQuantity,ColorStateList.valueOf(Color.parseColor("#70000000")));
//                productQuantity.setTextColor(Color.parseColor("#70000000"));
//                productQuantity.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#70000000")));
            }



                if(showDeleteBtn)
                {
                    deleteBtn.setVisibility(View.VISIBLE);
                }else {
                    deleteBtn.setVisibility(View.GONE);
                }
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!ProductDetailsActivity.running_cart_query)
                        {
                            ProductDetailsActivity.running_cart_query=true;
                            DBqueries.removeFromCart(position,itemView.getContext(),cartTotalAmount);
                        }
                    }
                });

            }

    }

    class CartTotalAmountViewholder extends RecyclerView.ViewHolder {
        private TextView totalAmountPrice,totalPrice,saveAmount;


        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalAmountPrice=itemView.findViewById(R.id.total_amount_price);
            totalPrice=itemView.findViewById(R.id.total_price);
            saveAmount=itemView.findViewById(R.id.save_amount);
        }
        private void setTotalAmount(int totalItemPriceText,int totalPriceText,int totalSaveAmount)
        {
            totalAmountPrice.setText("Rs."+totalItemPriceText+"/-");
            totalPrice.setText("Rs."+totalPriceText+"/-");
            cartTotalAmount.setText("Rs."+totalPriceText+"/-");
            saveAmount.setText("Rs."+totalSaveAmount+"/-");

            LinearLayout parent=(LinearLayout) cartTotalAmount.getParent().getParent();
            if(totalItemPriceText ==0)
            {
                DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);
                parent.setVisibility(View.GONE);
            }else {
                parent.setVisibility(View.VISIBLE);
            }
        }

    }


}
