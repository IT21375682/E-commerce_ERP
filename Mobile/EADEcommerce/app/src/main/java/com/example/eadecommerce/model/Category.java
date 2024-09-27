package com.example.eadecommerce.model;

public class Category {
    private String name;
    private int productCount;
    private String imageRes;

    public Category(String name, int productCount, String imageRes) {
        this.name = name;
        this.productCount = productCount;
        this.imageRes = imageRes;
    }

    public String getName() {
        return name;
    }

    public int getProductCount() {
        return productCount;
    }

    public String getImageResId() {
        return imageRes;
    }
}
