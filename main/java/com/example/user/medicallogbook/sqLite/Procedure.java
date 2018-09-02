package com.example.user.medicallogbook.sqLite;

import java.util.Date;

/**
 * Created by user on 12/9/2015.
 */
public class Procedure {

    int id, idRole, idHospital, idProcedureType;
    String name;
    Date date;

    public Procedure(int id,  String name,Date date, int idProcedureType, int idHospital, int idRole) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.idProcedureType = idProcedureType;
        this.idHospital = idHospital;
        this.idRole = idRole;
    }

    public Procedure(String name, Date date, int idProcedureType, int idHospital, int idRole) {
        this.name = name;
        this.date = date;
        this.idProcedureType = idProcedureType;
        this.idHospital = idHospital;
        this.idRole = idRole;
    }

    public Procedure() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdProcedureType() {
        return idProcedureType;
    }

    public void setIdProcedureType(int idProcedureType) {
        this.idProcedureType = idProcedureType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(int idHospital) {
        this.idHospital = idHospital;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String toString(){
        return this.name;
    }
}
