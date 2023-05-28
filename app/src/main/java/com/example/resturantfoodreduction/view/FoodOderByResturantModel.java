package com.example.resturantfoodreduction.view;
public class FoodOderByResturantModel {
    private String userId;
    private String name;
    private String email;
    private String password;
    private Integer phoneNumber;
    private Double longitude;
    private Double latitude;
    private FoodInfoModel foodInfo;
    public FoodOderByResturantModel() {
    }

    public FoodOderByResturantModel(String userId, String name, String email, String password, Integer phoneNumber, Double longitude, Double latitude, FoodInfoModel foodInfo) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.foodInfo = foodInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public FoodInfoModel getFoodInfo() {
        return foodInfo;
    }

    public void setFoodInfo(FoodInfoModel foodInfo) {
        this.foodInfo = foodInfo;
    }
}
