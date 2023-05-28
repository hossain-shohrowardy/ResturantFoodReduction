package com.example.resturantfoodreduction.view;

public class FoodsInfoModel {
    String description;
    int discount;
    String foodId;
    String imguri;
    int latestPice;
    int prePrice;
    int quentity;
    String title;
    public FoodsInfoModel() {
    }

    public FoodsInfoModel(String description, int discount, String foodId, String imguri, int latestPice, int prePrice, int quentity, String title) {
        this.description = description;
        this.discount = discount;
        this.foodId = foodId;
        this.imguri = imguri;
        this.latestPice = latestPice;
        this.prePrice = prePrice;
        this.quentity = quentity;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }

    public int getLatestPice() {
        return latestPice;
    }

    public void setLatestPice(int latestPice) {
        this.latestPice = latestPice;
    }

    public int getPrePrice() {
        return prePrice;
    }

    public void setPrePrice(int prePrice) {
        this.prePrice = prePrice;
    }

    public int getQuentity() {
        return quentity;
    }

    public void setQuentity(int quentity) {
        this.quentity = quentity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
