package com.overnightApps.myapplication.app.core.helper;

public class SignUpForm {
    private final String fullName;
    private final String email;
    private final String password;
    private final String publicSignature;
    private final String privateSignature;

    public SignUpForm(String fullName, String email, String password,
                      String publicSignature, String privateSignature) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.publicSignature = publicSignature;
        this.privateSignature = privateSignature;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPublicSignature() {
        return publicSignature;
    }

    public String getPrivateSignature() {
        return privateSignature;
    }
}
