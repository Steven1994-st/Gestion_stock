package com.gestion.service;

import com.gestion.model.Holiday;
import com.gestion.repository.HolidayRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service()
public class HolidayService {

    @Autowired
    HolidayRepository holidayRepository;

    @Autowired
    NotificationService notificationService;

    public HolidayRepository getRepository(){
        return holidayRepository;
    }

    /**
     * Save a Holiday
     * @param holiday
     * @return
     */
    @Transactional()
    public Holiday saveHoliday(Holiday holiday){

        holiday.setStatus(Holiday.HOLIDAY_STATUS.PROCESSING);
        holiday.setCreationDate(new Date());
        String message = "Le congé \"" +holiday.getComment()+ "\" a été ajouté pour l'utilisateur:" + " "
                + holiday.getUser().getFirstname() + " " + holiday.getUser().getName();

        notificationService.sendNotificationToAdmins(message);

        return getRepository().save(holiday);
    }

    /**
     * Update a Holiday
     * @param holiday
     * @return
     */
    @Transactional()
    public Holiday updateHoliday(Holiday holiday){

        Holiday holidayFound = getRepository()
                .findById(holiday.getId()).get();

        holidayFound.setStartDate(holiday.getStartDate());
        holidayFound.setEndDate(holiday.getEndDate());
        holidayFound.setComment(holiday.getComment());
        holidayFound.setUser(holiday.getUser());
        holidayFound.setStatus(holiday.getStatus());

        holidayFound.setModificationDate(new Date());

        String message = "Le congé \""+holiday.getComment()+ "\" a été modifié pour l'utilisateur: " +
                holiday.getUser().getFirstname() + " " + holiday.getUser().getName();

        notificationService.sendNotificationToAdmins(message);

        return getRepository().save(holidayFound);
    }

    /**
     * Notifier l'employé de la modification du congé
     * @param holiday
     * @return
     */
    public void sendNotificationToEmployee(Holiday holiday){
        String status;
        if (holiday.getStatus().equals(Holiday.HOLIDAY_STATUS.PROCESSING)){
            status = "est en traitement";
        } else if (holiday.getStatus().equals(Holiday.HOLIDAY_STATUS.ACCEPT)) {
            status = "a été accepté";
        } else {
            status = "a été refusé";
        }

        String message = "Le congé \"" +holiday.getComment()+
                "\" " +status+ " par un administrateur";

        notificationService.sendNotification(message, holiday.getUser());
    }



    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Search name or firstname by keyword in DB
     * @param keyword
     * @return
     */
    public List<Holiday> search(String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Holiday> criteriaQuery = criteriaBuilder.createQuery(Holiday.class);

        Root<Holiday> root = criteriaQuery.from(Holiday.class);
        String criteriaStr = "%" + keyword.toUpperCase() + "%";

        Predicate predicate = criteriaBuilder.or(criteriaBuilder
                        .like(criteriaBuilder.upper(root.get("comment")), criteriaStr),
                criteriaBuilder.like(criteriaBuilder.upper(root.get("user").get("name")), criteriaStr),
                criteriaBuilder.like(criteriaBuilder.upper(root.get("user").get("firstname")), criteriaStr)
                );
        criteriaQuery.where(predicate);

        TypedQuery<Holiday> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
