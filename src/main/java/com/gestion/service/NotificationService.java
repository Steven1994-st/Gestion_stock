package com.gestion.service;

import com.gestion.model.Notification;
import com.gestion.model.User;
import com.gestion.repository.NotificationRepository;
import com.gestion.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service()
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Get Notification Repository
     * @return repository
     */
    public NotificationRepository getRepository(){
        return notificationRepository;
    }

    /**
     * Send notification to all users
     * @param message
     * @return notification sent
     */
    public void sendNotificationToAdmins(String message){
        List<User> admins = userRepository.findByRole(User.ROLE.ADMIN);
        admins.forEach(admin ->{
            sendNotification(message, admin);
        });
    }

    /**
     * Send notification
     * @param message
     * @param user
     * @return notification sent
     */
    public Notification sendNotification(String message, User user){
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setStatus(Notification.NOTIFICATION_STATUS.UNREAD);
        notification.setCreationDate(new Date());

        return notificationRepository.save(notification);
    }

    /**
     * Update notification
     * @param notification
     * @return notification updated
     */
    public Notification updateNotification(Notification notification){
        if (notification.getId()!=null){
            Optional<Notification> result = getRepository().findById(notification.getId());
            if (result.isPresent()){
                Notification notif = result.get();
                notif.setUser(notification.getUser());
                notif.setMessage(notification.getMessage());
                notif.setStatus(notification.getStatus());
                notif.setModificationDate(new Date());

                notification = notif;
            }
        }

        return notificationRepository.save(notification);
    }


    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Search notification by name or firstname in DB
     * @param keyword
     * @return
     */
    public List<Notification> search(String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Notification> criteriaQuery = criteriaBuilder.createQuery(Notification.class);

        Root<Notification> root = criteriaQuery.from(Notification.class);
        String criteriaStr = "%" + keyword.toUpperCase() + "%";

        Predicate predicate = criteriaBuilder.or(criteriaBuilder
                        .like(criteriaBuilder.upper(root.get("name")), criteriaStr),
                criteriaBuilder.like(criteriaBuilder.upper(root.get("firstname")), criteriaStr));
        criteriaQuery.where(predicate);

        TypedQuery<Notification> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

}
