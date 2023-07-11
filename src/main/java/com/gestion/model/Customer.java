package com.gestion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="CUSTOMER")
public class Customer extends Persistent{

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "firstname", nullable = false)
    private String firstname;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy ="customer")
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();


    public Customer() {
    }

    public Customer(String name, String firstname, String phone, String email, String address, List<Order> orders) {
        this.name = name;
        this.firstname = firstname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
