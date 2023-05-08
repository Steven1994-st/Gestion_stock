package com.gestion.controller;

import com.gestion.model.*;
import com.gestion.repository.PaymentRepository;
import com.gestion.service.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value="user")
public class UserController {

    protected Logger logger;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;


    @Autowired
    AccountService accountService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private PaymentRepository paymentRepository;


    public UserController() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    @RequestMapping("/product-list")
    public String viewProductListPage(Model model) {
        model.addAttribute("listProduct", productService.getRepository().findAll());
        return "productList";
    }

    @RequestMapping("/show-add-product-form")
    public String viewAddProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "addProduct";
    }

    @PostMapping("/add-product")
    public String saveNewProduct(@Valid @ModelAttribute("product") Product product,
                               BindingResult result,
                               Model model) {
        Product productFound = productService.getRepository()
                .findProductByReference(product.getReference());

        if (productFound != null)
            result.rejectValue("reference", null,
                    "Un produit existe déjà avec cette référence !!!");

        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "addProduct";
        }
        product.setId(product.getId());
        productService.updateProduct(product);
        return "redirect:/user/product-list";
    }

    @GetMapping("/show-update-product-form/{id}")
    public String showFormForUpdateProduct(@PathVariable(value = "id") long id, Model model) {

        // get product from the service
        Product product = productService.getRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Product Id:" + id));

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/update-product/{id}")
    public String updateProduct(@PathVariable("id") long id, @Valid @ModelAttribute("product") Product product,
                             BindingResult result,
                             Model model) {
        Product existingProduct = productService.getRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));

