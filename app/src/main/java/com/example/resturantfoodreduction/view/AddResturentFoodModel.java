package com.example.resturantfoodreduction.view;

public class AddResturentFoodModel {
    private String title;
    private String description;
    private Integer prePrice;
    private Integer latestPrice;
    private Integer Discount ;
    private Integer Quentity;

   public AddResturentFoodModel(){

   }

    public Integer getDiscount() {
        return Discount;
    }

    public void setDiscount(Integer discount) {
        Discount = discount;
    }

    public Integer getQuentity() {
        return Quentity;
    }

    public void setQuentity(Integer quentity) {
        Quentity = quentity;
    }

    public AddResturentFoodModel(String title, String description, Integer prePrice, Integer latestPrice, Integer discount, Integer quentity) {
        this.title = title;
        this.description = description;
        this.prePrice = prePrice;
        this.latestPrice = latestPrice;
        Discount = discount;
        Quentity = quentity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrePrice() {
        return prePrice;
    }

    public void setPrePrice(Integer prePrice) {
        this.prePrice = prePrice;
    }

    public Integer getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(Integer latestPrice) {
        this.latestPrice = latestPrice;
    }
}
