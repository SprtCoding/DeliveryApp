package com.sprtech.quickbite.Models;

public class CustomerNotificationModel {
    String SenderName, SenderMessage, Status, Driver, estimatedTime, OrderID, CartOrderID, DriverPhoneNumber;

    public CustomerNotificationModel() {
    }

    public CustomerNotificationModel(String senderName, String senderMessage, String status, String driver, String estimatedTime, String orderID, String cartOrderID, String driverPhoneNumber) {
        SenderName = senderName;
        SenderMessage = senderMessage;
        Status = status;
        Driver = driver;
        this.estimatedTime = estimatedTime;
        OrderID = orderID;
        CartOrderID = cartOrderID;
        DriverPhoneNumber = driverPhoneNumber;
    }

    public String getDriverPhoneNumber() {
        return DriverPhoneNumber;
    }

    public String getCartOrderID() {
        return CartOrderID;
    }

    public String getStatus() {
        return Status;
    }

    public String getOrderID() {
        return OrderID;
    }

    public String getDriver() {
        return Driver;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public String getSenderName() {
        return SenderName;
    }

    public String getSenderMessage() {
        return SenderMessage;
    }
}
