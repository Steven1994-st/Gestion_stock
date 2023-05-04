package com.gestion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="USER")
public class User extends Persistent{
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone")
    private String phone;

    private String login;
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "role")
    private ROLE role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy ="user")
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy ="user")
    @JsonIgnore
    private List<Holiday> holidays = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy ="user")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    public User() {
    }


    public User(String name, String firstname, String email, String phone, String login, String password, String token, boolean active, ROLE role) {
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.phone = phone;
        this.login = login;
        this.password = password;
        this.token = token;
        this.active = active;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Represent the role of this user
     */
    public enum ROLE{
        ADMIN,
        EMPLOYEE,
    }
}
