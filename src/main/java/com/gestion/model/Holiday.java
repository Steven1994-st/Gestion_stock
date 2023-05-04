package com.gestion.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="HOLIDAY")
public class Holiday extends Persistent{

    @Column(name = "start_date", nullable = false)
    private Date start_date;
    @Column(name = "end_date", nullable = false)
    private Date end_date;
    @Column(name = "comment")
    private String comment;

    @Column(name = "status", nullable = false)
    private HOLIDAY_STATUS status;

    @ManyToOne()
    @JoinColumn(name = "user")
    private User user;


    public Holiday() {
    }

    public Holiday(Date start_date, Date end_date, String comment, HOLIDAY_STATUS status, User user) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.comment = comment;
        this.status = status;
        this.user = user;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
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
