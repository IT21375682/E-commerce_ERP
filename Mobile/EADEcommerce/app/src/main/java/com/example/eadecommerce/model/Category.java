package com.example.eadecommerce.model;

public class Category {
    private String id;
    private String categoryName;

    public Category(String id, String name) {
        this.id = id;
        this.categoryName = name;
    }

    public String getName() {
        return categoryName;
    }

    public String getId() {
        return id;
    }

}
