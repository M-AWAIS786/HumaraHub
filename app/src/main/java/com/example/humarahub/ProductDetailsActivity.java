package com.example.humarahub;


import static com.example.humarahub.MainActivity.showCart;
import static com.example.humarahub.RegisterActivity.setSignUpFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {
    public static boolean running_cart_query = false;
    public static boolean running_wishlist_query = false;    //same product multiple show orha tha isi lia use kia hain
    public static Activity productDetailsActivity;
    private ViewPager productImagesViewpager;
    private TextView productTitle;
    private TextView averageRatingtextview;
    private TextView totalRatingtextview;
    private TextView productPrice;
    private TextView cuttedPrice;

public static boolean fromSearch=false;
    private TabLayout viewpagerIndicator;
    private String productDescription;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();


    public static FloatingActionButton addtowishlistbtn;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;

    private FirebaseFirestore firebaseFirestore;

    //product description
    private ConstraintLayout productDetailsTabContainer;
    private TabLayout productDetailsTablayout;
    private ViewPager productDetailsViewpager;
    ///////////////////////
    private Dialog signInDialog;
    private Dialog loadingDialog;
    private Button addtoCartBtn;
    public static MenuItem cartitem;
    private TextView badgeCount;

    private LinearLayout couponRedemptionLayout;
    private Button couponRedeemBtn;
    private FirebaseUser currentUser;
    public static String productID;
    private DocumentSnapshot documentSnapshot;
    private Button buyNowBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        productImagesViewpager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addtowishlistbtn = findViewById(R.id.add_to_wishList_btn);
        productDetailsViewpager = findViewById(R.id.product_details_viewpager);
        productDetailsTablayout = findViewById(R.id.product_details_tablayout);
        productTitle = findViewById(R.id.product_title);
        averageRatingtextview = findViewById(R.id.tv_product_rating);
        totalRatingtextview = findViewById(R.id.total_ratings_textview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        productDetailsTabContainer = findViewById(R.id.product_details_tabs_container);
        addtoCartBtn = findViewById(R.id.addCartBtn);
        couponRedemptionLayout = findViewById(R.id.coupon_redemption_layout);
        buyNowBtn = findViewById(R.id.buyNowBtn);

//        CFkzYqTAI8X6vsh8IyNA
//        getIntent().getStringExtra("PRODUCT_ID")

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    DeliveryActivity.fromCart=false;
                    loadingDialog.show();
                    productDetailsActivity=ProductDetailsActivity.this;
                    if (DeliveryActivity.cartItemModelList == null) {
                        DeliveryActivity.cartItemModelList = new ArrayList<>();
                    }
                    DeliveryActivity.cartItemModelList.clear();
//                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    DeliveryActivity.cartItemModelList.add(new cartItemModel(cartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString(),
                            documentSnapshot.get("product_title").toString()
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            , (boolean) documentSnapshot.get("in_stock")
                    ));
                    DeliveryActivity.cartItemModelList.add(new cartItemModel(cartItemModel.TOTAL_AMOUNT));
                    if (DBqueries.addressesModelList.size() == 0) {
                        DBqueries.loadAddresses(ProductDetailsActivity.this, loadingDialog);  //
                    } else {
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });


//        loading dialog

        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();


