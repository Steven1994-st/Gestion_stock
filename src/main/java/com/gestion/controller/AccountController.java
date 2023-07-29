package com.gestion.controller;

import com.gestion.model.User;
import com.gestion.security.UserDetailsImpl;
import com.gestion.service.AccountService;
import com.gestion.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
//@RequestMapping(value = "account/")
public class AccountController {
    protected Logger logger;

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;


    public AccountController() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    // display list of employees
    @GetMapping("/")
    public String viewHomePage() {
        return "login";
    }

    @RequestMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    @RequestMapping("/access-denied")
    public String viewAccessDeniedPage() {
        return "accessDenied";
    }

    @GetMapping("/show-update-password-step1-form")
    public String showFormForUpdatePasswordStep1( Model model) {

        // set holiday as a model attribute to pre-populate the form
        model.addAttribute("user", new User());
        return "updatePasswordStep1";
    }

    @PostMapping("/update-password-step1")
    public String updatePasswordStep1(@Valid @ModelAttribute("user") User user,
                                      BindingResult result, Model model) {

        User userFound = null;

        //Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getRepository().findByEmail(username);

        if (user.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "L'email est requis !!!");
        }else if (currentUser == null){
            userFound = userService.getRepository().findByEmail(user.getEmail());

            if (userFound == null){
                result.rejectValue("email", null,
                        "L'email est introuvable !!!");
            }
        }else {
            userFound = currentUser;

            if(!userFound.getEmail().equals(user.getEmail())){
                result.rejectValue("email", null,
                        "L'email renseigné n'est pas lié à ce compte !!!");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "updatePasswordStep1";
        }

        accountService.sendPasswordResetCode(userFound);

        return "redirect:/show-update-password-step2-form/"+userFound.getId();
    }

    @GetMapping("/show-update-password-step2-form/{idUser}")
    public String showFormForUpdatePasswordStep2(@PathVariable(value = "idUser") long idUser, Model model) {

        // get user from the service
        User user = userService.getRepository().findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + idUser));

        // set use as a model attribute to pre-populate the form
        user.setPassword(null);
        user.setToken(null);
        model.addAttribute("user", user);
        return "updatePasswordStep2";
    }

    @PostMapping("/update-password-step2/{idUser}")
    public String updatePasswordStep2(@PathVariable("idUser") long idUser, @Valid @ModelAttribute("user") User user,
                                      BindingResult result, Model model) {


        user.setId(idUser);

        if (user.getPassword() == null){
            result.rejectValue("password", null,
                    "Le mot de passe est réquis !!!");
        } else if (!accountService.userPasswordsMatch(
                user.getPassword(), user.getPasswordConfirm())){
            result.rejectValue("password", null,
                    "Les mots de passe ne correspondent pas !!!");
        }else if (!accountService.passwordValidation(user)){
            result.rejectValue("token", null,
                    "Le code de vérification est incorrect!!!");
        }

        if (result.hasErrors()) {
            user.setPassword(null);
            user.setPasswordConfirm(null);
            model.addAttribute("user", user);
            return "updatePasswordStep2";
        }

        //Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getRepository().findByEmail(username);

        if (currentUser == null)
            return "redirect:/login";
        else
            return "redirect:/logout";
    }

//    @GetMapping("/registration")
//    public String registrationForm() {
//        return "userRegistration";
//    }

    // display list of employees
//    @GetMapping("show-user-list")
//    public String userListPage(Model model) {
//        model.addAttribute("listUser", userService.getRepository().findAll());
//        return "userList";
//    }

    // display list of employees
//    @GetMapping("save-new-user")
//    public String SaveNewUser(@ModelAttribute("user") User user) {
//        userService.saveNewEmployee(user);
//        return "redirect:/";
//    }

//    /**
//     * Employee registration
//     * @param employee
//     * @return
//     * @throws Exception
//     */
//    @PostMapping(value = "register/employee")
//    public ResponseEntity<?> registerEmployee(@RequestBody User employee) throws Exception {
//        employee = userService.saveNewEmployee(employee);
//        accountService.sendValidateAccount(employee);
//        return ResponseEntity.ok(employee);
//    }
//
//    /**
//     * Administrator registration
//     * @param admin
//     * @return
//     * @throws Exception
//     */
//    @PostMapping(value = "register/admin")
//    public ResponseEntity<?> registerAdmin(@RequestBody User admin) throws Exception {
//        admin = userService.saveNewAdmin(admin);
//        accountService.sendValidateAccount(admin);
//        return ResponseEntity.ok(admin);
//    }
//
//    /**
//     * Update User
//     * @param user
//     * @return
//     */
//    @PostMapping(value = "update")
//    public ResponseEntity<?> updateUser(@RequestBody User user){
//        logger.debug("Call : Update User");
//
//        return ResponseEntity.ok(userService
//                .updateUser(user));
//    }
//
//    /**
//     * Check if the password provided by the User is correct
//     * @param user
//     * @return
//     */
//    @PostMapping(value = "check-password")
//    public ResponseEntity<?> checkPassword(@RequestBody User user){
//
//        return ResponseEntity.ok(userService.checkPassword(user));
//    }
//
//    /**
//     * Update User password
//     * @param user
//     * @return
//     */
//    @PostMapping(value = "password/update")
//    public ResponseEntity<?> updatePassword(@RequestBody User user){
//        logger.debug("Call : Update password");
//
//        return ResponseEntity.ok(userService
//                .updatePassword(user));
//    }
//
//    /**
//     * Send the account activation code to a user
//     * @param user
//     * @return
//     * @throws Exception
//     */
//    @PermitAll
//    @PostMapping(value = "register/send-code")
//    public ResponseEntity<?> sendAccountActivationCode(@RequestBody User user) throws Exception{
//        logger.debug("Call : Send account reset code");
//        boolean result = accountService.sendAccountActivationCode(user);
//
//        return ResponseEntity.ok(result);
//    }
//
//    /**
//     * Activate an account when creating it with the activation code
//     * @param user
//     * @return
//     * @throws Exception
//     */
//    @PermitAll
//    @PostMapping(value = "register/activation")
//    public ResponseEntity<?> accountActivation(@RequestBody User user) throws Exception{
//        logger.debug("Call : Account Activation");
//        boolean result = accountService.accountActivation(user);
//
//        return ResponseEntity.ok(result);
//    }
//
//    /**
//     * Send password reset code
//     * @param user
//     * @return
//     * @throws Exception
//     */
//    @PermitAll
//    @PostMapping(value = "reset-password/send-code")
//    public ResponseEntity<?> sendPasswordResetCode(@RequestBody User user) throws Exception{
//        logger.debug("Call : Sent Password Reset Code");
//        user = accountService.sendPasswordResetCode(user);
//
//        return ResponseEntity.ok(user);
//    }
//
//    /**
//     * Validate the activation code when resetting the password
//     * @param user
//     * @return
//     * @throws Exception
//     */
//    @PermitAll
//    @PostMapping(value = "reset-password/validation")
//    public ResponseEntity<?> passwordValidation(@RequestBody User user) throws Exception{
//        logger.debug("Call : Validation Reset Code");
//        boolean result = accountService.passwordValidation(user);
//
//        return ResponseEntity.ok(result);
//    }

}
