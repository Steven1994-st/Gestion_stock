package com.gestion.repository;

import com.gestion.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product,Long> {

    /**
     * Search Product by name or reference
     * @param keyword
     * @return Product retrieved
     */
    @Query("SELECT r FROM Product r where r.name=:p OR r.reference =:p")
    Product findProductByNameOrReference(@Param("p") String keyword);

    /**
     * Search Product by name
     * @param name
     * @return Product retrieved
     */
    @Query("SELECT r FROM Product r where r.name=:n")
    Product findProductByName(@Param("n") String name);

    /**
     * Search Product by reference
     * @param reference
     * @return Product retrieved
     */
    @Query("SELECT r FROM Product r where r.reference =:r")
    Product findProductByReference(@Param("r") String reference);
}
