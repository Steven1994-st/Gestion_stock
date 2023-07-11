package com.gestion.service;

import com.gestion.model.Customer;
import com.gestion.model.Product;
import com.gestion.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service()
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;


    /**
     * Get Customer Repository
     * @return repository
     */
    public CustomerRepository getRepository(){
        return customerRepository;
    }


    /**
     * Update a Customer
     * @param customer
     * @return
     */
    @Transactional()
    public Customer updateCustomer(Customer customer){

        Customer customerFound = getRepository()
                .findById(customer.getId()).get();

        customerFound.setFirstname(customer.getFirstname());
        customerFound.setName(customer.getName());
        customerFound.setPhone(customer.getPhone());
        customerFound.setEmail(customer.getEmail());
        customerFound.setAddress(customer.getAddress());

        customerFound.setModificationDate(new Date());

        return getRepository().save(customerFound);
    }


    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Search name or firstname by keyword in DB
     * @param keyword
     * @return
     */
    public List<Customer> search(String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);

        Root<Customer> root = criteriaQuery.from(Customer.class);
        String criteriaStr = "%" + keyword.toUpperCase() + "%";

        Predicate predicate = criteriaBuilder.or(criteriaBuilder
                        .like(criteriaBuilder.upper(root.get("name")), criteriaStr),
                criteriaBuilder.like(criteriaBuilder.upper(root.get("firstname")), criteriaStr));
        criteriaQuery.where(predicate);

        TypedQuery<Customer> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
