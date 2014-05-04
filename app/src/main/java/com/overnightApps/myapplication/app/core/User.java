package com.overnightApps.myapplication.app.core;

import java.io.Serializable;

/**
 * Created by andre on 3/21/14.
 */
public class User implements Serializable {
    private String email;
    private String fullName;
    private String createdAt;
    private int experience;
    private String publicSignature;
    private String privateSignature;

    public User(String email, String fullName, String createdAt, int experience, String publicSignature,
                String privateSignature) {
        this.email = email;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.experience = experience;
        this.publicSignature = publicSignature;
        this.privateSignature = privateSignature;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPrivateSignature() {
        return privateSignature;
    }

    public void setPrivateSignature(String privateSignature) {
        this.privateSignature = privateSignature;
    }

    public String getPublicSignature() {
        return publicSignature;
    }

    public void setPublicSignature(String publicSignature) {
        this.publicSignature = publicSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || User.class != o.getClass()) return false;

        User user = (User) o;

        return email.equals(user.email);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    public void addAmountToExperience(int newExperience) {
        experience += newExperience;
    }
}
