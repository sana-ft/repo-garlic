package com.example.user.medicallogbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.medicallogbook.sqLite.Cases;
import com.example.user.medicallogbook.sqLite.DatabaseHandler;
import com.example.user.medicallogbook.sqLite.Doctor;
import com.example.user.medicallogbook.sqLite.Patient;
import com.example.user.medicallogbook.sqLite.Perform;
import com.example.user.medicallogbook.sqLite.Procedure;
import com.example.user.medicallogbook.sqLite.Speciality;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int idDoctor,idSpec;
    private DatabaseHandler db;
    private Doctor docToEditDel;
    private EditText name,lname,email,pass1,pass2;
    private Speciality speciality;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Your Profile");

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        idDoctor = (int) b.get("idDoctor");

        db=DatabaseHandler.getInstance();
        Log.d("doc count",""+db.getDoctorsCount());
        //get doctor to editor delete
       docToEditDel=db.getDoctorbyID(idDoctor);


        name=(EditText)findViewById(R.id.nameDoc);
        lname=(EditText)findViewById(R.id.lnameDoc);
        email=(EditText)findViewById(R.id.emailDoc);
        pass1=(EditText)findViewById(R.id.passwordchange);
        pass2=(EditText)findViewById(R.id.confirmPasschange);
        TextView emailView=(TextView)findViewById(R.id.docemailview);
        int specialityid=docToEditDel.getSpeciality();
        String speciaD=db.getSpeciality(specialityid).getSeciality();
        TextView specView=(TextView)findViewById(R.id.specDoc);

        //populate editTexts with doctor's infos
        name.setText(docToEditDel.getFname());
        lname.setText(docToEditDel.getLname());
        emailView.setText("Current email: "+docToEditDel.getEmail());
        pass1.setText(docToEditDel.getPassword());
        pass2.setText(docToEditDel.getPassword());
        specView.setText("Current speciality: "+speciaD);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSpecChange);
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<Speciality> speciality=db.getAllSpecialities();
        ArrayAdapter<Speciality> adapter =new ArrayAdapter<Speciality>(this,android.R.layout.simple_spinner_item,speciality);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }

    //method to update the profile
    public void updateProfile(View view){
        String names=name.getText().toString();
        String lnames=lname.getText().toString();
        String emails=email.getText().toString();
        String pass1s=pass1.getText().toString();
        String pass2s=pass2.getText().toString();

        //check if fields are empty
        if(!names.isEmpty() || !lnames.isEmpty() || !pass1s.isEmpty() || !pass2s.isEmpty()){

                    //check id passwords match
                    if(pass1s.equals(pass2s)){
                        //update doctor
                        //if the doctor changes his email
                        if(!emails.isEmpty())
                        {
                            //check if email form is valid
                            if(isEmailValid(emails)) {
                                //check if email exists
                                if(db.getDoctor(emails)==null){
                                docToEditDel.setEmail(emails);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_LONG).show();
                                    return;
                                }

                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Email form is not valid!", Toast.LENGTH_LONG).show();
                            }
                        }
                        if(idSpec!=0){
                            docToEditDel.setSpeciality(idSpec);
                        }
                        docToEditDel.setFname(names);
                        docToEditDel.setLname(lnames);
                        docToEditDel.setPassword(pass1s);
                        db.updateDoctor(docToEditDel);
                        Toast.makeText(getApplicationContext(), "You profile has been updated", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(this,LogsActivity.class);
                        intent.putExtra("idDoctor", idDoctor);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }else{
                        Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
                    }


        }else{
            Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_LONG).show();
        }
    }

    //method to log out and go the login page
    public void logOut(View view){

        SharedPreferences preferences = getSharedPreferences("Login Credentials", 0);
        preferences.edit().remove("Username").commit();
        preferences.edit().remove("Password").commit();
        Intent i=new Intent(this,LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    //methid to delete an account
    public void deleteAccount(View view){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final Context context = this;

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete your account? (caution: Data cannot be restored!)");

        // Setting Positive "Yes" Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db = DatabaseHandler.getInstance();
                        //removing SharedPreferences
                        SharedPreferences preferences = getSharedPreferences("Login Credentials", 0);
                        preferences.edit().remove("Username").commit();
                        preferences.edit().remove("Password").commit();
                        try {
                            //delete all data related to doctor (patients, procedures,...)
                            ArrayList<Patient> patientList = (ArrayList<Patient>) db.getAllPatientsByDoc(idDoctor);
                            for (int i = 0; i < patientList.size(); i++) {
                                //delete patient
                                db.deletePatient(patientList.get(i));
                                Log.d("deleting Patient ", "patient deleted");
                                ArrayList<Cases> caseList = (ArrayList<Cases>) db.getCaseListIdPatient(patientList.get(i).getId());
                                for (int j = 0; j < caseList.size(); j++) {
                                    //delete cases related to this doctor's patients
                                    db.deleteCase(caseList.get(j));
                                    Log.d("deleteing case", "case deleted");
                                    int idpocd = caseList.get(j).getIdProcedure();
                                    //deleting perform rows
                                    Perform performTodel = db.getPerformProcDoc(idDoctor, idpocd);
                                    db.deletePerform(performTodel);
                                    Log.d("deleteing perform", "perform deleted");
                                    //deleting procedure
                                    Procedure procedureToDel = db.getProcedure(idpocd);
                                    db.deleteProcedure(procedureToDel);
                                    Log.d("deleteing procedure", "procedure deleted");
                                }
                            }

                            //deleting doctor
                            db.deleteDoctor(docToEditDel);
                            Log.d("deleting doctor", "doctor deleted");
                            Intent i = new Intent(context, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                            Log.d("patients count", "" + db.getPatientsCount());
                            Log.d("cases count", "" + db.getCaseCount());
                            Log.d("proc count",""+db.getProceduresCount());
                            Log.d("perform count",""+db.getPerformCount());



                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });

        // Setting Negative "Cancel" Button
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        speciality=(Speciality)parent.getItemAtPosition(position);
        idSpec=speciality.getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
