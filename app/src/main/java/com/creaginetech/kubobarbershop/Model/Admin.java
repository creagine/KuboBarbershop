package com.creaginetech.kubobarbershop.Model;

public class Admin {

    private String emailAdmin;
    private String passwordAdmin;
    private String roleAdmin;

    public Admin() {
    }

    public Admin(String emailAdmin, String passwordAdmin, String roleAdmin) {
        this.emailAdmin = emailAdmin;
        this.passwordAdmin = passwordAdmin;
        this.roleAdmin = roleAdmin;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }

    public String getRoleAdmin() {
        return roleAdmin;
    }

    public void setRoleAdmin(String roleAdmin) {
        this.roleAdmin = roleAdmin;
    }
}
