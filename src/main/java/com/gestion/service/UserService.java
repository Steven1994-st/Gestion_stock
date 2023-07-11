package com.gestion.service;

import com.gestion.model.Product;
import com.gestion.model.User;
import com.gestion.repository.UserRepository;
import com.gestion.utils.Code;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.util.Date;
import java.util.List;

@Service()
public class UserService {
    protected static Logger logger;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get User Repository
     * @return repository
     */
    public UserRepository getRepository(){
        return userRepository;
    }

    public UserService() {
    }

    /**
     * Create a new employee
     * @param user
     * @return
     */
    public User saveNewEmployee(User user){
        user.setRole(User.ROLE.EMPLOYEE);

        return saveNewUser(user);
    }

    /**
     * Create a new administrator
     * @param user
     * @return
     */
    public User saveNewAdmin(User user){
        user.setRole(User.ROLE.ADMIN);

        return saveNewUser(user);
    }

    /**
     * Create a new user
     * @param user
     * @return
     */
    public User saveNewUser(User user){

        User userFound = getRepository().findByEmail(user.getEmail());

        //      if it is a new User and already exists with this login
        if(userFound != null){
            throw new RuntimeException("This User already exists");
        }else { //      if it's a new user and doesn't exist yet
            user.setCreationDate(new Date());
            userFound = SerializationUtils.clone(user);
            String hashPW = passwordEncoder.encode(user.getPassword());
            userFound.setPassword(hashPW);
            userFound.setToken(Code.generateCode());
//            userFound.setRole(User.ROLE.EMPLOYEE);
        }

        return getRepository().save(userFound);
    }

    /**
     * Update a User
     * @param user
     * @return
     */
    @Transactional()
    public User updateUser(User user){

        User userFound = getRepository()
                .findById(user.getId()).get();

        userFound.setFirstname(user.getFirstname());
        userFound.setName(user.getName());
//        userFound.setLogin(user.getLogin());
        userFound.setPhone(user.getPhone());

        userFound.setModificationDate(new Date());

        return getRepository().save(userFound);
    }

    /**
     * Update User password
     * @param user
     * @return
     */
    @Transactional()
    public User updatePassword(User user){

        User userFound = getRepository()
                .findById(user.getId()).get();

        String hashPW = passwordEncoder.encode(user.getPassword());
        userFound.setPassword(hashPW);
        userFound.setModificationDate(new Date());

        return getRepository().save(userFound);
    }


    /**
     * Check if the password provided by the User is correct
     * @param user
     * @return
     */
    public boolean checkPassword(User user){
        User result = getRepository().findById(user.getId()).get();
        return passwordEncoder.matches(user.getPassword(), result.getPassword());
    }


    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Search name or firstname by keyword in DB
     * @param keyword
     * @return
     */
    public List<User> search(String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class);
        String criteriaStr = "%" + keyword.toUpperCase() + "%";

        Predicate predicate = criteriaBuilder.or(criteriaBuilder
                        .like(criteriaBuilder.upper(root.get("name")), criteriaStr),
                criteriaBuilder.like(criteriaBuilder.upper(root.get("firstname")), criteriaStr));
        criteriaQuery.where(predicate);

        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
