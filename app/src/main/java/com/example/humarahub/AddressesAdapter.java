package com.example.humarahub;

import static com.example.humarahub.DeliveryActivity.SELECT_ADDRESS;
import static com.example.humarahub.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.humarahub.MyAddressesActivity.refreshItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int preSelectedPosition;

    public AddressesAdapter(List<AddressesModel> addressesModelList,int MODE) {
        this.addressesModelList = addressesModelList;
        this.MODE=MODE;
        preSelectedPosition=DBqueries.selectedAddress;
    }

    @NonNull
    @Override
    public AddressesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesAdapter.Viewholder holder, int position) {

        String name=addressesModelList.get(position).getName();
        String address=addressesModelList.get(position). getAddress();
        String pincode=addressesModelList.get(position).getPincode();
        Boolean selected=addressesModelList.get(position).isSelected();
        holder.setData(name,address,pincode,selected,position);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView address;
        private TextView pincode;
        private ImageView icon;
        private LinearLayout optionContainer;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            address=itemView.findViewById(R.id.address_item);
            pincode=itemView.findViewById(R.id.pincode_item);
            icon=itemView.findViewById(R.id.icon_view);
            optionContainer=itemView.findViewById(R.id.option_container);
        }

        private void setData(String username,String useraddress,String userpincode,Boolean selected,int position)
        {
            name.setText(username);
            address.setText(useraddress);
            pincode.setText(userpincode);

            if(MODE==SELECT_ADDRESS)
            {

                icon.setImageResource(R.drawable.baseline_check_24);
                if(selected)
                {
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition=position;
                }else {
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(preSelectedPosition!=position) {
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(preSelectedPosition).setSelected(false);
                            refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                            DBqueries.selectedAddress=position;
                        }
                    }
                });
            } else if (MODE==MANAGE_ADDRESS) {
                    optionContainer.setVisibility(View.GONE);
                    icon.setImageResource(R.drawable.vertical_dot);
                    icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            optionContainer.setVisibility(View.VISIBLE);
                            refreshItem(preSelectedPosition,preSelectedPosition);   // 1 he layout ko refresh krwa rha hi
                            // edit or remove option ko hide krna k lia
                            preSelectedPosition=position;
                        }
                    });
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            refreshItem(preSelectedPosition,preSelectedPosition);
                            preSelectedPosition=-1;
                        }
                    });
            }
        }
    }
}
