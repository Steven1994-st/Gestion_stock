package com.gestion.service;

import com.gestion.model.Notification;
import com.gestion.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service()
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    /**
     * Get Notification Repository
     * @return repository
     */
    public NotificationRepository getRepository(){
        return notificationRepository;
    }

    /**
     * Save or update notification
     * @param notification
     * @return notification saved or updated
     */
    public Notification saveNotification(Notification notification){
        if (notification.getId()!=null){
            Optional<Notification> result = getRepository().findById(notification.getId());
            if (result.isPresent()){
                Notification not = result.get();
                not.setUser(notification.getUser());
                not.setMessage(notification.getMessage());
                not.setStatus(notification.getStatus());
                not.setModificationDate(notification.getModificationDate());

                notification = not;
            }
        }

        return notificationRepository.save(notification);
    }

}
