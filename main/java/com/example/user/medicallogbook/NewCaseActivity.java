package com.example.user.medicallogbook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.medicallogbook.sqLite.Cases;
import com.example.user.medicallogbook.sqLite.DatabaseHandler;
import com.example.user.medicallogbook.sqLite.DoctorRole;
import com.example.user.medicallogbook.sqLite.Hospital;
import com.example.user.medicallogbook.sqLite.Patient;
import com.example.user.medicallogbook.sqLite.Perform;
import com.example.user.medicallogbook.sqLite.Procedure;
import com.example.user.medicallogbook.sqLite.ProcedureType;
import com.example.user.medicallogbook.sqLite.Speciality;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewCaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseHandler db;
    private int idRole,idType,idHospital, idDoctor,idPatient;
    private String pName;
    private Date dop;
    private int mYear, mMonth, mDay;
    private EditText datePick;
    static final int DATE_DIALOG_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case);
        setTitle("New Case");

        db=DatabaseHandler.getInstance();

        datePick=(EditText)findViewById(R.id.procdate);
        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        //getting extras
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        //checking if intent has the extras
        if(intent.hasExtra("idDoctor")){
            idDoctor = (int) b.get("idDoctor");
            Log.d("idDoctor","dsasadasd");
        }

        if(intent.hasExtra("idPatient")){
            idPatient=(int)b.get("idPatient");
            Log.d("patient's id case",""+idPatient);
        }




        try {
            Patient p=db.getPatient(idPatient);
            if(p!=null)
            {
                String namePt=p.getFname()+" "+p.getLname();
                EditText nameP= (EditText) findViewById(R.id.namePatient);
                nameP.setText(""+namePt);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // display the current date
        updateDisplay();
        datePick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        //load spinners into activity
        loadSpinnerRoles(db);
        loadSpinnerTypes(db);
        loadSpinnerHospitals(db);


    }

    private void updateDisplay() {
        this.datePick.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    // insert the new case into database
    public boolean insertintoDB(DatabaseHandler db) throws ParseException {

        //get the procedure's name
        pName=((EditText)findViewById(R.id.nameP)).getText().toString();
        //check if the edittext is empty
        if(!pName.isEmpty()){

            Procedure prtest=db.getProcedurebyName(pName);
             //check if the name of procedure already exists because the name must be unique
            //if null, proceed with inserting the data
            if(prtest==null) {
                    if(addLog(db)==true)
                        return true;
                    else
                        return false;
            }
            //if not, see if the same procedure(name) exists in the same doctor's database
            else {
                Perform ptest= db.getPerformProcDoc(idDoctor, prtest.getId());
                //if not, insert into database
                if(ptest==null) {
                 if(addLog(db)==true)
                    return true;
                    else
                     return false;
                }
                //if the procedure with the same name exists => warn user
                else{
                Toast.makeText(getApplicationContext(), "Procedure's name must be unique!", Toast.LENGTH_LONG).show();
                return false;
                }
            }

        }
        //if not, display a toast that warns the user that the EditText is empty
        else
            Toast.makeText(getApplicationContext(), "Procedure's name is Empty!", Toast.LENGTH_LONG).show();
            return false;
    }

    // Load All three spinners with data from SQLite

    public void loadSpinnerRoles(DatabaseHandler db){
        Spinner spinner = (Spinner) findViewById(R.id.roleSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<DoctorRole>roles =db.getAllDoctorsRole();
        ArrayAdapter<DoctorRole> adapter =new ArrayAdapter<DoctorRole>(this,android.R.layout.simple_spinner_item,roles);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }
    public void loadSpinnerTypes(DatabaseHandler db){
        Spinner spinner = (Spinner) findViewById(R.id.typeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<ProcedureType> types =db.getAllProcedureTypes();
        ArrayAdapter<ProcedureType> adapter =new ArrayAdapter<ProcedureType>(this,android.R.layout.simple_spinner_item,types);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void loadSpinnerHospitals(DatabaseHandler db){
        Spinner spinner = (Spinner) findViewById(R.id.hospitalSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<Hospital> hospital =db.getAllHospitals();
        ArrayAdapter<Hospital> adapter =new ArrayAdapter<Hospital>(this,android.R.layout.simple_spinner_item,hospital);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    // get the ids of the selected items
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(parent.getId()){
            case R.id.roleSpinner:
                DoctorRole roleDoctor= (DoctorRole)parent.getItemAtPosition(position);
                idRole=roleDoctor.getId();
                break;
            case R.id.typeSpinner:
                ProcedureType procType=(ProcedureType)parent.getItemAtPosition(position);
                idType=procType.getId();
                break;
            case R.id.hospitalSpinner:
                Hospital hos=(Hospital)parent.getItemAtPosition(position);
                idHospital=hos.getId();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // add log method
    public boolean addLog(DatabaseHandler db) throws ParseException {
        Log.d("null", " no procedure with such name");
        dop = new Date(mYear,mMonth,mDay);
        // if the user did not choose a patient, warn him
        if(idPatient==0)
        {
            Toast.makeText(getApplicationContext(), "Please choose a patient", Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            //create the procedure
            Procedure procedure = new Procedure(pName, dop,idType, idHospital, idRole);
            Log.d("procedure added, ", "adding");
            Log.d("idRole, ", ""+idRole);
            Log.d("idRoleP, ", ""+procedure.getIdRole());
            //insert procedure
            db.addProcedure(procedure);
            Procedure p = db.getProcedurebyName(pName);
            Log.d("NameProced, ", "" + p.getName());
            Log.d("ROleeeeeeee, ", "" + p.getIdRole());
            //create the perform
            Perform perform = new Perform(idDoctor, p.getId());
            Log.d("id proceeeeed",""+p.getId());
            //insert perform
            db.addPerform(perform);
            //create the case
            Log.d("id patient new case",""+idPatient);
            Cases cases = new Cases(p.getId(), idPatient);
            //insert the case
            db.addCase(cases);
            Log.d("adding case: ", "added");
            Log.d("Id Proce: ", "" + cases.getIdProcedure());
            Toast.makeText(getApplicationContext(), "Case Added", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    // add the new case to database
    public void addCase(View view) throws ParseException {
        db=DatabaseHandler.getInstance();
        boolean added=insertintoDB(db);
        if(added==true)
        {
            Intent i =new Intent(this, LogsActivity.class);
            i.putExtra("idDoctor",idDoctor);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    //go to search a patient
    public void goPatientActivity(View view){

        Intent i=new Intent(this,SearchPatientActivity.class);
        i.putExtra("idDoctor",idDoctor);
        i.putExtra("idPatient", idPatient);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

}

