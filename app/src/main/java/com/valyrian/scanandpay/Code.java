package com.valyrian.scanandpay;

public class Code {


        public Long _id; // for cupboard
        public String name; // bunny name
        //public String type;
        public double price;
        public String pname;
        public int qty;

        public Code() {
            this.name = "noName";
        }

        public Code(String name,String pname, double price, int qty) {
            this.name = name;
            this.pname=pname;
            this.price=price;
            this.qty=qty;
        }

}
