package com.example.humarahub;

public class AddressesModel {

    private String name;
    private String address;
    private String pincode;
    private boolean selected;

    public AddressesModel(String name, String address, String pincode,boolean selected) {
        this.name = name;
        this.address = address;
        this.pincode = pincode;
        this.selected=selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
