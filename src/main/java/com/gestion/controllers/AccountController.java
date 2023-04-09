package com.gestion.controllers;

import com.gestion.models.User;
import com.gestion.security.UserDetailsImpl;
import com.gestion.services.AccountService;
import com.gestion.services.UserService;
import com.gestion.utils.JwtResponse;
import com.gestion.utils.JwtUtils;
import com.gestion.utils.LoginRequest;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "account/")
public class AccountController {
    protected Logger logger;

    private final AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;


    public AccountController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * User login
     * @param loginRequest
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findByLogin(loginRequest.getUsername());
        if(!user.isActive()){
            throw new RuntimeException("Account not activated");
        }
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(),
                roles, user));
    }

    /**
     * Employee registration
     * @param employee
     * @return
     * @throws Exception
     */
    @PostMapping(value = "register/employee")
    public ResponseEntity<?> registerEmployee(@RequestBody User employee) throws Exception {
        employee = userService.saveNewEmployee(employee);
        accountService.sendValidateAccount(employee);
        return ResponseEntity.ok(employee);
    }

    /**
     * Administrator registration
     * @param admin
     * @return
     * @throws Exception
     */
    @PostMapping(value = "register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody User admin) throws Exception {
        admin = userService.saveNewAdmin(admin);
        accountService.sendValidateAccount(admin);
        return ResponseEntity.ok(admin);
    }

    /**
     * Update User
     * @param user
     * @return
     */
    @PostMapping(value = "update")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        logger.debug("Call : Update User");

        return ResponseEntity.ok(userService
                .updateUser(user));
    }

    /**
     * Check if the password provided by the User is correct
     * @param user
     * @return
     */
    @PostMapping(value = "check-password")
    public ResponseEntity<?> checkPassword(@RequestBody User user){

        return ResponseEntity.ok(userService.checkPassword(user));
    }

    /**
     * Update User password
     * @param user
     * @return
     */
    @PostMapping(value = "password/update")
    public ResponseEntity<?> updatePassword(@RequestBody User user){
        logger.debug("Call : Update password");

        return ResponseEntity.ok(userService
                .updatePassword(user));
    }

    /**
     * Send the account activation code to a user
     * @param user
     * @return
     * @throws Exception
     */
    @PermitAll
    @PostMapping(value = "register/send-code")
    public ResponseEntity<?> sendAccountActivationCode(@RequestBody User user) throws Exception{
        logger.debug("Call : Send account reset code");
        boolean result = accountService.sendAccountActivationCode(user);

        return ResponseEntity.ok(result);
    }

    /**
     * Activate an account when creating it with the activation code
     * @param user
     * @return
     * @throws Exception
     */
    @PermitAll
    @PostMapping(value = "register/activation")
    public ResponseEntity<?> accountActivation(@RequestBody User user) throws Exception{
        logger.debug("Call : Account Activation");
        boolean result = accountService.accountActivation(user);

        return ResponseEntity.ok(result);
    }

    /**
     * Send password reset code
     * @param user
     * @return
     * @throws Exception
     */
    @PermitAll
    @PostMapping(value = "reset-password/send-code")
    public ResponseEntity<?> sendPasswordResetCode(@RequestBody User user) throws Exception{
        logger.debug("Call : Sent Password Reset Code");
        user = accountService.sendPasswordResetCode(user);

        return ResponseEntity.ok(user);
    }

    /**
     * Validate the activation code when resetting the password
     * @param user
     * @return
     * @throws Exception
     */
    @PermitAll
    @PostMapping(value = "reset-password/validation")
    public ResponseEntity<?> passwordValidation(@RequestBody User user) throws Exception{
        logger.debug("Call : Validation Reset Code");
        boolean result = accountService.passwordValidation(user);

        return ResponseEntity.ok(result);
    }

}
