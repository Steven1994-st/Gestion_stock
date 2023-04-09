package com.gestion.repositories;

import com.gestion.models.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product,Long> {

    /**
     * Search Product by name or reference
     * @param keyword
     * @return Product retrieved
     */
    @Query("SELECT r FROM Product r where r.name=:p OR r.reference =:p")
    Product findProductByNameOrReference(@Param("p") String keyword);
}
