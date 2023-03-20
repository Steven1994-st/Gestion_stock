package com.gestion.models;
import jakarta.persistence.*;

//@Entity
//@Table(name="PRODUCT")
public class Product {
	
	//@Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String nom;
	private String description;
	private float prix;
	
	public Product(int id, String nom, String description, float prix) {
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.prix = prix;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}
	
	
	
}
