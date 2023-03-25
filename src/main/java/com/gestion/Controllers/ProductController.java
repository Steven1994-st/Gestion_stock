package com.gestion.Controllers;

import com.gestion.models.Product;
import com.gestion.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="product/")

public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping(value = "saveProduct")
    public ResponseEntity<?> createProduct(@RequestBody Product product)
    {
      Product souris =productService.createProduct(product.getNom(),product.getDescription(),String.valueOf(product.getPrix()));
      return ResponseEntity.ok(souris);
    }
}
