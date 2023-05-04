package com.gestion.service;

import com.gestion.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
