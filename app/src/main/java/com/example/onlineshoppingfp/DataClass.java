package com.example.onlineshoppingfp;


public class DataClass {

    private String dataProduct;
    private String dataPrice;
    private String dataQuantity;
    private String dataImage;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataProduct() {
        return dataProduct;
    }

    public String getDataPrice() {
        return dataPrice;
    }

    public String getDataQuantity() {
        return dataQuantity;
    }

    public String getDataImage() {
        return dataImage;
    }

    public DataClass(String dataProduct, String dataPrice, String dataQuantity, String dataImage) {
        this.dataProduct = dataProduct;
        this.dataPrice = dataPrice;
        this.dataQuantity = dataQuantity;
        this.dataImage = dataImage;
    }
    public DataClass(){

    }
}