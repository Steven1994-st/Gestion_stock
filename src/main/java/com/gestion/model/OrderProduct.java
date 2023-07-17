package com.gestion.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ORDER_PRODUCT")
public class OrderProduct implements Serializable {

    @EmbeddedId
    OrderProductKey orderProductKey = new OrderProductKey();

    @Column(name = "quantity", nullable=false)
    private int quantity;

    @ManyToOne()
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable=false)
    private Order order;

    @ManyToOne()
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable=false)
    private Product product;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderProductKey getOrderProductKey() {
        return orderProductKey;
    }

    public void setOrderProductKey(OrderProductKey orderProductKey) {
        this.orderProductKey = orderProductKey;
    }
}
