package com.gestion.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ORDER_TAB")
public class Order extends Persistent{

    @Column(name = "reservation", nullable=false)
    private boolean reservation;
    @Column(name = "QR_code")
    private String QR_code;

    @Column(name = "status", nullable=false)
    private ORDER_STATUS status;

    @ManyToOne()
    @JoinColumn(name = "customer")
    private Customer customer;

    @ManyToMany()
    @JoinTable(name = "Product_Order", joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private Payment payment;

    public Order() {
    }

    public Order(boolean reservation, String QR_code, ORDER_STATUS status, Customer customer, Payment payment) {
        this.reservation = reservation;
        this.QR_code = QR_code;
        this.status = status;
        this.customer = customer;
        this.payment = payment;
    }

    public boolean isReservation() {
        return reservation;
    }

    public void setReservation(boolean reservation) {
        this.reservation = reservation;
    }

    public String getQR_code() {
        return QR_code;
    }

    public void setQR_code(String QR_code) {
        this.QR_code = QR_code;
    }

    public ORDER_STATUS getStatus() {
        return status;
    }

    public void setStatus(ORDER_STATUS status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    /**
     * Represent the status of a order
     */
    public enum ORDER_STATUS{
        PROCESSING,
        ENDED,
    }
}
