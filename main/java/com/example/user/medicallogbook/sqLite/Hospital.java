package com.example.user.medicallogbook.sqLite;

/**
 * Created by user on 12/9/2015.
 */
public class Hospital {
    int id;
    String name;

    public Hospital(String name) {
        this.name = name;
    }

    public Hospital(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Hospital() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String toString(){
        return this.name;
    }
}
