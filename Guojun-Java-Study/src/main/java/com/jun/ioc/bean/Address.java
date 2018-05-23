package com.jun.ioc.bean;

/**
 * Created by Administrator on 2017/6/16/016.
 */
public class Address {

    private String city;

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address [city=" + city + "]";
    }
}
