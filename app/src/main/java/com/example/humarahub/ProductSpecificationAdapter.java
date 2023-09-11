package com.example.humarahub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {
    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductSpecificationAdapter(List<ProductSpecificationModel> productSpecificationModelList) {
        this.productSpecificationModelList = productSpecificationModelList;
    }

//    @Override
//    public int getItemViewType(int position) {
//        switch (productSpecificationModelList.get(position).getType())
//        {
//            case 0:
//                return ProductSpecificationModel.SPECIFICATION_TITLE;
//            case 1:
//                return  ProductSpecificationModel.SPECIFICATION_BODY;
//            default:
//                return -1;
//        }
//    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//        switch (viewType) {
//            case ProductSpecificationModel.SPECIFICATION_TITLE:

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_specification_item_layout, viewGroup, false);
            return new ViewHolder(view);
        }
//    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder holder, int position) {
        String featureTitle = productSpecificationModelList.get(position).getFeatureName();
        String featureDetail = productSpecificationModelList.get(position).getFeatureValue();
        holder.setFeature(featureTitle, featureDetail);

    }

    @Override
    public int getItemCount() {
        return productSpecificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView featureName;
        private TextView featureValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featureName = itemView.findViewById(R.id.feature_name);
            featureValue = itemView.findViewById(R.id.feature_value);
        }

        private void setFeature(String featureTitle, String featuredetail) {
            featureName.setText(featureTitle);
            featureValue.setText(featuredetail);
        }

    }
}
