package com.example.humarahub;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


public class DeliveryActivity extends AppCompatActivity {
    public static List<cartItemModel> cartItemModelList;
    private RecyclerView deliveryRecyclerView;
    Button changeOraddNewAddress;
    private TextView totalAmount;
    public static final int SELECT_ADDRESS = 0;  // is se pta chla ga user delivery activity se aya hain
    private TextView fullname;
    private TextView fulladdress;
    private TextView pincode;
    private Button continueBtn;
//    private Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageView jazzcashbtn;
    private ImageView cod;
    private ConstraintLayout orderConfirmationLayout;
    private TextView orderIdTv;
    private ImageButton continueShoppingBtn;
    private  boolean successResponse =false;
    public static boolean fromCart;
    private String paymentMethod="JAZZCASH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");


        deliveryRecyclerView = findViewById(R.id.delivery_recyclerView);
        changeOraddNewAddress = findViewById(R.id.change_or_addAddressBtn);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullname = findViewById(R.id.fullname);  // ya 3 shipping details layout ki id assign hain
        fulladdress = findViewById(R.id.address_item);
        pincode = findViewById(R.id.pincode_item);
        continueBtn=findViewById(R.id.delivery_continue_btn);
        orderConfirmationLayout=findViewById(R.id.order_confirmation_layout);
        continueShoppingBtn=findViewById(R.id.continue_shoppingBtn);

        orderIdTv=findViewById(R.id.order_id);




        //        loading dialog

//        loadingDialog = new Dialog(DeliveryActivity.this);
//        loadingDialog.setContentView(R.layout.loading_progress_dialog);
//        loadingDialog.setCancelable(false);
//        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
//        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

//        loading dialog




//        loading dialog

        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        jazzcashbtn=paymentMethodDialog.findViewById(R.id.jazzcash_btn);
        cod=paymentMethodDialog.findViewById(R.id.cod_btn);

//        loading dialog







        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);


        cartItemAdapter cartAdapter = new cartItemAdapter(cartItemModelList, totalAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        changeOraddNewAddress.setVisibility(View.VISIBLE);
        changeOraddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myAddresses = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddresses.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddresses);
            }
        });


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethodDialog.show();
            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod="COD";
                placeOrderDetails();
            }
        });

        jazzcashbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod="JAZZCASH";
                placeOrderDetails();
            }
        });

    }



    @Override
    protected void onStart() {
        fullname.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName());
        fulladdress.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//        loadingDialog.show();
//    }

    @Override
    public void onBackPressed() {
        if(successResponse)
        {
            finish();
            return;
        }
        super.onBackPressed();
    }

    public static String generateOrderId() {
        UUID uuid = UUID.randomUUID();
        String orderId = uuid.toString().toUpperCase().substring(0, 8);
        return orderId;
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        String orderId = generateOrderId();

        if(requestCode==0 && resultCode==RESULT_OK)
        {
            String ResponseCode=  data.getStringExtra("pp_ResponseCode");
//            String arrayAsString = Arrays.toString(ResponseCode);
            System.out.println("DataFn: ResponseCode"+ ResponseCode);
            if(ResponseCode.equals("199"))
            {
                successResponse = true;
//                Toast.makeText(getApplicationContext(), "Payment failed", Toast.LENGTH_SHORT).show();
                if(MainActivity.mainActivity!=null)
                {
                    MainActivity.mainActivity.finish();
                    MainActivity.mainActivity=null;
                    MainActivity.showCart=false;
                }
                if(ProductDetailsActivity.productDetailsActivity!=null)
                {
                    ProductDetailsActivity.productDetailsActivity.finish();
                    ProductDetailsActivity.productDetailsActivity=null;
                }

                if(fromCart)
                {
//                    loadingDialog.show();
                    Map<String, Object> updateCartList = new HashMap<>();

                    long cartListsize=0;
                    List<Integer> indexList=new ArrayList<>(); //jo item remove krna hai wo yaha store krye gye list me
                    for (int x = 0; x < DBqueries.cartList.size(); x++) {
                        if(!cartItemModelList.get(x).isInstock())
                        {
                            updateCartList.put("product_ID_"+cartListsize,cartItemModelList.get(x).getProductID()); //sequence maintein k lia carlistsize use kia hia
                            cartListsize++;
                        }else{
                            indexList.add(x);
                        }
                    }

                    updateCartList.put("list_size",  cartListsize);
                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                            .document("MY_CART").set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               // yaha jo store kia indexlist me wo no nikale gye or remove kera ha
                                    if(task.isSuccessful())
                                    {
                                        for(int x=0; x<indexList.size();x++)
                                        {
                                            DBqueries.cartList.remove(indexList.get(x).intValue()); //offline wali list me se itm nikale gye
                                            DBqueries.cartItemModelList.remove(indexList.get(x).intValue()); //intvalue array list se value nikale ga
                                            DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);

                                        }
                                    }else{
                                        String error = task.getException().getMessage();
                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

//                    loadingDialog.dismiss();

                }

                   orderIdTv.setText("Order ID "+orderId);
                   orderConfirmationLayout.setVisibility(View.VISIBLE);
                   continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           finish();
                       }
                   });
