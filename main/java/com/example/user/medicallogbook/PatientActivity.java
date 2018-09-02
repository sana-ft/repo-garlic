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

import com.example.user.medicallogbook.sqLite.DatabaseHandler;
import com.example.user.medicallogbook.sqLite.Patient;
import com.example.user.medicallogbook.sqLite.Speciality;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private DatabaseHandler db;
    private String genderFM,bloodGroup;
    private int idDoctor;
    private Date dob;
    private int mYear, mMonth, mDay;
    private EditText datePick;
    static final int DATE_DIALOG_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        setTitle("Patients");

        datePick=(EditText)findViewById(R.id.patientdate);
        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        idDoctor = (int) b.get("idDoctor");


        // display the current date
        updateDisplay();
        datePick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        //initialize gender spinner
        String[] gender=new String[]{"Male","Female"};
        String[] bld_grp=new String[]{"A+","A-","AB+","AB-","B+","B-","O+","O-"};
        Spinner sgender=(Spinner)findViewById(R.id.genderSpinner);
        Spinner sBld_grp=(Spinner)findViewById(R.id.bloodGroup);

        ArrayAdapter<String> genderArray= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gender);
        genderArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sgender.setAdapter(genderArray);
        sgender.setOnItemSelectedListener(this);
        ArrayAdapter<String> bloodArray= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,bld_grp);
        bloodArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sBld_grp.setAdapter(bloodArray);
        sBld_grp.setOnItemSelectedListener(this);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

        switch(parent.getId()) {
            case R.id.genderSpinner:
                genderFM = (String) parent.getItemAtPosition(position);
                break;
            case R.id.bloodGroup:
                bloodGroup = (String) parent.getItemAtPosition(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // checking if the email string is valid in the EditText
    public boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void addPatient(View view){

        db=DatabaseHandler.getInstance();
        String fname=((EditText)findViewById(R.id.fnameP)).getText().toString();
        String lname=((EditText)findViewById(R.id.lnameP)).getText().toString();
        String email=((EditText)findViewById(R.id.emailP)).getText().toString();

        dob = new Date(mYear,mMonth,mDay);

        if(!fname.isEmpty() && !lname.isEmpty() && !email.isEmpty()){

            if(isEmailValid(email)){
                Log.d("adding patient", "adding");
                //Log.d("gender",genderFM);
                Patient patient=new Patient(fname,lname,email,bloodGroup,genderFM,dob,idDoctor);

                db.addPatient(patient);
                Toast.makeText(getApplicationContext(), "New patient Added", Toast.LENGTH_LONG).show();
                Log.d("Patient added","added");
                Intent i=new Intent(this,LogsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("idDoctor",idDoctor);
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "Email form is not valid!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_LONG).show();
        }
    }
}
