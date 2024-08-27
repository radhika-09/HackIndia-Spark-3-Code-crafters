package com.exchange.model;

public class Product {
    private int id;
    private String name;
    private String price;
    private String model;
    private String brand;
    private String category;
    private String image;  // Base64 encoded image string
    private String description;

    // Constructor
    public Product(int id, String name, String price, String model, String brand, String category, String image, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.model = model;
        this.brand = brand;
        this.category = category;
        this.image = image;
        this.description = description;
    }

    // Default constructor
    public Product() {
        // Default values can be set here if necessary
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
