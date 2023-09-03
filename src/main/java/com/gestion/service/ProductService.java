package com.gestion.service;

import com.gestion.model.Product;
import com.gestion.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductService
{
    @Autowired
    ProductRepository productRepository;


    /**
     * Get Product Repository
     * @return repository
     */
    public ProductRepository getRepository(){
        return productRepository;
    }

    /**
     * Update a Product
     * @param product
     * @return
     */
    @Transactional()
    public Product updateProduct(Product product){

        Product productFound = getRepository()
                .findById(product.getId()).get();

        productFound.setReference(product.getReference());
        productFound.setName(product.getName());
        productFound.setDescription(product.getDescription());
        productFound.setPrice(product.getPrice());
        productFound.setQuantity(product.getQuantity());

        productFound.setModificationDate(new Date());

        return getRepository().save(productFound);
    }

    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Search reference or name by keyword in DB
     * @param keyword
     * @return
     */
    public List<Product> search(String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

        Root<Product> root = criteriaQuery.from(Product.class);
        String criteriaStr = "%" + keyword.toUpperCase() + "%";

        Predicate predicate = criteriaBuilder.or(criteriaBuilder
                        .like(criteriaBuilder.upper(root.get("reference")), criteriaStr),
                criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), criteriaStr));
        criteriaQuery.where(predicate);

        TypedQuery<Product> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public Page<Product> getEMpByPaginate(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }
}
