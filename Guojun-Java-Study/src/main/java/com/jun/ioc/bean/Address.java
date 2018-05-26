package com.jun.ioc.bean;

/**
 * 
 * @Description Address
 * @author Guojun
 * @Date 2018年5月26日 下午3:55:57
 *
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
