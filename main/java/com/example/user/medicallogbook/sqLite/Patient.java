package com.example.user.medicallogbook.sqLite;

import java.util.Date;

/**
 * Created by user on 12/9/2015.
 */
public class Patient {
    int id, idDoc;
    String fname, lname, email,bloodGroup, gender;
    Date dob;

    public Patient(int id, String fname, String lname, String email, String bloodGroup, String gender, Date dob,int idDoc) {
        this.fname = fname;
        this.lname = lname;
        this.id = id;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
        this.dob = dob;
        this.idDoc=idDoc;
    }

    public Patient(String fname, String lname, String email, String bloodGroup, String gender, Date dob,int idDoc) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
        this.dob = dob;
        this.idDoc=idDoc;
    }

    public Patient() {
    }

    public int getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(int idDoc) {
        this.idDoc = idDoc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String toString(){
        return fname+" "+lname;
    }
}
