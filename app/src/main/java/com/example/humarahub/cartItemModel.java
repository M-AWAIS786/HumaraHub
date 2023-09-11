package com.example.humarahub;

public class cartItemModel {
    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    ////cart item
    private String productID;
    private String productImage;
    private String productTitle;
    private String productPrice;
    private String cuttedPrice;
//    private long productQuantity;
    //cart item;

    private boolean instock;
    public cartItemModel(int type, String productID, String productImage, String productTitle, String productPrice, String cuttedPrice,boolean instock) {
        this.type = type;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.instock=instock;

    }

    public boolean isInstock() {
        return instock;
    }

    public void setInstock(boolean instock) {
        this.instock = instock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }


//////////////////cart total
private int totalItemPriceText,totalAmount,totalSaveAmount;
    public cartItemModel(int type) {
        this.type = type;
    }

    public int getTotalItemPriceText() {
        return totalItemPriceText;
    }

    public void setTotalItemPriceText(int totalItemPriceText) {
        this.totalItemPriceText = totalItemPriceText;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalSaveAmount() {
        return totalSaveAmount;
    }

    public void setTotalSaveAmount(int totalSaveAmount) {
        this.totalSaveAmount = totalSaveAmount;
    }
}







