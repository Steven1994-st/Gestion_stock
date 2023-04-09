package com.gestion.controllers;

import com.gestion.models.Product;
import com.gestion.models.User;
import com.gestion.services.AccountService;
import com.gestion.services.ProductService;
import com.gestion.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="admin/")
public class AdminController {

    protected Logger logger;

    @Autowired
    AccountService accountService;

    @Autowired
    ProductService productService;


    public AdminController() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    // Resources for Product

    /**
     * Save or update Product
     * @param product to be created
     * @return The created Product or update if already exists
     */
    @PostMapping("product/save")
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        logger.debug("Call : Save Product");
        return ResponseEntity.ok(productService.getRepository().save(product));
    }
}
