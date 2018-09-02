package com.example.user.medicallogbook;

/**
 * Created by user on 12/23/2015.
 */
public class LogItems {
    private String procedur_name, proc_date, patient_name;

    public LogItems(String patient_name, String proc_date, String procedur_name) {
        this.patient_name = patient_name;
        this.proc_date = proc_date;
        this.procedur_name = procedur_name;
    }

    public LogItems() {
    }

    public String getProc_date() {
        return proc_date;
    }

    public void setProc_date(String proc_date) {
        this.proc_date = proc_date;
    }

    public String getProcedur_name() {
        return procedur_name;
    }

    public void setProcedur_name(String procedur_name) {
        this.procedur_name = procedur_name;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }
}
