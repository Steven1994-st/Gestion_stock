package com.gestion.controller;

import com.gestion.model.*;
import com.gestion.service.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value="user")
public class UserController {

    protected Logger logger;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private CustomerService customerService;


    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private PaymentService paymentService;

    public UserController() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    // RESOURCES FOR PRODUCTS
   @RequestMapping("/product-list")
    public String viewProductListPage(Model model,
                                      @RequestParam (name="page",defaultValue = "0") int page,
                                      @RequestParam (name="size", defaultValue = "3") int size) {

       Page<Product> productPage = productService.getRepository().findAll(PageRequest.of(page, size));

       model.addAttribute("listProduct",productPage);
       model.addAttribute("totalPages", productPage.getTotalPages());
       model.addAttribute("totalItems", productPage.getTotalElements());
       model.addAttribute("currentPage", page);

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
                                 @RequestParam("imageData") MultipartFile imageData,
                               BindingResult result, Model model) throws IOException {

        Product productFound = productService.getRepository()
                .findProductByReference(product.getReference());

        if (productFound != null)
            result.rejectValue("reference", null,
                    "Un produit existe déjà avec cette référence !!!");

        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "addProduct";
        }
        productService.saveProductAndImage(product, imageData);

        return "redirect:/user/product-list";
    }

    @GetMapping("/show-update-product-form/{id}")
    public String showFormForUpdateProduct(@PathVariable(value = "id") long id, Model model) {

        // get product from the service
        Product product = productService.getRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Product Id:" + id));

        // set product as a model attribute to pre-populate the form
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/update-product/{id}")
    public String updateProduct(@PathVariable("id") long id,
                                @Valid @ModelAttribute("product") Product product,
                                @RequestParam("imageData") MultipartFile imageData,
                             BindingResult result, Model model) throws IOException {

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
        productService.updateProductAndImage(product, imageData);

        return "redirect:/user/product-list";
    }

    @GetMapping("/delete-product-by-id/{id}")
    public String deleteProduct(@PathVariable(value = "id") long id) {

        productService.getRepository().deleteById(id);
        return "redirect:/user/product-list";
    }

    @RequestMapping("/product-search")
    public String searchProduct(Model model, String keyword) {


        if(keyword!=null && !keyword.isEmpty()) {
            List<Product> productList = productService.search(keyword);
            model.addAttribute("listProduct", productList);
            return "productList";
        }else {
            return "redirect:/user/product-list";
        }

    }


    // RESOURCES FOR CUSTOMER

    @RequestMapping("/customer-list")
    public String viewCustomerListPage(Model model,
                                       @RequestParam (name="page",defaultValue = "0") int page,
                                       @RequestParam (name="size", defaultValue = "4") int size) {

        Page<Customer> customerPage = customerService.getRepository().findAll(PageRequest.of(page, size));
        model.addAttribute("listCustomer",customerPage);
        model.addAttribute("totalPages", customerPage.getTotalPages());
        model.addAttribute("totalItems", customerPage.getTotalElements());
        model.addAttribute("currentPage", page);

        return "customerList";
    }

    @RequestMapping("/show-add-customer-form")
    public String viewAddCustomerForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "addCustomer";
    }

    @PostMapping("/add-customer")
    public String saveNewCustomer(@Valid @ModelAttribute("customer") Customer customer,
                                 BindingResult result, Model model) {
        Customer customerFound = customerService.getRepository()
                .findByEmail(customer.getEmail());

        if (customerFound != null)
            result.rejectValue("email", null,
                    "Un client existe déjà avec cet email !!!");

        if (result.hasErrors()) {
            model.addAttribute("customer", customer);
            return "addCustomer";
        }
        customerService.getRepository().save(customer);
        return "redirect:/user/customer-list";
    }

    @GetMapping("/show-update-customer-form/{id}")
    public String showFormForUpdateCustomer(@PathVariable(value = "id") long id, Model model) {

        // get customer from the service
        Customer customer = customerService.getRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Customer Id:" + id));

        // set customer as a model attribute to pre-populate the form
        model.addAttribute("customer", customer);
        return "editCustomer";
    }

    @PostMapping("/update-customer/{id}")
    public String updateCustomer(@PathVariable("id") long id, @Valid @ModelAttribute("customer") Customer customer,
                                BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("customer", customer);
            return "editCustomer";
        }

        customer.setId(id);
        customerService.updateCustomer(customer);
        return "redirect:/user/customer-list";
    }

    @GetMapping("/delete-customer-by-id/{id}")
    public String deleteCustomer(@PathVariable(value = "id") long id) {

        productService.getRepository().deleteById(id);
        return "redirect:/user/customer-list";
    }

    @RequestMapping("/customer-search")
    public String searchCustomer(Model model, String keyword) {
        if(keyword!=null && !keyword.isEmpty()) {
            List<Customer> customerList = customerService.search(keyword);
            model.addAttribute("listCustomer", customerList);
            return "customerList";
        }else {
            return "redirect:/user/customer-list";
        }

    }


    // RESOURCES FOR ORDER

    @RequestMapping("/order-list")
    public String viewOrderListPage(Model model,
                                    @RequestParam (name="page",defaultValue = "0") int page,
                                    @RequestParam (name="size", defaultValue = "3") int size) {

        Page<Order> orderPage = orderService.getRepository().findAll(PageRequest.of(page, size));
        model.addAttribute("listOrder",orderPage);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("processing", Order.ORDER_STATUS.PROCESSING);
        model.addAttribute("ended", Order.ORDER_STATUS.ENDED);

        return "orderList";
    }

    @RequestMapping("/show-add-order-form")
    public String viewAddOrderForm(Model model) {
        Order order = new Order();

        model.addAttribute("customerList", customerService.getRepository().findAll());
        model.addAttribute("productList", productService.getRepository().findAll());
        model.addAttribute("order", order);
        return "addOrder";
    }

    @PostMapping("/add-order")
    public String saveNewOrder(@Valid @ModelAttribute("order") Order order,
                                 BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("order", order);
            return "addOrder";
        }

        orderService.getRepository().save(order);
        return "redirect:/user/order-list";
    }

    @GetMapping("/show-update-order-form/{id}")
    public String showFormForUpdateOrder(@PathVariable(value = "id") long id, Model model) {

        // get order from the service
        Order order = orderService.getRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));

        // set order as a model attribute to pre-populate the form
        model.addAttribute("productList", productService.getRepository().findAll());
        model.addAttribute("order", order);
        return "editOrder";
    }

    @PostMapping("/update-order/{id}")
    public String updateOrder(@PathVariable("id") long id, @Valid @ModelAttribute("order") Order order,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            model.addAttribute("order", order);
            return "editOrder";
        }

        orderService.updateOrder(order);
        return "redirect:/user/order-list";
    }

    @GetMapping("/delete-order-by-id/{id}")
    public String deleteOrder(@PathVariable(value = "id") long id) {

        orderService.getRepository().deleteById(id);
        return "redirect:/user/order-list";
    }

    @RequestMapping("/order-search")
    public String searchOrder(Model model, String keyword) {
        if(keyword!=null && !keyword.isEmpty()) {
            List<Order> orderList = orderService.search(keyword);
            model.addAttribute("listOrder", orderList);
            return "orderList";
        }else {
            return "redirect:/user/order-list";
        }
    }

    @RequestMapping("/view-product-order/{orderId}")
    public String viewProductOrderPage(@PathVariable(value = "orderId") long orderId, Model model) {

        Order order = orderService.getRepository().findById(orderId).get();
        model.addAttribute("order", order);
        model.addAttribute("listOrderProduct", order.getOrderProducts());
        model.addAttribute("productList", productService.getRepository().findAll());
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order);
        model.addAttribute("orderProduct", orderProduct);

        return "viewProductsOrder";
    }

    @RequestMapping("/add-order-product")
    public String viewAddProductOrderPage(@Valid @ModelAttribute("orderProduct") OrderProduct orderProduct,
                                          BindingResult result, Model model) {

        OrderProduct orderProductFound = orderProductService.getRepository()
                .findOrderProductByOrderAndProduct(orderProduct.getOrder().getId(), orderProduct.getProduct().getId());

        if (orderProductFound != null )
            result.rejectValue("product", null,
                    "Ce produit existe déjà dans cette commande !!!");

        if (orderProduct.getQuantity() == 0 )
            result.rejectValue("quantity", null,
                    "La quantité est requise !!!");

        Product product =productService.getRepository()
                .findProductByName(orderProduct.getProduct().getName());

        int stockQuantity = product.getQuantity();

        if (stockQuantity < orderProduct.getQuantity())
            result.rejectValue("quantity", null,
                    "La quantité en stock est insuffisante !!!");

        if (result.hasErrors()) {
            model.addAttribute("orderProduct", orderProduct);
            model.addAttribute("order", orderProduct.getOrder());
            model.addAttribute("listOrderProduct", orderProduct.getOrder().getOrderProducts());
            model.addAttribute("productList", productService.getRepository().findAll());
            return "viewProductsOrder";
        }

        orderProductService.getRepository().save(orderProduct);

        // Mise à jour de la quantité du produit en stock
        product.setQuantity(product.getQuantity() - orderProduct.getQuantity());
        productService.updateProduct(product);

        // Mise à jour du montant total de la commande
        orderService.orderAmountUpdate(orderProduct.getOrder());

        return "redirect:/user/view-product-order/"+orderProduct.getOrder().getId();
    }

    @GetMapping("/delete-product-order-by-key/{orderId}/{productId}")
    public String deleteProductOrder(@PathVariable(value = "orderId") Long orderId, @PathVariable(value = "productId") Long productId) {

        OrderProduct orderProduct = orderProductService.getRepository()
                .findOrderProductByOrderAndProduct(orderId, productId);


        orderProductService.getRepository().deleteById(orderProduct.getOrderProductKey());

        Product product = productService.getRepository()
                .findProductByName(orderProduct.getProduct().getName());

        // Mise à jour de la quantité du produit en stock
        product.setQuantity(product.getQuantity() + orderProduct.getQuantity());
        productService.updateProduct(product);

        // Mise à jour du montant total de la commande
        orderService.orderAmountUpdate(orderProduct.getOrder());


        return "redirect:/user/view-product-order/"+orderProduct.getOrder().getId();
    }


    // RESOURCES FOR HOLIDAY

    @RequestMapping("/holiday-list")
    public String viewHolidayListPage(Model model,
                                      @RequestParam (name="page",defaultValue = "0") int page,
                                      @RequestParam (name="size", defaultValue = "4") int size) {

        //Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getRepository().findByEmail(username);

        Pageable pageable = PageRequest.of(page, size);
        Page<Holiday> holidayPage = holidayService.getRepository().findHolidaysByUser(currentUser.getId(), pageable);
        model.addAttribute("listHoliday",holidayPage);
        model.addAttribute("totalPages", holidayPage.getTotalPages());
        model.addAttribute("totalItems", holidayPage.getTotalElements());
        model.addAttribute("currentPage", page);

        return "userHolidayList";
    }

    @RequestMapping("/show-add-holiday-form")
    public String viewAddHolidayForm(Model model) {
        Holiday holiday = new Holiday();

        //Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getRepository().findByEmail(username);
        holiday.setUser(currentUser);

        model.addAttribute("holiday", holiday);
        return "userAddHoliday";
    }

    @PostMapping("/add-holiday")
    public String saveNewHoliday(@Valid @ModelAttribute("holiday") Holiday holiday,
                                 BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("holiday", holiday);
            return "userAddHoliday";
        }
        holidayService.saveHoliday(holiday);
        return "redirect:/user/holiday-list";
    }

    @GetMapping("/show-update-holiday-form/{id}")
    public String showFormForUpdateHoliday(@PathVariable(value = "id") long id, Model model) {

        // get holiday from the service
        Holiday holiday = holidayService.getRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Customer Id:" + id));

        // set holiday as a model attribute to pre-populate the form
        model.addAttribute("holiday", holiday);
        return "userEditHoliday";
    }

    @PostMapping("/update-holiday/{id}")
    public String updateHoliday(@PathVariable("id") long id, @Valid @ModelAttribute("holiday") Holiday holiday,
                                BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("holiday", holiday);
            return "userEditHoliday";
        }

        holiday.setId(id);
        holiday.setStatus(Holiday.HOLIDAY_STATUS.PROCESSING);
        holidayService.updateHoliday(holiday);
        return "redirect:/user/holiday-list";
    }

    @GetMapping("/delete-holiday-by-id/{id}")
    public String deleteHoliday(@PathVariable(value = "id") long id) {

        holidayService.getRepository().deleteById(id);
        return "redirect:/user/holiday-list";
    }

    @RequestMapping("/holiday-search")
    public String searchHoliday(Model model, String keyword) {
        if(keyword!=null && !keyword.isEmpty()) {
            List<Holiday> holidayList = holidayService.search(keyword);
            model.addAttribute("listHoliday", holidayList);
            return "userHolidayList";
        }else {
            return "redirect:/user/holiday-list";
        }

    }


    // RESOURCES FOR PROFILE

    @RequestMapping("/view-profile")
    public String viewProfilePage(Model model) {

        //Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getRepository().findByEmail(username);

        model.addAttribute("user", currentUser);

        return "viewProfile";
    }

    @RequestMapping("/show-update-profile-form")
    public String viewUpdateProfileFormPage(Model model) {

        //Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getRepository().findByEmail(username);

        // set user as a model attribute to pre-populate the form
        model.addAttribute("user", currentUser);
        return "updateProfile";
    }

    @PostMapping("/update-profile/{id}")
    public String updateProfile(@PathVariable("id") long id, @Valid @ModelAttribute("user") User user,
                                BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "updateProfile";
        }

        user.setId(id);
        userService.updateUser(user);

        return "redirect:/user/view-profile";
    }


    /**
     * Affichage de la page de facture et payement de commande
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping("/show-order-payment-form/{orderId}")
    public String viewOrderPaymentFormPage(@PathVariable(value = "orderId") long orderId,
                                           Model model) {

        Order order = orderService.getRepository().findById(orderId).get();
        model.addAttribute("order", order);
        model.addAttribute("listOrderProduct", order.getOrderProducts());
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order);
        model.addAttribute("orderProduct", orderProduct);
        model.addAttribute("processing", Order.ORDER_STATUS.PROCESSING);
        model.addAttribute("ended", Order.ORDER_STATUS.ENDED);

        return "orderPayment";
    }


    /**
     * Enregistrement de la facture d'une commande
     * @param orderId
     * @return
     */
    @RequestMapping("/order-payment/{orderId}")
    public String OrderPayment(@PathVariable("orderId") long orderId, Model model) {

        Order order = orderService.getRepository()
                .findById(orderId).get();
        Payment payment = paymentService.createOrderPayment(order);
        order.setStatus(Order.ORDER_STATUS.ENDED);
        order.setPayment(payment);
        orderService.updateOrder(order);

        model.addAttribute("listOrder",
                orderService.getRepository().findAll());

        return "redirect:/user/order-list";
    }

    // RESOURCES FOR NOTIFICATION

    @RequestMapping("/notification-list")
    public String viewNotificationPage(Model model,
                                       @RequestParam (name="page",defaultValue = "0") int page,
                                       @RequestParam (name="size", defaultValue = "5") int size) {

        //Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getRepository().findByEmail(username);

        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notificationPage = notificationService
                .getRepository().findNotificationsByUser(currentUser.getId(), pageable);

        model.addAttribute("notificationList", notificationPage);
        model.addAttribute("totalPages", notificationPage.getTotalPages());
        model.addAttribute("totalItems", notificationPage.getTotalElements());
        model.addAttribute("currentPage", page);

        return "notificationList";
    }

    @GetMapping("/delete-notification-by-id/{id}")
    public String deleteNotification(@PathVariable(value = "id") long id) {

        notificationService.getRepository().deleteById(id);
        return "redirect:/user/notification-list";
    }

    @RequestMapping("/notification-search")
    public String searchNotification(Model model, String keyword) {
        if(keyword!=null && !keyword.isEmpty()) {
            List<Notification> notificationList = notificationService.search(keyword);
            model.addAttribute("notificationList", notificationList);
            return "notificationList";
        }else {
            model.addAttribute("notificationList", notificationService.getRepository().findAll());
            return "redirect:/user/notification-list";
        }
    }

    /////EXPORT FILES///

    @GetMapping("/excel")
    public void generateExcelReport(HttpServletResponse response) throws Exception{

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=Listes des produits en stoks.xls";

        response.setHeader(headerKey, headerValue);

        productService.generateExcel(response);
        response.flushBuffer();
    }

    @GetMapping("/excel_order")
    public void generateExcelReport_order(HttpServletResponse response) throws Exception{

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=Listes des commandes.xls";

        response.setHeader(headerKey, headerValue);

        orderService.generateExcel_order(response);
        response.flushBuffer();
    }

    @GetMapping("/excel_customer")
    public void generateExcelReport_customer(HttpServletResponse response) throws Exception{

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=Listes des clients.xls";

        response.setHeader(headerKey, headerValue);

        customerService.generateExcel_customer(response);
        response.flushBuffer();
    }

}



