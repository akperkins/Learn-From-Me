package com.overnightApps.myapplication.app.core;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by andre on 3/24/14.
 */
public class Comment implements Serializable {
    private final String message;
    private final User author;
    private final long creationTime;

    public Comment(String message, User author, long creationTime) {
        this.message = message;
        this.author = author;
        this.creationTime = creationTime;
    }

    public Comment(String message, User author) {
       this(message,author,new Date().getTime());
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }

    public User getAuthor() {
        return author;
    }

    public long getCreationTime() {
        return creationTime;
    }
}
