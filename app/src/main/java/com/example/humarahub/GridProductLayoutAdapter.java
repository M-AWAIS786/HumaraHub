package com.example.humarahub;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {
List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertview, final ViewGroup parent) {
       View view;
       if(convertview==null)
       {
           view= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,null);
           view.setElevation(0);
           view.setBackgroundColor(Color.parseColor("#ffffff"));

           view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent productDetailsIntent=new Intent(parent.getContext(),ProductDetailsActivity.class);
                   productDetailsIntent.putExtra("PRODUCT_ID",horizontalProductScrollModelList.get(i).getProductID());
                   parent.getContext().startActivity(productDetailsIntent);
               }
           });
//           view.setOnClickListener(v -> {
//
//           });





           ImageView productImage=view.findViewById(R.id.h_s_product_image);
           TextView productTitle=view.findViewById(R.id.h_s_product_title);
           TextView productDescription=view.findViewById(R.id.h_s_product_description);
           TextView productPrice=view.findViewById(R.id.h_s_product_price);


           Glide.with(view.getContext()).load(horizontalProductScrollModelList.get(i).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.bydefaultimage)).into(productImage);
           productTitle.setText(horizontalProductScrollModelList.get(i).getProductTitle());
           productDescription.setText(horizontalProductScrollModelList.get(i).getProductDescription());
           productPrice.setText("Rs."+horizontalProductScrollModelList.get(i).getProductPrice()+"/-");
       }
       else {
           view=convertview;
       }

        return view;
    }
}
