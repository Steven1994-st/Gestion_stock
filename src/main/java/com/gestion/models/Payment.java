package com.gestion.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="PAYMENT")
public class Payment extends Persistent{

    @Column(name = "ref_transaction", nullable=false)
    private String ref_transaction;
    @Column(name = "amount", nullable=false)
    private long amount;

    @JsonIgnore
    @OneToOne(mappedBy = "payment")
    private Order order;

    public Payment() {
    }

    public Payment(String ref_transaction, long amount, Order order) {
        this.ref_transaction = ref_transaction;
        this.amount = amount;
        this.order = order;
    }

    public String getRef_transaction() {
        return ref_transaction;
    }

    public void setRef_transaction(String ref_transaction) {
        this.ref_transaction = ref_transaction;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
