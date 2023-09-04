package com.gestion.repository;

import com.gestion.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {


    /**
     * Search Product by name or reference
     * @param keyword
     * @return Product retrieved
     */
    @Query("SELECT r FROM Product r where r.name=:p OR r.reference =:p")
    Product findProductByNameOrReference(@Param("p") String keyword);

    /*int pageNumber = 1;
    int pageSize = 5;
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    Page<Product> page = ProductRepository.findAll(pageable);*/
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
