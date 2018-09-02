package com.example.user.medicallogbook;

import android.content.Intent;
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

import com.example.user.medicallogbook.sqLite.DatabaseHandler;
import com.example.user.medicallogbook.sqLite.Doctor;
import com.example.user.medicallogbook.sqLite.Speciality;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseHandler db;
    private Speciality speciality;
    private int idSpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView loginScreen = (TextView) findViewById(R.id.link_login);
        // Listening to register new account link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Sign up screen
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        db = DatabaseHandler.getInstance();
        // Doctor Speciality spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSpeciality);
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<Speciality> speciality=db.getAllSpecialities();
        ArrayAdapter<Speciality> adapter =new ArrayAdapter<Speciality>(this,android.R.layout.simple_spinner_item,speciality);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View vi8w,int pos, long id) {
        speciality=(Speciality)parent.getItemAtPosition(pos);
        idSpec=speciality.getId();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void register(View view) {

        int idDoctor;
        db = DatabaseHandler.getInstance();

        String email =( (EditText) findViewById(R.id.email)).getText().toString();
        String pass = ((EditText) findViewById(R.id.password)).getText().toString();
        String passConf = ((EditText) findViewById(R.id.confirmPass)).getText().toString();
        String fname = ((EditText) findViewById(R.id.fname)).getText().toString();
        String lname =( (EditText) findViewById(R.id.lname)).getText().toString();
        int spec=idSpec;

        if(fname.isEmpty() || lname.isEmpty() || pass.isEmpty()){
            Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_LONG).show();
        }else {
            if (isEmailValid(email) == false) {
                Toast.makeText(getApplicationContext(), "Email form is not valid!", Toast.LENGTH_LONG).show();
            } else if (db.getDoctor(email) != null) {
                Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_LONG).show();
            } else if (!pass.equals(passConf)) {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            } else {

                //creating a new doctor object
                Doctor doc = new Doctor(fname, lname, email, pass, spec);
                // adding the doctor to the database
                db.addDoctor(doc);
                Log.d("add doctor: ", "adding");

                Toast.makeText(getApplicationContext(), "You've been Registered!", Toast.LENGTH_LONG).show();

                // get doctor in order to get his id and send it to the next activity
                Doctor d = db.getDoctor(email);
                Log.d("getting doc", "docc");
                int ids = d.getSpeciality();
                Speciality sdoc = db.getSpeciality(ids);
                Log.d("doc's speciality", "" + sdoc.getSeciality());
                idDoctor = d.getId();
                Log.d("getting id, ", "" + idDoctor);

                Intent intent = new Intent(this, LogsActivity.class);
                intent.putExtra("idDoctor", idDoctor);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }

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


}
