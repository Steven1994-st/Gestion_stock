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

    @OneToMany(fetch = FetchType.LAZY, mappedBy ="customer")
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();


    public Customer() {
    }

    public Customer(String name, String firstname, String phone, String email) {
        this.name = name;
        this.firstname = firstname;
        this.phone = phone;
        this.email = email;
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
