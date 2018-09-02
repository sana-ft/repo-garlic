package com.example.user.medicallogbook.sqLite;

/**
 * Created by user on 12/9/2015.
 */
public class DoctorRole {
    int id;
    String role;

    public DoctorRole(String role) {
        this.role = role;
    }

    public DoctorRole(int id, String role) {
        this.id = id;
        this.role = role;
    }

    public DoctorRole() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String toString(){
        return this.role;
    }
}
