package com.gestion.services;

import com.gestion.models.Product;
import com.gestion.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService
{
    @Autowired
    ProductRepository productRepository;

    public Product createProduct (String nom,String description,String prix)
    {
        Product souris =new Product();
        souris.setNom(nom);
        souris.setDescription(description);
        souris.setPrix(Float.parseFloat(prix));

        return productRepository.save(souris);
    }
}
