package com.example.humarahub;

import java.util.ArrayList;

public class WishListModel {
    private String productId;
    private String productImage;
    private String productTitle;
    private String rating;
    private long totalRating;
    private String productPrice;
    private String cutterPrice;
    private boolean COD;
    private  boolean inStock;


    private ArrayList<String> tags;
    public WishListModel(String productId,String productImage, String productTitle, String rating, long totalRating, String productPrice, String cutterPrice, boolean COD,boolean inStock) {
        this.productId=productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.rating = rating;
        this.totalRating = totalRating;
        this.productPrice = productPrice;
        this.cutterPrice = cutterPrice;
        this.COD = COD;
        this.inStock=inStock;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(long totalRating) {
        this.totalRating = totalRating;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCutterPrice() {
        return cutterPrice;
    }

    public void setCutterPrice(String cutterPrice) {
        this.cutterPrice = cutterPrice;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }
}

