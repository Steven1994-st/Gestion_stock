package com.gestion.controller;

import com.gestion.model.*;
import com.gestion.repository.PaymentRepository;
import com.gestion.service.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        productService.getRepository().save(product);
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

    @RequestMapping("/product-search")
    public String searchProduct(Model model, String keyword) {
        if(keyword!=null) {
            List<Product> productList = productService.search(keyword);
            model.addAttribute("listProduct", productList);
        }else {
            model.addAttribute("listProduct", productService.getRepository().findAll());
        }
        return "productList";
    }



    // RESOURCES FOR CUSTOMER

    @RequestMapping("/customer-list")
    public String viewCustomerListPage(Model model) {
        model.addAttribute("listCustomer", customerService.getRepository().findAll());
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
                                 BindingResult result,
                                 Model model) {
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
                                BindingResult result,
                                Model model) {

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
        if(keyword!=null) {
            List<Customer> customerList = customerService.search(keyword);
            model.addAttribute("listCustomer", customerList);
        }else {
            model.addAttribute("listCustomer", productService.getRepository().findAll());
        }
        return "customerList";
    }


    // RESOURCES FOR HOLIDAY

    @RequestMapping("/holiday-list")
    public String viewHolidayListPage(Model model) {

        //Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User curentUser = userService.getRepository().findByEmail(username);

        model.addAttribute("listHoliday",
                holidayService.getRepository().findHolidaysByUser(curentUser.getId()));
        return "userHolidayList";
    }

    @RequestMapping("/show-add-holiday-form")
    public String viewAddHolidayForm(Model model) {
        Holiday holiday = new Holiday();

        //Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User curentUser = userService.getRepository().findByEmail(username);
        holiday.setUser(curentUser);

        model.addAttribute("holiday", holiday);
        return "userAddHoliday";
    }

    @PostMapping("/add-holiday")
    public String saveNewHoliday(@Valid @ModelAttribute("holiday") Holiday holiday,
                                 BindingResult result,
                                 Model model) {

        if (result.hasErrors()) {
            model.addAttribute("holiday", holiday);
            return "userAddHoliday";
        }
        holiday.setStatus(Holiday.HOLIDAY_STATUS.PROCESSING);
        holidayService.getRepository().save(holiday);
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
                                BindingResult result,
                                Model model) {

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
        if(keyword!=null) {
            List<Holiday> holidayList = holidayService.search(keyword);
            model.addAttribute("listHoliday", holidayList);
        }else {
            model.addAttribute("listHoliday", holidayService.getRepository().findAll());
        }
        return "userHolidayList";
    }


}
