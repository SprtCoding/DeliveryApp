package com.sprtech.quickbite.Models;

public class FoodModel {
    String branch, fName, fdescription, fcategory, facroname, fprice, fpic, total, foodUID;

    public FoodModel() {
    }

    public FoodModel(String branch, String fName, String fdescription, String fcategory, String facroname, String fprice, String fpic, String total, String foodUID) {
        this.branch = branch;
        this.fName = fName;
        this.fdescription = fdescription;
        this.fcategory = fcategory;
        this.facroname = facroname;
        this.fprice = fprice;
        this.fpic = fpic;
        this.total = total;
        this.foodUID = foodUID;
    }

    public String getBranch() {
        return branch;
    }

    public String getfName() {
        return fName;
    }

    public String getFdescription() {
        return fdescription;
    }

    public String getFcategory() {
        return fcategory;
    }

    public String getFacroname() {
        return facroname;
    }

    public String getFprice() {
        return fprice;
    }

    public String getFpic() {
        return fpic;
    }

    public String getTotal() {
        return total;
    }

    public String getFoodUID() {
        return foodUID;
    }
}
