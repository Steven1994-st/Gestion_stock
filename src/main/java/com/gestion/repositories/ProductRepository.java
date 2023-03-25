package com.gestion.repositories;

import com.gestion.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Integer>
{
}
