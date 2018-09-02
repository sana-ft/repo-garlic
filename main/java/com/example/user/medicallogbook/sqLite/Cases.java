package com.example.user.medicallogbook.sqLite;

/**
 * Created by user on 12/9/2015.
 */
public class Cases {
    int id, idpatient, idProcedure;

    public Cases(int id, int idProcedure, int idpatient) {
        this.id = id;
        this.idProcedure = idProcedure;
        this.idpatient = idpatient;
    }

    public Cases(int idProcedure, int idpatient) {
        this.idProcedure = idProcedure;
        this.idpatient = idpatient;
    }

    public Cases() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProcedure() {
        return idProcedure;
    }

    public void setIdProcedure(int idProcedure) {
        this.idProcedure = idProcedure;
    }

    public int getIdpatient() {
        return idpatient;
    }

    public void setIdpatient(int idpatient) {
        this.idpatient = idpatient;
    }
}

