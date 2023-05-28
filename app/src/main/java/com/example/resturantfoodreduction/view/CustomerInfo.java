package com.example.resturantfoodreduction.view;

import android.net.Uri;

public class CustomerInfo {
    private String userId;
    private String token;
    private String name;
    private String email;
    private String password;
    private Integer phoneNumber;
    private Double longitude;
    private Double latitude;
    private float distance;
    private String cusImg;
    private String customerRequestState;
    private String customerApproveState;
    public CustomerInfo() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CustomerInfo(String userId, String token, String name, String email, String password, Integer phoneNumber, Double longitude, Double latitude, float distance, String cusImg,String customerRequestState,String customerApproveState) {
        this.userId = userId;
        this.token = token;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
        this.cusImg = cusImg;
        this.customerRequestState=customerRequestState;
        this.customerApproveState=customerApproveState;
    }

    public String getCustomerRequestState() {
        return customerRequestState;
    }

    public void setCustomerRequestState(String customerRequestState) {
        this.customerRequestState = customerRequestState;
    }

    public String getCustomerApproveState() {
        return customerApproveState;
    }

    public void setCustomerApproveState(String customerApproveState) {
        this.customerApproveState = customerApproveState;
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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getCusImg() {
        return cusImg;
    }

    public void setCusImg(String cusImg) {
        this.cusImg = cusImg;
    }
}
