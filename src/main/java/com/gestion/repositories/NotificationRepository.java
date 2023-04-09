package com.gestion.repositories;

import com.gestion.models.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification,Long> {

    /** Find notifications related to an user in the database
     * by using the User ID
     *
     * @param userId the User ID
     * @return the list of notifications found or an empty list
     */
    @Query("SELECT r FROM Notification r where r.user.id=:p")
    List<Notification> findNotificationsByUser(@Param("p") Long userId);

    /** Find notifications related to an User in the database
     * by using the User ID and its status
     *
     * @param userId the User ID
     * @param notification_status the status
     * @return the list of notifications found or an empty list
     */
    @Query("SELECT r FROM Notification r where r.user.id=:i and r.status=:s")
    List<Notification> findNotificationsByStatusAndUser(@Param("i") Long userId, @Param("s")Notification.NOTIFICATION_STATUS notification_status);
}
