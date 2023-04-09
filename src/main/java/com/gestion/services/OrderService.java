package com.gestion.services;

import com.gestion.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public OrderRepository getRepository(){
        return orderRepository;
    }
}
