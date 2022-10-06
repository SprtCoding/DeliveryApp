package com.sprtech.quickbite.Models;

public class OrderListModel {
    String cusID, orderName, orderPic, orderQuantity, orderTotal, orderFoodName, orderDescription, orderID, ID, orderDate, orderTime, OrderStatus;

    public OrderListModel() {
    }

    public OrderListModel(String cusID, String orderName, String orderPic, String orderQuantity, String orderTotal, String orderFoodName, String orderDescription, String orderID, String ID, String orderDate, String orderTime, String orderStatus) {
        this.cusID = cusID;
        this.orderName = orderName;
        this.orderPic = orderPic;
        this.orderQuantity = orderQuantity;
        this.orderTotal = orderTotal;
        this.orderFoodName = orderFoodName;
        this.orderDescription = orderDescription;
        this.orderID = orderID;
        this.ID = ID;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.OrderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getCusID() {
        return cusID;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderPic() {
        return orderPic;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public String getOrderFoodName() {
        return orderFoodName;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getID() {
        return ID;
    }
}
