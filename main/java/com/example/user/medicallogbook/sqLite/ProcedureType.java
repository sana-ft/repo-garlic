package com.example.user.medicallogbook.sqLite;

/**
 * Created by user on 12/9/2015.
 */
public class ProcedureType {

    int id;
    String type;

    public ProcedureType(String type) {
        this.type = type;
    }

    public ProcedureType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public ProcedureType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String toString(){
        return this.type;
    }
}
