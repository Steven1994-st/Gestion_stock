package com.gestion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ORDER_TAB")
public class Order extends Persistent{

    @Column(name = "description")
    private String description;
    @Column(name = "reservation", nullable=false)
    private boolean reservation;
    @Column(name = "QR_code")
    private String QR_code;

    @Column(name = "status", nullable=false)
    private ORDER_STATUS status;

    @ManyToOne()
    @JoinColumn(name = "customer")
    private Customer customer;

//    @ManyToMany()
//    @JoinTable(name = "Product_Order", joinColumns = @JoinColumn(name = "order_id"),
//            inverseJoinColumns = @JoinColumn(name = "product_id"))
//    private List<Product> products = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy ="order")
    @JsonIgnore
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public Order() {
    }

    public Order(String description, boolean reservation, String QR_code, ORDER_STATUS status, Customer customer, List<Product> products, Payment payment) {
        this.description = description;
        this.reservation = reservation;
        this.QR_code = QR_code;
        this.status = status;
        this.customer = customer;
        this.payment = payment;
    }

    public Order(String description, boolean reservation, String QR_code, ORDER_STATUS status, Customer customer, Payment payment, List<OrderProduct> orderProducts) {
        this.description = description;
        this.reservation = reservation;
        this.QR_code = QR_code;
        this.status = status;
        this.customer = customer;
        this.payment = payment;
        this.orderProducts = orderProducts;
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

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Represent the status of a order
     */
    public enum ORDER_STATUS{
        PROCESSING,
        ENDED,
    }
}
