package com.gestion.service;

import com.gestion.model.Product;
import com.gestion.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductService
{
    @Autowired
    ProductRepository productRepository;


    /**
     * Get Product Repository
     * @return repository
     */
    public ProductRepository getRepository(){
        return productRepository;
    }

    /**
     * Update a Product
     * @param product
     * @return
     */
    @Transactional()
    public Product updateProduct(Product product){

        Product productFound = getRepository()
                .findById(product.getId()).get();

        productFound.setReference(product.getReference());
        productFound.setName(product.getName());
        productFound.setDescription(product.getDescription());
        productFound.setPrice(product.getPrice());
        productFound.setQuantity(product.getQuantity());

        productFound.setModificationDate(new Date());

        return getRepository().save(productFound);
    }
}
