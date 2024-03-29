package com.gestion.service;

import com.gestion.model.User;
import com.gestion.repository.UserRepository;
import com.gestion.utils.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service()
public class AccountService {
    @Autowired
    EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get User Repository
     * @return repository
     */
    public UserRepository getRepository(){
        return userRepository;
    }

//    /**
//     * Send mail for User account validation when creating a new user account
//     * @param user
//     * @throws Exception
//     */
//    public void sendValidateAccount(User user)throws Exception{
//        String mailSubject = "Validation de votre compte";
//        String token = user.getToken();
//        String message = "Bonjour,\n\n Votre compte a bien été créé.\n Veuillez utiliser ce code pour le valider : "
//                + token + "\n\nMerci, \nTFE Gestion de stock";
//
//        sendMail(user.getEmail(), mailSubject, message);
//    }
//
//    /**
//     * Create and send a user account activation code
//     * @param user
//     * @return
//     * @throws Exception
//     */
//    public boolean sendAccountActivationCode(User user) throws Exception {
//
//        String token = Code.generateCode();
//        String mailSubject = "Activation du compte";
//        String message = "Bonjour,\n\n Veuillez utiliser ce code pour activer votre compte : "
//                + token + "\n\nMerci, \nTFE Gestion de stock";
//        sendMail(user.getEmail(), mailSubject, message);
//        user.setToken(token);
//        updateTokenOrStatusOrPwd(user);
//
//        return true;
//    }

    /**
     * Create and send a user password reset code
     * @param user
     * @return user
     */
    public User sendPasswordResetCode(User user) {

        String token = Code.generateCode();
        user.setToken(token);
        User user2 = updateTokenOrStatusOrPwd(user);

        String mailSubject = "Réinitialisation du mot de passe";
        String message = "Bonjour"+" "+user2.getName()+"\n\n Veuillez utiliser ce code pour réinitialiser votre mot de passe : "
                + user2.getToken()+ "\n\nMerci, \n Equipe Link électronics";
        sendMail(user2.getEmail(), mailSubject, message);

        return user2;
    }

//    /**
//     * User account activation
//     * @param user
//     * @return
//     * @throws Exception
//     */
//    public boolean accountActivation(User user)throws Exception{
//        if(codeValidation(user)){
//            user.setActive(true);
//            updateTokenOrStatusOrPwd(user);
//
//            return true;
//        }
//
//        return false;
//    }

    /**
     * User Passwords Match
     * @param password
     * @param passwordConfirm
     * @return boolean
     */
    public boolean userPasswordsMatch(String password, String passwordConfirm){
        if(password.equals(passwordConfirm))
            return true;

        return false;
    }


    /**
     * User password validation
     * @param user
     * @return boolean
     */
    public boolean passwordValidation(User user){
        if(codeValidation(user)){
            String newPassword = user.getPassword();
            String hashPW = passwordEncoder.encode(newPassword);
            user.setPassword(hashPW);
            updateTokenOrStatusOrPwd(user);

            return true;
        }

        return false;
    }

    /**
     * User code validation
     * @param user
     * @return
     */
    public boolean codeValidation(User user){
        User user2 = getRepository().findById(user.getId()).get();

        if(!user.getToken().equals(user2.getToken())){
            return false;
        }

        return true;
    }

    /**
     * Send an email to an existing user
     * @param receiverAdress
     * @param mailSubject
     * @param message
     */
    public void sendMail(String receiverAdress, String mailSubject, String message){
        emailService.sendSimpleMessage(receiverAdress, mailSubject, message);
    }

    /**
     * Update token or the activation status or password of a User account
     * @param user
     * @throws Exception
     */
    private User updateTokenOrStatusOrPwd(User user){
        User userFound = getRepository()
                .findById(user.getId()).get();

        if (user.getToken() != null){
            userFound.setToken(user.getToken());
        }
        if (user.getPassword() != null){
            userFound.setPassword(user.getPassword());
        }
//        if (!userFound.isActive()){
//            userFound.setActive(user.isActive());
//        }
        return getRepository().save(userFound);
    }

}
