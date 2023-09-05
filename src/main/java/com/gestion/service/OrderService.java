package com.gestion.service;

import com.gestion.model.Order;
import com.gestion.model.OrderProduct;
import com.gestion.model.Product;
import com.gestion.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.RichTextString;
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

        order.getOrderProducts().forEach(orderProduct -> {
            order.setAmount(order.getAmount()
                    + orderProduct.getQuantity() * orderProduct.getProduct().getPrice() );
        });

        getRepository().save(order);
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
                        .like(criteriaBuilder.upper(root.get("name")), criteriaStr),
                criteriaBuilder.like(criteriaBuilder.upper(root.get("firstname")), criteriaStr));
        criteriaQuery.where(predicate);

        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    /// EXPORT ORDER////////

    public void generateExcel_order(HttpServletResponse response) throws Exception{{
    }
        List<Order> orderList =orderRepository.findAll();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Product Info");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID Commande");
        row.createCell(1).setCellValue("Description");
        row.createCell(2).setCellValue("Montant");



        int dataRowIndex = 1;

        for (Order order : orderList)
        {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(order.getId());
            dataRow.createCell(1).setCellValue(order.getDescription());
            dataRow.createCell(2).setCellValue(order.getAmount());
            dataRowIndex++;
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();

    }
}