//        if (existingProduct != null)
//            result.rejectValue("reference", null,
//                    "Product not found !!!");

        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "editProduct";
        }

        product.setId(id);
        productService.updateProduct(product);
        return "redirect:/user/product-list";
    }

    @GetMapping("/delete-product-by-id/{id}")
    public String deleteProduct(@PathVariable(value = "id") long id) {

        productService.getRepository().deleteById(id);
        return "redirect:/user/product-list";
    }




    // Resources for Product

    /**
     * Find Product by ID
     * @param id
     * @return Product retrieved
     */
    @GetMapping(value = "product/find-by-id/{id}")
    public ResponseEntity<?> findProductById(@PathVariable Long id){
        logger.debug("Call : Get Product by id");
        Optional<Product> result = productService.getRepository().findById(id);
        return result.map(ResponseEntity::ok).orElse(null);
    }

    /**
     * Find all Products
     * @return  List of all the Product retrieved
     */
    @GetMapping("product/list")
    public ResponseEntity<?> getAllProducts() {
        List<Product> entities = (List<Product>) productService.getRepository().findAll();
        return ResponseEntity.ok(entities);
    }

    /**
     * Find Product by nom or reference
     * @param keyword
     * @return Product retrieved
     */
    @GetMapping(value = "product/get-by-name-or-reference/{keyword}")
    public ResponseEntity<?> getProductByNameOrRef(@PathVariable String keyword){
        logger.debug("Call : Get Product by name or Reference");
        Product result = productService.getRepository()
                .findProductByNameOrReference(keyword);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete Product by ID
     * @param id
     * @return true if product deleted or false else
     */
    @DeleteMapping(value = "product/delete-by-id/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id){
        logger.debug("Call : delete product by id");
        productService.getRepository().deleteById(id);
        return ResponseEntity.ok(true);
    }


    // Resources for Order

    /**
     * Save or update Order
     * @param order to be created
     * @return The created order or update if already exists
     */
    @PostMapping("order/save")
    public ResponseEntity<?> saveOrder(@RequestBody Order order) {
        logger.debug("Call : Save Order");
        return ResponseEntity.ok(orderService.getRepository().save(order));
    }

    /**
     * Find Order by ID
     * @param id
     * @return Order retrieved
     */
    @GetMapping(value = "order/find-by-id/{id}")
    public ResponseEntity<?> findOrderById(@PathVariable Long id){
        logger.debug("Call : Get Order by id");
        Optional<Order> result = orderService.getRepository().findById(id);
        return result.map(ResponseEntity::ok).orElse(null);
    }

    /**
     * Search Orders by Customer ID
     * @param idCustomer
     * @return Order list retrieved
     */
    @GetMapping(value = "order/get-by-customer/{idCustomer}")
    public ResponseEntity<?> getOrderByCustomer(@PathVariable String idCustomer){
        logger.debug("Call : Get Order by Client ID");
        List<Order> entities = orderService.getRepository().findOrderByCustomer(idCustomer);
        return ResponseEntity.ok(entities);
    }

    /**
     * Find all Order
     * @return  List of all the Order retrieved
     */
    @GetMapping("order/list")
    public ResponseEntity<?> getAllOrders() {
        List<Order> entities = (List<Order>) orderService.getRepository().findAll();
        return ResponseEntity.ok(entities);
    }

    /**
     * Delete Order by ID
     * @param id
     * @return true if order deleted or false else
     */
    @DeleteMapping(value = "order/delete-by-id/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable Long id){
        logger.debug("Call : delete order by id");
        orderService.getRepository().deleteById(id);
        return ResponseEntity.ok(true);
    }


    // RESOURCES FOR NOTIFICATION

    /**
     * Save notification
     * @param notification
     * @return
     */
    @PostMapping(value = "notification/save")
    public ResponseEntity<?> saveNotification(@RequestBody Notification notification){
        logger.debug("Call : Save Notification");

        return ResponseEntity.ok(notificationService.saveNotification(notification));
    }

    /**
     * Find All Notification by User
     * @param userId for User ID
     * @return
     */
    @GetMapping(value = "notification/get-by-user/{userId}")
    public ResponseEntity<?> findNotificationsByUser(@PathVariable Long userId){
        logger.debug("Call : Find All Notifications by User");

        return ResponseEntity.ok(notificationService.getRepository()
                .findNotificationsByUser(userId));
    }

    /**
     * Find Unread Notifications by User
     * @param userId for User ID
     * @return
     */
    @GetMapping(value = "notification/unread/get-by-user/{userId}")
    public ResponseEntity<?> findUnreadNotificationsByUser(@PathVariable Long userId){
        logger.debug("Call : Find Unread Notifications by User");

        return ResponseEntity.ok(notificationService.getRepository()
                .findNotificationsByStatusAndUser(userId, Notification.NOTIFICATION_STATUS.UNREAD));
    }

    // RESOURCES FOR HOLIDAY

    /**
     * Save or update Holiday
     * @param holiday
     * @return Holiday saved or updated
     */
    @PostMapping(value = "holiday/save")
    public ResponseEntity<?> saveHoliday(@RequestBody Holiday holiday){
        logger.debug("Call : Save Holiday");

        return ResponseEntity.ok(holidayService.getRepository()
                .save(holiday));
    }

    /**
     * Find All Holiday by User
     * @param userId for User ID
     * @return
     */
    @GetMapping(value = "holiday/get-by-user/{userId}")
    public ResponseEntity<?> findHolidaysByUser(@PathVariable Long userId){
        logger.debug("Call : Find All Holiday by User");

        return ResponseEntity.ok(holidayService.getRepository()
                .findHolidaysByUser(userId));
    }

    /**
     * Find Processing Holiday by User
     * @param userId for User ID
     * @return
     */
    @GetMapping(value = "holiday/processing/get-by-user/{userId}")
    public ResponseEntity<?> findProcessingHolidaysByUser(@PathVariable Long userId){
        logger.debug("Call : Find Processing holiday by User");

        return ResponseEntity.ok(holidayService.getRepository()
                .findHolidaysByStatusAndUser(userId, Holiday.HOLIDAY_STATUS.PROCESSING));
    }

    /**
     * Find Accept Holiday by User
     * @param userId for User ID
     * @return
     */
    @GetMapping(value = "holiday/accept/get-by-user/{userId}")
    public ResponseEntity<?> findAcceptHolidaysByUser(@PathVariable Long userId){
        logger.debug("Call : Find Accept holiday by User");

        return ResponseEntity.ok(holidayService.getRepository()
                .findHolidaysByStatusAndUser(userId, Holiday.HOLIDAY_STATUS.ACCEPT));
    }

    /**
     * Find Refuse Holiday by User
     * @param userId for User ID
     * @return
     */
    @GetMapping(value = "holiday/refuse/get-by-user/{userId}")
    public ResponseEntity<?> findRefuseHolidaysByUser(@PathVariable Long userId){
        logger.debug("Call : Find Refuse holiday by User");

        return ResponseEntity.ok(holidayService.getRepository()
                .findHolidaysByStatusAndUser(userId, Holiday.HOLIDAY_STATUS.REFUSE));
    }


    // RESOURCES FOR PAYMENT

    /**
     * Save or update payment
     * @param payment
     * @return Payment saved or updated
     */
    @PostMapping(value = "payment/save")
    public ResponseEntity<?> savePayment(@RequestBody Payment payment){
        logger.debug("Call : Save Payment");

        return ResponseEntity.ok(paymentRepository.save(payment));
    }

}
