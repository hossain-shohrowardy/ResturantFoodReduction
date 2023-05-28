package com.example.resturantfoodreduction.view;

public class FoodInfoModel {
    String imguri;
    String foodId;
    String title;
    String description;
    Integer prePrice;
   Integer latestPice;
   Integer discount;
   Integer quentity;
    public FoodInfoModel() {
    }

    public FoodInfoModel(String imguri, String foodId, String title, String description, Integer prePrice, Integer latestPice, Integer discount, Integer quentity) {
        this.imguri = imguri;
        this.foodId = foodId;
        this.title = title;
        this.description = description;
        this.prePrice = prePrice;
        this.latestPice = latestPice;
        this.discount = discount;
        this.quentity = quentity;
    }

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
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

    public Integer getLatestPice() {
        return latestPice;
    }

    public void setLatestPice(Integer latestPice) {
        this.latestPice = latestPice;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getQuentity() {
        return quentity;
    }

    public void setQuentity(Integer quentity) {
        this.quentity = quentity;
    }
}
