package com.gestion.repository;

import com.gestion.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    /** Find an Customer in the database by using his email
     *
     * @param email the email
     * @return the Customer found or null
     */
    @Query("SELECT r FROM Customer r where r.email=:e")
    Customer findByEmail(@Param("e") String email);
}
