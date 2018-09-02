package com.example.user.medicallogbook.sqLite;

/**
 * Created by user on 12/20/2015.
 */
public class Speciality {
    private int id;
    private String seciality;

    public Speciality(int id, String seciality) {
        this.id = id;
        this.seciality = seciality;
    }

    public Speciality(String seciality) {
        this.seciality = seciality;
    }

    public Speciality() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeciality() {
        return seciality;
    }

    public void setSeciality(String seciality) {
        this.seciality = seciality;
    }

    public String toString(){
        return this.seciality;
    }
}
