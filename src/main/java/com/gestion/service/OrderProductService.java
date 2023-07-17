package com.gestion.service;

import com.gestion.model.Order;
import com.gestion.model.OrderProduct;
import com.gestion.repository.OrderProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service()
public class OrderProductService {

    @Autowired
    OrderProductRepository orderProductRepository;

    public OrderProductRepository getRepository(){
        return orderProductRepository;
    }
}
