package com.example.user.medicallogbook.sqLite;

/**
 * Created by user on 12/9/2015.
 */
public class Perform {

    int id, idDoctor, idProcedure;

    public Perform() {
    }

    public Perform(int idDoctor, int idProcedure) {
        this.idDoctor = idDoctor;
        this.idProcedure = idProcedure;
    }

    public Perform(int id, int idDoctor ,int idProcedure) {
        this.id = id;
        this.idDoctor = idDoctor;
        this.idProcedure = idProcedure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public int getIdProcedure() {
        return idProcedure;
    }

    public void setIdProcedure(int idProcedure) {
        this.idProcedure = idProcedure;
    }
}
