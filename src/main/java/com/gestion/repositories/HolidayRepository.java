package com.gestion.repositories;

import com.gestion.models.Holiday;
import com.gestion.models.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HolidayRepository extends CrudRepository<Holiday,Long> {
    /** Find Holiday related to an user in the database
     * by using the User ID
     *
     * @param userId the User ID
     * @return the list of holiday found or an empty list
     */
    @Query("SELECT r FROM Holiday r where r.user.id=:p")
    List<Holiday> findHolidaysByUser(@Param("p") Long userId);

    /** Find Holidays related to an User in the database
     * by using the User ID and its status
     *
     * @param userId the User ID
     * @param holiday_status the status
     * @return the list of holidays found or an empty list
     */
    @Query("SELECT r FROM Holiday r where r.user.id=:i and r.status=:s")
    List<Holiday> findHolidaysByStatusAndUser(@Param("i") Long userId, @Param("s")Holiday.HOLIDAY_STATUS holiday_status);
}
