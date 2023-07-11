package com.gestion.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="HOLIDAY")
public class Holiday extends Persistent{

    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @Column(name = "end_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @Column(name = "comment")
    private String comment;

    @Column(name = "status", nullable = false)
    private HOLIDAY_STATUS status;

    @ManyToOne()
    @JoinColumn(name = "user")
    private User user;


    public Holiday() {
    }

    public Holiday(Date startDate, Date endDate, String comment, HOLIDAY_STATUS status, User user) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.comment = comment;
        this.status = status;
        this.user = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HOLIDAY_STATUS getStatus() {
        return status;
    }

    public void setStatus(HOLIDAY_STATUS status) {
        this.status = status;
    }

    /**
     * Represent the status of a Holiday
     */
    public enum HOLIDAY_STATUS{
        PROCESSING,
        ACCEPT,
        REFUSE
    }
}
