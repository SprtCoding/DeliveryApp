package com.sprtech.quickbite.Models;

public class DeliveryFeeModel {
    String customerName, orderDate, orderTime, deliveryFee, deliveryFeeID, Driver;

    public DeliveryFeeModel() {
    }

    public DeliveryFeeModel(String customerName, String orderDate, String orderTime, String deliveryFee, String deliveryFeeID, String driver) {
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.deliveryFee = deliveryFee;
        this.deliveryFeeID = deliveryFeeID;
        this.Driver = driver;
    }

    public String getDriver() {
        return Driver;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public String getDeliveryFeeID() {
        return deliveryFeeID;
    }
}
