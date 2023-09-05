package com.gestion.service;

import com.gestion.model.Order;
import com.gestion.model.OrderProduct;
import com.gestion.repository.OrderRepository;
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
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public OrderRepository getRepository(){
        return orderRepository;
    }

    /**
     * Update a Order
     * @param order
     * @return
     */
    @Transactional()
    public Order updateOrder(Order order){

        Order orderFound = getRepository()
                .findById(order.getId()).get();

        orderFound.setDescription(order.getDescription());
        orderFound.setCustomer(order.getCustomer());
        orderFound.setReservation(order.isReservation());
        orderFound.setStatus(order.getStatus());
        orderFound.setPayment(order.getPayment());
        orderFound.setQR_code(order.getQR_code());

        orderFound.setModificationDate(new Date());

        return getRepository().save(orderFound);
    }

    /**
     * Mise à jour du montant de la commande
     * @param order
     * @return
     */
    @Transactional()
    public void orderAmountUpdate(Order order){

        Order orderFound = getRepository().findById(order.getId()).get();

        orderFound.setAmount(0);

        orderFound.getOrderProducts().forEach(orderProduct -> {
            orderFound.setAmount(orderFound.getAmount()
                    + orderProduct.getQuantity() * orderProduct.getProduct().getPrice() );
        });

        getRepository().save(orderFound);
    }


    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Search by keyword in DB
     * @param keyword
     * @return
     */
    public List<Order> search(String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

        Root<Order> root = criteriaQuery.from(Order.class);
        String criteriaStr = "%" + keyword.toUpperCase() + "%";

        Predicate predicate = criteriaBuilder.or(criteriaBuilder
                        .like(criteriaBuilder.upper(root.get("description")), criteriaStr),
                criteriaBuilder.like(criteriaBuilder.upper(root.get("customer").get("name")), criteriaStr),
                criteriaBuilder.like(criteriaBuilder.upper(root.get("customer").get("firstname")), criteriaStr)
        );
        criteriaQuery.where(predicate);

        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
