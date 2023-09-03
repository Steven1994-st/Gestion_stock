package com.gestion.controller;

import com.gestion.model.Holiday;
import com.gestion.model.Product;
import com.gestion.model.User;
import com.gestion.service.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @Autowired
    HolidayService holidayService;


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

//    @PostMapping("/registration/{role}")
//    public String registration(@PathVariable String role,
//            @Valid @ModelAttribute("user") User user,
//            BindingResult result,
//            Model model) {
//        User existingUser = userService.getRepository().findByEmail(user.getEmail());
//
//        if (existingUser != null)
//            result.rejectValue("email", null,
//                    "User already registered !!!");
//
//        if (result.hasErrors()) {
//            model.addAttribute("user", user);
//            return "addUser";
//        }
//
//        if(role.equals("ADMIN"))
//            user.setRole(User.ROLE.ADMIN);
//        else
//            user.setRole(User.ROLE.EMPLOYEE);
//
//        userService.saveNewUser(user);
//        return "redirect:/admin/userList";
////        return "redirect:/registration?success";
//    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
        User existingUser = userService.getRepository().findByEmail(user.getEmail());

        if (existingUser != null)
            result.rejectValue("email", null,
                    "Un utilisateur avec cet email existe déjà !!!");

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "addUser";
        }

        userService.saveNewUser(user);
        return "redirect:/admin/user-list";
//        return "redirect:/registration?success";
    }

    @GetMapping("/show-update-user-form/{id}")
    public String showFormForUpdateUser(@PathVariable(value = "id") long id,
                                        Model model) {

        // get user from the service
        User user = userService.getRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/update-user/{id}")
    public String updateUser( @PathVariable("id") long id,
                              @Valid @ModelAttribute ("user") User user,
                               BindingResult result,
                               Model model) {
        User existingUser = userService.getRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));

//        if (existingUser != null)
//            result.rejectValue("email", null,
//                    "User not found !!!");

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "editUser";
        }

        user.setId(id);
        userService.updateUser(user);
        return "redirect:/admin/user-list";
    }

    @GetMapping("/delete-user-by-id/{id}")
    public String deleteUser(@PathVariable(value = "id") long id) {

        // call delete user method
        userService.getRepository().deleteById(id);
        return "redirect:/admin/user-list";
    }


    @RequestMapping("/user-search")
    public String searchUser(Model model, String keyword) {
        if(keyword!=null) {
            List<User> userList = userService.search(keyword);
            model.addAttribute("listUser", userList);
        }else {
            model.addAttribute("listUser", userService.getRepository().findAll());
        }
        return "userList";
    }


    // RESOURCES FOR HOLIDAY

    @RequestMapping("/holiday-list")
    public String viewHolidayListPage(Model model) {
        model.addAttribute("listHoliday", holidayService.getRepository().findAll());
        return "adminHolidayList";
    }

    @RequestMapping("/show-add-holiday-form")
    public String viewAddHolidayForm(Model model) {
        Holiday holiday = new Holiday();

        model.addAttribute("userList", userService.getRepository().findAll());
        model.addAttribute("holiday", holiday);
        return "adminAddHoliday";
    }

    @PostMapping("/add-holiday")
    public String saveNewHoliday(@Valid @ModelAttribute("holiday") Holiday holiday,
                                  BindingResult result,
                                  Model model) {

        if (result.hasErrors()) {
            model.addAttribute("holiday", holiday);
            return "adminAddHoliday";
        }
        holidayService.getRepository().save(holiday);
        return "redirect:/admin/holiday-list";
    }

    @GetMapping("/show-update-holiday-form/{id}")
    public String showFormForUpdateHoliday(@PathVariable(value = "id") long id, Model model) {

        // get holiday from the service
        Holiday holiday = holidayService.getRepository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Customer Id:" + id));

        // set holiday as a model attribute to pre-populate the form
        model.addAttribute("holiday", holiday);
        return "adminEditHoliday";
    }

    @PostMapping("/update-holiday/{id}")
    public String updateHoliday(@PathVariable("id") long id, @Valid @ModelAttribute("holiday") Holiday holiday,
                                 BindingResult result,
                                 Model model) {

        if (result.hasErrors()) {
            model.addAttribute("holiday", holiday);
            return "adminEditHoliday";
        }

        holiday.setId(id);
        holidayService.updateHoliday(holiday);
        return "redirect:/admin/holiday-list";
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
        return "adminHolidayList";
    }
}
