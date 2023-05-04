package com.gestion.repository;

import com.gestion.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order,Long> {

    /**
     * Search Order by Customer id
     * @param idCustomer
     * @return list of orders retrieved
     */
    @Query("SELECT r FROM Order r where r.customer=:p")
    List<Order> findOrderByCustomer(@Param("p") String idCustomer);
}