//        loading dialog

        final List<String> productImage = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            documentSnapshot = task.getResult();
                            for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                                productImage.add(documentSnapshot.get("product_image_" + x).toString());
                            }
                            ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImage);
                            productImagesViewpager.setAdapter(productImagesAdapter);

                            productTitle.setText(documentSnapshot.get("product_title").toString());
                            averageRatingtextview.setText(documentSnapshot.get("average_rating").toString());
                            totalRatingtextview.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                            productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
                            cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");

                            if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                productDetailsTabContainer.setVisibility(View.VISIBLE);
                                productDescription = documentSnapshot.get("product_description").toString();
                            } else {
                                productDetailsTabContainer.setVisibility(View.GONE);
                            }
                            for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                                productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                                for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                    productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));

                                }
                            }
                            productDetailsViewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTablayout.getTabCount(), productDescription, productSpecificationModelList));

                            if (currentUser != null) {
                                if (DBqueries.cartList.size() == 0) {
                                    DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false, badgeCount, new TextView(ProductDetailsActivity.this));
                                }
                                if (DBqueries.wishList.size() == 0) {
                                    DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
                                } else {
                                    loadingDialog.dismiss();
                                }
                            } else {
                                loadingDialog.dismiss();
                            }
                            if (DBqueries.cartList.contains(productID)) {
                                ALREADY_ADDED_TO_CART = true;
                            } else {
                                ALREADY_ADDED_TO_CART = false;
                            }

                            if (DBqueries.wishList.contains(productID)) {
                                ALREADY_ADDED_TO_WISHLIST = true;
                                addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.primaryYellowBtn));
                            } else {
                                addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                ALREADY_ADDED_TO_WISHLIST = false;
                            }

                            if ((boolean) documentSnapshot.get("in_stock")) {
                                addtoCartBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (currentUser == null) {
                                            signInDialog.show();
                                        } else {
                                            /////////////// add to cart functionality
                                            if (!running_cart_query) {
                                                running_cart_query = true;
                                                if (ALREADY_ADDED_TO_CART) {
                                                    running_cart_query = false;
                                                    Toast.makeText(ProductDetailsActivity.this, "Already added to cart", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Map<String, Object> addProduct = new HashMap<>();
                                                    addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                                    addProduct.put("list_size", (long) (DBqueries.cartList.size() + 1));


                                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA")
                                                            .document("MY_CART").update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                        if (DBqueries.cartItemModelList.size() != 0) {
                                                                            DBqueries.cartItemModelList.add(0, new cartItemModel(cartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString(),
                                                                                    documentSnapshot.get("product_title").toString()
                                                                                    , documentSnapshot.get("product_price").toString()
                                                                                    , documentSnapshot.get("cutted_price").toString()
                                                                                    , (boolean) documentSnapshot.get("in_stock")
                                                                            ));

                                                                        }
                                                                        ALREADY_ADDED_TO_CART = true;

                                                                        DBqueries.cartList.add(productID);  //yaha wishlist me product add kr lia hain
                                                                        Toast.makeText(ProductDetailsActivity.this, "Added to Cart successfully", Toast.LENGTH_SHORT).show();
                                                                        invalidateOptionsMenu();
                                                                        running_cart_query = false;
                                                                    } else {
//                                                addtowishlistbtn.setEnabled(true);
                                                                        running_cart_query = false;
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                buyNowBtn.setVisibility(View.GONE);
                                TextView outOfStock=(TextView)addtoCartBtn.findViewById(R.id.addCartBtn);
                                outOfStock.setText("Out of Stock");
//                                addtoCartBtn.setText("out of stock");
                                outOfStock.setTextColor(getResources().getColor(R.color.white));
                                outOfStock.setCompoundDrawables(null, null, null, null); //cart icon remove line
                            }

                        } else {
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        viewpagerIndicator.setupWithViewPager(productImagesViewpager, true);

        addtowishlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
//                    addtowishlistbtn.setEnabled(false);
//#9e9e9e
                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBqueries.wishList.indexOf(productID);
                            DBqueries.removeFromWishlist(index, ProductDetailsActivity.this);
                            addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {

                            addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.primaryYellowBtn));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishList.size()), productID);
                            addProduct.put("list_size", (long) (DBqueries.wishList.size() + 1));

                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA")
                                    .document("MY_WISHLIST").update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if (DBqueries.wishListModelList.size() != 0) {
                                                    DBqueries.wishListModelList.add(new WishListModel(productID, documentSnapshot.get("product_image_1").toString(),
                                                            documentSnapshot.get("product_title").toString(),
                                                            documentSnapshot.get("average_rating").toString(),
                                                            (long) documentSnapshot.get("total_ratings"),
                                                            documentSnapshot.get("product_price").toString(),
                                                            documentSnapshot.get("cutted_price").toString(),
                                                            (boolean) documentSnapshot.get("COD"),
                                                            (boolean) documentSnapshot.get("in_stock")));
                                                }
                                                ALREADY_ADDED_TO_WISHLIST = true;
                                                addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.primaryYellowBtn));
                                                DBqueries.wishList.add(productID);  //yaha wishlist me product add kr lia hain
                                                Toast.makeText(ProductDetailsActivity.this, "Added to wishlist successfully", Toast.LENGTH_SHORT).show();
                                            } else {
//                                                addtowishlistbtn.setEnabled(true);
                                                addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                                String error = task.getException().getMessage();
                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                            running_wishlist_query = false;

                                        }
                                    });

                        }
                    }
                }
            }
        });
        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ///////////////////// sign in dialog
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dialogSingInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSingUpBtn = signInDialog.findViewById(R.id.sign_up_btn);

        final Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);


        dialogSingInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dialogSingUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });


        ////////////////sign in dialog
        if (currentUser == null) {
            couponRedemptionLayout.setVisibility(View.GONE);

        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            couponRedemptionLayout.setVisibility(View.GONE);
        } else {
            couponRedemptionLayout.setVisibility(View.VISIBLE);
        }
        if (currentUser != null) {

            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
            } else {
                loadingDialog.dismiss();
            }
        } else {
            loadingDialog.dismiss();
        }


        if (DBqueries.cartList.contains(productID)) {
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }

        if (DBqueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.primaryYellowBtn));
        } else {
            addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

            ALREADY_ADDED_TO_WISHLIST = false;
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        cartitem = menu.findItem(R.id.main_cart_icon);
        cartitem.setActionView(R.layout.badge_layout);
        ImageView badgeIcon = cartitem.getActionView().findViewById(R.id.badge_icon);
        badgeIcon.setImageResource(R.drawable.cartmainwhite);
        badgeCount = cartitem.getActionView().findViewById(R.id.badge_count);
        if (currentUser != null) {
            if (DBqueries.cartList.size() == 0) {
                DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false, badgeCount, new TextView(ProductDetailsActivity.this));
            } else {
                badgeCount.setVisibility(View.VISIBLE);
                if (DBqueries.cartList.size() < 99) {
                    badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                } else {
                    badgeCount.setText("99");
                }
            }

        }
        cartitem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {

                    Intent cartintent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                    showCart = true;
                    startActivity(cartintent);
                }
            }
        });

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_search_icon) {
            // Do something for menu item one
            if(fromSearch)
            {
                finish();
            }else {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }
            return true;
        } else if (id == android.R.id.home) {
            productDetailsActivity=null;
            finish();
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {

                Intent cartintent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartintent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch=false;
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity=null;
        super.onBackPressed();
    }
}