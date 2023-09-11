package com.example.humarahub;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humarahub.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {


    public static FirebaseFirestore firebaseFirestore;
    public static List<CategoryModel> categoryModelList = new ArrayList<>();
    //    public static List<HomePageModel> homePageModelList = new ArrayList<();
    public static List<List<HomePageModel>> lists = new ArrayList<>();  //is list me sari list add hogi hai ya
    public static List<String> loadedCategoriesNames = new ArrayList<>();
    public static List<String> wishList = new ArrayList<>();
    public static List<WishListModel> wishListModelList = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<cartItemModel> cartItemModelList = new ArrayList<>();

    public static int selectedAddress=-1;
    public static List<AddressesModel> addressesModelList=new ArrayList<>();



    public static void loadCategories(RecyclerView categoryRecyclerView, final Context context) {
//        categoryModelList = new ArrayList<CategoryModel>();
        categoryModelList.clear();
        firebaseFirestore.getInstance().collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public static void loadFragmentData(RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName) {
        firebaseFirestore.getInstance().collection("CATEGORIES").document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                if ((long) documentSnapshot.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banner = (long) documentSnapshot.get("no_of_banners");
                                    for (long x = 1; x < no_of_banner + 1; x++) {
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + x).toString(), documentSnapshot.get("banner_" + x + "_background").toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0, sliderModelList));
                                } else if ((long) documentSnapshot.get("view_type") == 2) {

                                    List<WishListModel> SeeAllProductList = new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_" + x).toString(),
                                                documentSnapshot.get("product_image_" + x).toString(),
                                                documentSnapshot.get("product_title_" + x).toString(),
                                                documentSnapshot.get("product_subtitle_" + x).toString(),
                                                documentSnapshot.get("product_price_" + x).toString()));

                                        SeeAllProductList.add(new WishListModel(documentSnapshot.get("product_ID_" + x).toString(), documentSnapshot.get("product_image_" + x).toString(),
                                                documentSnapshot.get("product_full_title_" + x).toString(),
                                                documentSnapshot.get("average_rating_" + x).toString(),
                                                (long) documentSnapshot.get("total_ratings_" + x),
                                                documentSnapshot.get("product_price_" + x).toString(),
                                                documentSnapshot.get("cutted_price_" + x).toString(),
                                                (boolean) documentSnapshot.get("COD_" + x),
                                                (boolean) documentSnapshot.get("in_stock_"+x) ));

                                    }
                                    lists.get(index).add(new HomePageModel(1, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList, SeeAllProductList));
                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    List<HorizontalProductScrollModel> GridLayoutModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        GridLayoutModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_" + x).toString(),
                                                documentSnapshot.get("product_image_" + x).toString(),
                                                documentSnapshot.get("product_title_" + x).toString(),
                                                documentSnapshot.get("product_subtitle_" + x).toString(),
                                                documentSnapshot.get("product_price_" + x).toString()
                                        ));
                                    }
                                    lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), GridLayoutModelList));
                                }
                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public static void loadWishList(final Context context, Dialog dialog, final boolean loadProductData) {
        wishList.clear();
        firebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_WISHLIST").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                                wishList.add(task.getResult().get("product_ID_" + x).toString());
                                if (DBqueries.wishList.contains(ProductDetailsActivity.productID)) {
                                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                                    if (ProductDetailsActivity.addtowishlistbtn != null) {
                                        ProductDetailsActivity.addtowishlistbtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.primaryYellowBtn));
                                    }
                                } else {
                                    if (ProductDetailsActivity.addtowishlistbtn != null) {
                                        ProductDetailsActivity.addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                    }
                                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                                }

                                if (loadProductData) {
                                    wishListModelList.clear();
                                    final String productId = task.getResult().get("product_ID_" + x).toString();
                                    firebaseFirestore.getInstance().collection("PRODUCTS").document(productId)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        wishListModelList.add(new WishListModel(productId, task.getResult().get("product_image_1").toString(),
                                                                task.getResult().get("product_title").toString(),
                                                                task.getResult().get("average_rating").toString(),
                                                                (long) task.getResult().get("total_ratings"),
                                                                task.getResult().get("product_price").toString(),
                                                                task.getResult().get("cutted_price").toString(),
                                                                (boolean) task.getResult().get("COD"),
                                                                (boolean) task.getResult().get("in_stock")));

                                                        MyWishListFragment.wishListAdapter.notifyDataSetChanged();
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    public static void removeFromWishlist(int index, Context context) {
        final String removeProductId = wishList.get(index);
        wishList.remove(index);
        Map<String, Object> updateWishlist = new HashMap<>();

        for (int x = 0; x < wishList.size(); x++) {
            updateWishlist.put("product_ID_" + x, wishList.get(x));
        }
        updateWishlist.put("list_size", (long) wishList.size());
        firebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_WISHLIST").set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (wishListModelList.size() != 0) {
                                wishListModelList.remove(index);
                                MyWishListFragment.wishListAdapter.notifyDataSetChanged();
                            }
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                            Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            if (ProductDetailsActivity.addtowishlistbtn != null) {
                                ProductDetailsActivity.addtowishlistbtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.primaryYellowBtn));
                            }
                            wishList.add(index, removeProductId);
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                        ProductDetailsActivity.running_wishlist_query = false;

                    }
                });

    }


    public static void loadCartList(final Context context, Dialog dialog, final boolean loadProductData, final TextView badgeCount,final TextView cartTotalAmount) {
        cartList.clear();
        firebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_CART").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                                cartList.add(task.getResult().get("product_ID_" + x).toString());
                                if (DBqueries.cartList.contains(ProductDetailsActivity.productID)) {
                                    ProductDetailsActivity.ALREADY_ADDED_TO_CART = true;   //already added productdetail se get kiahai

                                } else {
                                    ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;
                                }

                                if (loadProductData) {
                                    cartItemModelList.clear();
                                    final String productId = task.getResult().get("product_ID_" + x).toString();
                                    firebaseFirestore.getInstance().collection("PRODUCTS").document(productId)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        int index = 0;
                                                        if (cartList.size() >= 2) {
                                                            index = cartList.size() - 2;
                                                        }
                                                        cartItemModelList.add(index, new cartItemModel(cartItemModel.CART_ITEM, productId, task.getResult().get("product_image_1").toString(),
                                                                task.getResult().get("product_title").toString()
                                                                , task.getResult().get("product_price").toString()
                                                                , task.getResult().get("cutted_price").toString()
                                                                , (boolean) task.getResult().get("in_stock")));

                                                        if (cartList.size() == 1) {
                                                            cartItemModelList.add(new cartItemModel(cartItemModel.TOTAL_AMOUNT));
                                                            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                            if (parent != null) {
                                                                TextView textView = parent.findViewById(R.id.total_cart_amount);
                                                                if (textView != null) {
                                                                    parent.setVisibility(View.VISIBLE);
                                                                }
                                                            }

                                                        }
                                                        if (cartList.size() == 0) {
                                                            cartItemModelList.clear();
                                                        }


                                                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }

                                if (cartList.size() != 0) {
                                    badgeCount.setVisibility(View.VISIBLE);
                                } else {
                                    badgeCount.setVisibility(View.INVISIBLE);
                                }
                                if (DBqueries.cartList.size() < 99) {
                                    badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                                } else {
                                    badgeCount.setText("99");
                                }
                            }   //forloop
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    public static void removeFromCart(int index, Context context,TextView cartTotalAmount) {
        final String removeProductId = cartList.get(index);
        cartList.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();

        for (int x = 0; x < cartList.size(); x++) {
            updateCartList.put("product_ID_" + x, cartList.get(x));
        }
        updateCartList.put("list_size", (long) cartList.size());
        firebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_CART").set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (cartItemModelList.size() != 0) {
                                cartItemModelList.remove(index);
                                MyCartFragment.cartAdapter.notifyDataSetChanged();
                            }
                            // theree was an in with linearlayout line
                            if (cartList.size() == 0) {
                                LinearLayout parent=(LinearLayout) cartTotalAmount.getParent().getParent();
                                    parent.setVisibility(View.GONE);
                                cartItemModelList.clear();
                            }
                            Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            cartList.add(index, removeProductId);
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                        ProductDetailsActivity.running_cart_query = false;

                    }
                });

    }

    public static void loadAddresses(final Context context,Dialog loadingDialog)
    {
        addressesModelList.clear();
        firebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_ADDRESSES").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Intent deliveryIntent;
                            if((long)task.getResult().get("list_size")==0)   //list get ki hain
                            {
                                deliveryIntent=new Intent(context,AddAddressActivity.class);
                                deliveryIntent.putExtra("INTENT","deliveryIntent");
                            } else{
                                for(long x=1; x<(long)task.getResult().get("list_size")+1; x++)
                                {
                                    addressesModelList.add(new AddressesModel(task.getResult().get("name_"+x).toString(),task.getResult().get("address_"+x).toString(),
                                            task.getResult().get("pincode_"+x).toString(),(boolean)task.getResult().get("selected_"+x)));
                                    if((boolean)task.getResult().get("selected_"+x))
                                    {
                                        selectedAddress=Integer.parseInt(String.valueOf(x - 1));
                                    }
                                }
                             deliveryIntent=new Intent(context,DeliveryActivity.class);

                            }
                            context.startActivity(deliveryIntent);
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }



    public static void clearData()
    {
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        wishList.clear();
        wishListModelList.clear();
        cartList.clear();
        cartItemModelList.clear();
        addressesModelList.clear();
    }
}
