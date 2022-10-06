package com.sprtech.quickbite.Models;

public class CartModel {
    String fAName, quantity, fPic, fPrice, customerID, fBrach, fName, description, cartOrderID;

    public CartModel() {
    }

    public CartModel(String fAName, String quantity, String fPic, String fPrice, String customerID, String fBrach, String fName, String description, String cartOrderID) {
        this.fAName = fAName;
        this.quantity = quantity;
        this.fPic = fPic;
        this.fPrice = fPrice;
        this.customerID = customerID;
        this.fBrach = fBrach;
        this.fName = fName;
        this.description = description;
        this.cartOrderID = cartOrderID;
    }

    public String getfAName() {
        return fAName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getfPic() {
        return fPic;
    }

    public String getfPrice() {
        return fPrice;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getfBrach() {
        return fBrach;
    }

    public String getfName() {
        return fName;
    }

    public String getDescription() {
        return description;
    }

    public String getCartOrderID() {
        return cartOrderID;
    }
}