// second heheheeheh
            }else{
//                Toast.makeText(getApplicationContext(), "payment Success", Toast.LENGTH_SHORT).show();
                if(MainActivity.mainActivity!=null)
                {
                    MainActivity.mainActivity.finish();
                    MainActivity.mainActivity=null;
                    MainActivity.showCart=false;
                }
                if(ProductDetailsActivity.productDetailsActivity!=null)
                {
                    ProductDetailsActivity.productDetailsActivity.finish();
                    ProductDetailsActivity.productDetailsActivity=null;
                }

                orderIdTv.setText("Order ID "+orderId);
                orderConfirmationLayout.setVisibility(View.VISIBLE);
                continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        }


    }

    private void placeOrderDetails()
    {
        String userID=FirebaseAuth.getInstance().getUid();
//        loadingDialog.show();

        for(cartItemModel cartItemModel : cartItemModelList)
        {
            if(cartItemModel.getType() == cartItemModel.CART_ITEM){

                Map<String,Object> orderDetails=new HashMap<>();
                orderDetails.put("ORDER ID",orderIdTv.getText());

                orderDetails.put("Product Id",cartItemModel.getProductID());
                orderDetails.put("User Id",userID);
                if(!cartItemModel.getCuttedPrice().equals(null)) {
                    orderDetails.put("Cutted Price", cartItemModel.getCuttedPrice());
                }
                orderDetails.put("Ordered date", FieldValue.serverTimestamp());
                orderDetails.put("Packed date", FieldValue.serverTimestamp());
                orderDetails.put("Shipped date", FieldValue.serverTimestamp());
                orderDetails.put("Delivery date", FieldValue.serverTimestamp());
                orderDetails.put("Cancelled date", FieldValue.serverTimestamp());

                orderDetails.put("Product Price",cartItemModel.getProductPrice());
                orderDetails.put("Payment Method",paymentMethod);
                orderDetails.put("Address",fulladdress.getText());
                orderDetails.put("FullName",fullname.getText());
                orderDetails.put("Pincode",pincode.getText());
                orderDetails.put("Order Status","Ordered");


//                orderDetails.put("Payment Status","not Paid");
//                orderDetails.put("Order Status","Cancelled");  //

                FirebaseFirestore.getInstance().collection("ORDERS").document(generateOrderId()).collection("OrderItems")
                        .document(cartItemModel.getProductID()).set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful())
                                {


                                    String error=task.getException().getMessage();
                                    Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }else{
                Map<String,Object> orderDetails = new HashMap<>();
                orderDetails.put("Total Items Price",cartItemModel.getTotalItemPriceText());
                orderDetails.put("Total Amount",cartItemModel.getTotalAmount());
                orderDetails.put("Saved Amount",cartItemModel.getTotalSaveAmount());
                orderDetails.put("Payment Status","not Paid");
                orderDetails.put("Order Status","Cancelled");
                FirebaseFirestore.getInstance().collection("ORDERS").document("orderId")
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful())
                           {
                               if(paymentMethod.equals("JAZZCASH"))
                               {
                                   jazzcash();
                               }else{
                                   cod();
                               }
                           }else{
                               String error=task.getException().getMessage();
                               Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                           }
                            }
                        });
            }
        }
    }

    private void jazzcash()
    {
        paymentMethodDialog.dismiss();   //.substring(2,totalAmount.getText().length()-2)
        //  loadingDialog.show();
        Intent i=new Intent(DeliveryActivity.this,PaymentActivity.class);
        i.putExtra("price",totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2));
        startActivityForResult(i,0);
    }

    private void cod()
    {
//        paymentMethodDialog.dismiss();
        finish();
    }
}