package com.gestion.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="PRODUCT")
public class Product extends Persistent{

	@Column(name = "reference", nullable=false)
	private String reference;
	@Column(name = "name", nullable=false)
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "price", nullable=false)
	private float price;
	@Column(name = "quantity", nullable=false)
	private long quantity;

	@ManyToOne()
	@JoinColumn(name = "user")
	private User user;

//	@ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
//	@JsonIgnore
//	private List<Order> orders = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy ="product")
//	@JsonIgnore
	private List<OrderProduct> orderProducts = new ArrayList<>();

	public Product() {
	}

	public Product(String reference, String name, String description, float price, long quantity, User user) {
		this.reference = reference;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.user = user;
	}

	public Product(String reference, String name, String description, float price, long quantity, User user, List<OrderProduct> orderProducts) {
		this.reference = reference;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.user = user;
		this.orderProducts = orderProducts;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}
}
