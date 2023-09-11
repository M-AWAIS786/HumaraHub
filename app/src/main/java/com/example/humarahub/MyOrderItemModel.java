package com.example.humarahub;

public class MyOrderItemModel {
    private int productImage;
    private int rating;
    private String productTitle;
    private String deliveryDate;

    public MyOrderItemModel(int productImage, int rating, String productTitle, String deliveryDate) {
        this.productImage = productImage;
        this.rating = rating;
        this.productTitle = productTitle;
        this.deliveryDate = deliveryDate;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}