package com.gestion.controller;

import com.gestion.model.Product;
import com.gestion.model.User;
import com.gestion.service.AccountService;
import com.gestion.service.ProductService;
import com.gestion.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping(value="admin")
public class AdminController {

    protected Logger logger;

    @Autowired
    AccountService accountService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;


    public AdminController() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    @RequestMapping("/user-list")
    public String viewUserListPage(Model model) {
        model.addAttribute("listUser", userService.getRepository().findAll());
        return "userList";
    }

    @RequestMapping("/show-add-user-form")
    public String addUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "addUser";
    }

    @PostMapping("/registration/{role}")
    public String registration(@PathVariable String role,
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {
        User existingUser = userService.getRepository().findByEmail(user.getEmail());

        if (existingUser != null)
            result.rejectValue("email", null,
                    "User already registered !!!");

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/admin/show-add-user-form";
        }

        if(role.equals("ADMIN"))
            user.setRole(User.ROLE.ADMIN);
        else
            user.setRole(User.ROLE.EMPLOYEE);

        userService.saveNewUser(user);
        return "redirect:/admin/userList";
//        return "redirect:/registration?success";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
        User existingUser = userService.getRepository().findByEmail(user.getEmail());

        if (existingUser != null)
            result.rejectValue("email", null,
                    "User already registered !!!");

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/admin/show-add-user-form";
        }

        userService.saveNewUser(user);
        return "redirect:/admin/user-list";
//        return "redirect:/registration?success";
    }

    @GetMapping("/show-update-user-form/{id}")
    public String showFormForUpdateUser(@PathVariable(value = "id") long id, Model model) {

        // get user from the service
        Optional<User> user = userService.getRepository().findById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/update-user")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
        Optional<User> existingUser = userService.getRepository().findById(user.getId());

        if (existingUser != null)
            result.rejectValue("email", null,
                    "User not found !!!");

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/admin/show-update-user-form";
        }

        userService.updateUser(user);
        return "redirect:/admin/user-list";
    }

    @GetMapping("/delete-user-by-id/{id}")
    public String deleteEmployee(@PathVariable(value = "id") long id) {

        // call delete employee method
        userService.getRepository().deleteById(id);
        return "redirect:/";
    }





    // Resources for Product

    /**
     * Save or update Product
     * @param product to be created
     * @return The created Product or update if already exists
     */
    @PostMapping("product/save")
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        logger.debug("Call : Save Product");
        return ResponseEntity.ok(productService.getRepository().save(product));
    }
}
