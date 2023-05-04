package com.gestion.repository;

import com.gestion.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment,Long> {
}
