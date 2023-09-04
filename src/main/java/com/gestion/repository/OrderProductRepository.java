package com.gestion.repository;

import com.gestion.model.OrderProduct;
import com.gestion.model.OrderProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey> {

    /**
     * Search Product Order by Order ID and Product ID
     * @param orderID
     * @param productId
     * @return Products order retrieved
     */
    @Query("SELECT r FROM OrderProduct r where r.order.id=:o and r.product.id=:p")
    OrderProduct findOrderProductByOrderAndProduct(@Param("o") Long orderID, @Param("p") Long productId);
}
