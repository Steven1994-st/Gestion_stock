package com.gestion.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="PAYMENT")
public class Payment extends Persistent{

    @Column(name = "ref_Transaction", nullable=false)
    private String refTransaction;
    @Column(name = "amount", nullable=false)
    private float amount;

    //    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    public Payment() {
    }

    public Payment(String refTransaction, float amount, Order order) {
        this.refTransaction = refTransaction;
        this.amount = amount;
        this.order = order;
    }

    public String getRefTransaction() {
        return refTransaction;
    }

    public void setRefTransaction(String refTransaction) {
        this.refTransaction = refTransaction;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
