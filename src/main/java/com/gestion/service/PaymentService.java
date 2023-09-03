package com.gestion.service;

import com.gestion.model.Order;
import com.gestion.model.Payment;
import com.gestion.repository.PaymentRepository;
import com.gestion.utils.Code;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;


    /**
     * Get Product Repository
     * @return repository
     */
    public PaymentRepository getRepository(){
        return paymentRepository;
    }

    /**
     * Create an order payment
     * @param order
     * @return
     */
    @Transactional()
    public Payment createOrderPayment(Order order){

        Payment payment = new Payment();
        payment.setAmount(order.getAmount());
        payment.setOrder(order);
        payment.setRefTransaction(Code.getRandomStr(5));
        payment.setCreationDate(new Date());
        payment.setModificationDate(new Date());

        return getRepository().save(payment);
    }

}
