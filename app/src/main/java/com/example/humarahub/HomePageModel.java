package com.example.humarahub;

import java.util.List;

public class HomePageModel {
/////////////////Banner slider start
    private int type;
    public static final int BANNER_SLIDER=0;
    public static final int HORIZONTAL_PRODUCT_VIEW=1;
    public static final int GRID_PRODUCT_VIEW=2;
    private String backgroundColor;


    ///////// slider Model
    private List<SliderModel> sliderModelList;
    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    ////banner slider end







    private String title;


    private List<HorizontalProductScrollModel>horizontalProductScrollModelList;
    private List<WishListModel>SeeAllProductList;

    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }
    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    public List<WishListModel> getSeeAllProductList() {
        return SeeAllProductList;
    }

    public void setSeeAllProductList(List<WishListModel> seeAllProductList) {
        SeeAllProductList = seeAllProductList;
    }

    public HomePageModel(int type, String title, String backgroundColor , List<HorizontalProductScrollModel> horizontalProductScrollModelList, List<WishListModel>SeeAllProductList) {
        this.SeeAllProductList=SeeAllProductList;
        this.type = type;
        this.title = title;
        this.backgroundColor=backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    public HomePageModel(List<WishListModel> seeAllProductList) {
        SeeAllProductList = seeAllProductList;
    }


/////////////////Horizontal product layout
    public HomePageModel(int type, String title, String backgroundColor , List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.type = type;
        this.title = title;
        this.backgroundColor=backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }


    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
/////////////////////////////// Horiontal Product layout & Gird Product Layout


}
