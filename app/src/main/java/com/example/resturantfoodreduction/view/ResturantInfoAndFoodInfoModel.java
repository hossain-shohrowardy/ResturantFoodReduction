package com.example.resturantfoodreduction.view;

public class ResturantInfoAndFoodInfoModel {
    String imguri;
    String foodId;
    String title;
    String description;
    Integer prePrice;
    Integer latestPice;
    Integer discount;
    Integer quentity;
    private String userId;
    private String name;
    private String email;
    private String password;
    private Integer phoneNumber;
    private Double longitude;
    private Double latitude;
    private String resImg;
    String seenState;
    String resToken;



    public ResturantInfoAndFoodInfoModel() {
    }

    public ResturantInfoAndFoodInfoModel(String imguri, String foodId, String title, String description, Integer prePrice, Integer latestPice, Integer discount, Integer quentity, String userId, String name, String email, String password, Integer phoneNumber, Double longitude, Double latitude, String resImg, String seenState,String resToken) {
        this.imguri = imguri;
        this.foodId = foodId;
        this.title = title;
        this.description = description;
        this.prePrice = prePrice;
        this.latestPice = latestPice;
        this.discount = discount;
        this.quentity = quentity;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.resImg = resImg;
        this.seenState = seenState;
        this.resToken=resToken;
    }

    public String getResToken() {
        return resToken;
    }

    public void setResToken(String resToken) {
        this.resToken = resToken;
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

    public String getResImg() {
        return resImg;
    }

    public void setResImg(String resImg) {
        this.resImg = resImg;
    }
    public String getSeenState() {
        return seenState;
    }

    public void setSeenState(String seenState) {
        this.seenState = seenState;
    }
}
