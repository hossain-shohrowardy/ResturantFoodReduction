package com.example.resturantfoodreduction.view;

public class ResturantOwnerModel {
    private String userId;
    private String name;
    private String email;
    private String imguri;
    private String password;
    private Integer phoneNumber;
    private Double longitude;
    private Double latitude;
    private String status;
    private String token;
    private String foodToken;
    private String validity;
    private String approveState;
    private String seenState;
    private String extendValidityState;
    private String restaurantFoodAddState;
    private String resAmount;


    public ResturantOwnerModel() {
    }

    public ResturantOwnerModel(String userId, String name, String email, String imguri, String password, Integer phoneNumber, Double longitude, Double latitude, String status, String token, String foodToken, String validity, String approveState, String seenState,String extendValidityState,String restaurantFoodAddState,String resAmount) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.imguri = imguri;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.token = token;
        this.foodToken = foodToken;
        this.validity = validity;
        this.approveState = approveState;
        this.seenState = seenState;
        this.extendValidityState=extendValidityState;
        this.restaurantFoodAddState=restaurantFoodAddState;
        this.resAmount=resAmount;
    }

    public String getResAmount() {
        return resAmount;
    }

    public void setResAmount(String resAmount) {
        this.resAmount = resAmount;
    }

    public String getExtendValidityState() {
        return extendValidityState;
    }

    public void setExtendValidityState(String extendValidityState) {
        this.extendValidityState = extendValidityState;
    }

    public String getRestaurantFoodAddState() {
        return restaurantFoodAddState;
    }

    public void setRestaurantFoodAddState(String restaurantFoodAddState) {
        this.restaurantFoodAddState = restaurantFoodAddState;
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

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFoodToken() {
        return foodToken;
    }

    public void setFoodToken(String foodToken) {
        this.foodToken = foodToken;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getApproveState() {
        return approveState;
    }

    public void setApproveState(String approveState) {
        this.approveState = approveState;
    }

    public String getSeenState() {
        return seenState;
    }

    public void setSeenState(String seenState) {
        this.seenState = seenState;
    }
}