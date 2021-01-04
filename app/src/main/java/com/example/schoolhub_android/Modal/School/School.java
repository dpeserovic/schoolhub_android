package com.example.schoolhub_android.Modal.School;

public class School {
    private String key;
    private String name;
    private String address;
    private String city;

    public School() {
    }

    public School(String key, String name, String address, String city) {
        this.key = key;
        this.name = name;
        this.address = address;
        this.city = city;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}