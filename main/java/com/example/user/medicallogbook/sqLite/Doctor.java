package com.example.user.medicallogbook.sqLite;

/**
 * Created by user on 12/9/2015.
 */
public class Doctor {

    int id, idSpeciality;
    String fname, lname, email, password;

    public Doctor( int id,String fname, String lname, String email, String password, int idSpeciality) {

        this.id=id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.idSpeciality = idSpeciality;
    }

    public Doctor( String fname, String lname, String email, String password, int idSpeciality) {

        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.idSpeciality = idSpeciality;
    }


    public Doctor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSpeciality() {
        return idSpeciality;
    }

    public void setSpeciality(int idSpeciality) {
        this.idSpeciality = idSpeciality;
    }
}
