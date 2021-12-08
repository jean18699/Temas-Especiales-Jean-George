package com.pucmm.proyectofinal.roomviewmodel.model;

import java.util.concurrent.atomic.AtomicLong;

public class Notification {

    private static final AtomicLong count = new AtomicLong(0);
    private final long id;
    private String message;

    public Notification(){
        this.id = count.incrementAndGet();
    }

    public Notification(String message) {
        id = count.incrementAndGet();
        this.message = message;

    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
