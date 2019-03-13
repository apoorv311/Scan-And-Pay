package com.valyrian.scanandpay;

public class ListItem {

    private Long _id; // for cupboard
    private String name; // bunny name
    //public String type;
    private double price;
    private String pname;
    private int qty;


    public ListItem(long _id,String name,String pname, double price, int qty) {
        this._id=_id;
        this.name = name;
        this.pname=pname;
        this.price=price;
        this.qty=qty;
    }

    public Long get_Id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getPname() {
        return pname;
    }

    public int getQty() {
        return qty;
    }
}
