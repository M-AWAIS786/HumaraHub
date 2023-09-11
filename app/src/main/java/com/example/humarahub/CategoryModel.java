package com.example.humarahub;

public class CategoryModel {
    private String categoryName;
    private String categoyrIconLink;

    public CategoryModel(String categoyrIconLink, String categoryName) {
        this.categoyrIconLink = categoyrIconLink;
        this.categoryName = categoryName;
    }

    public String getCategoyrIconLink() {
        return categoyrIconLink;
    }

    public void setCategoyrIconLink(String categoyrIconLink) {
        this.categoyrIconLink = categoyrIconLink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
