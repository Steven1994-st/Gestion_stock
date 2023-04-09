package com.gestion.models;

import jakarta.persistence.*;


@Entity
@Table(name="NOTIFICATION")
public class Notification extends Persistent{

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "status", nullable=false)
    private NOTIFICATION_STATUS status;

    @ManyToOne()
    @JoinColumn(name = "user")
    private User user;

    public Notification() {
    }

    public Notification(String message, NOTIFICATION_STATUS status, User user) {
        this.message = message;
        this.status = status;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NOTIFICATION_STATUS getStatus() {
        return status;
    }

    public void setStatus(NOTIFICATION_STATUS status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Represent the status of a notification
     */
    public enum NOTIFICATION_STATUS{
        READ,
        UNREAD,
    }
}
