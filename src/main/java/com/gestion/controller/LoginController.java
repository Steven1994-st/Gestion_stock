package com.gestion.controller;

import com.gestion.model.User;
import com.gestion.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    UserService userService;



//    @RequestMapping("/login/{role}")
//    public String submitLoginPage() {
//        return "login";
//    }

//    @GetMapping("/registration")
//    public String registrationForm(Model model) {
//        User user = new User();
//        model.addAttribute("user", user);
//        return "registration";
//    }
}
