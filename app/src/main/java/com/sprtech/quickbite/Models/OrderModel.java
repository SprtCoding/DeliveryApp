package com.sprtech.quickbite.Models;

public class OrderModel {
    String cusID, cusName, cusPic, totalOrderFee , cusAddress, cusPhone, CusOrderID, deliveryFee, orderID, cartOrderID, orderDate, orderTime, Driver, orderStatus, order, Status;

    public OrderModel() {
    }

    public OrderModel(String cusID, String cusName, String cusPic, String totalOrderFee, String cusAddress, String cusPhone, String cusOrderID, String deliveryFee, String orderID, String cartOrderID, String orderDate, String orderTime, String driver, String orderStatus, String order, String status) {
        this.cusID = cusID;
        this.cusName = cusName;
        this.cusPic = cusPic;
        this.totalOrderFee = totalOrderFee;
        this.cusAddress = cusAddress;
        this.cusPhone = cusPhone;
        CusOrderID = cusOrderID;
        this.deliveryFee = deliveryFee;
        this.orderID = orderID;
        this.cartOrderID = cartOrderID;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        Driver = driver;
        this.orderStatus = orderStatus;
        this.order = order;
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public String getOrder() {
        return order;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getDriver() {
        return Driver;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getCartOrderID() {
        return cartOrderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getCusID() {
        return cusID;
    }

    public String getCusName() {
        return cusName;
    }

    public String getCusPic() {
        return cusPic;
    }

    public String getTotalOrderFee() {
        return totalOrderFee;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public String getCusOrderID() {
        return CusOrderID;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }
}
