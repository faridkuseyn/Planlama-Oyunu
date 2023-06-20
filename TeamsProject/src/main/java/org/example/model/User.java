package org.example.model;

public class User {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String tc;
    private String phone;
    private String email;
    private String address;
    private String usertype;

    public User(String username, String password, String name, String surname, String tc, String phone, String email, String address, String userType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.tc = tc;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.usertype = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserType() {
        return usertype;
    }

    public void setUserType(String userType) {
        this.usertype = userType;
    }
}
