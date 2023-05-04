package com.gestion.service;

import com.gestion.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public OrderRepository getRepository(){
        return orderRepository;
    }
}
