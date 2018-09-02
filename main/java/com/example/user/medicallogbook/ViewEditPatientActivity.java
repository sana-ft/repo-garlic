package com.example.user.medicallogbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.medicallogbook.sqLite.Cases;
import com.example.user.medicallogbook.sqLite.DatabaseHandler;
import com.example.user.medicallogbook.sqLite.Patient;
import com.example.user.medicallogbook.sqLite.Procedure;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewEditPatientActivity extends AppCompatActivity {

    private int idDoctor, idPatient;
    private DatabaseHandler db;
    private EditText name, lname, email;
    private TextView ddn,bldgroup,gender;
    private Patient patientToEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_patient);
        setTitle("Patient's Details");

        db=DatabaseHandler.getInstance();

        final ArrayAdapter<Patient> arrayProcedure;
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        idDoctor = (int) b.get("idDoctor");
        idPatient = (int) b.get("idPatient");


        name=(EditText)findViewById(R.id.frstname);
        lname=(EditText)findViewById(R.id.lstname);
        email=(EditText)findViewById(R.id.emailpatient);
        ddn=(TextView)findViewById(R.id.ddn);
        bldgroup=(TextView)findViewById(R.id.bloodgr);
        gender=(TextView)findViewById(R.id.gender);
        try {
            //fill the patient's details into the activity
            patientToEdit=db.getPatient(idPatient);
            name.setText(patientToEdit.getFname());
            lname.setText(patientToEdit.getLname());
            email.setText(patientToEdit.getEmail());
            int yearDate = patientToEdit.getDob().getYear();
            int monthDate = patientToEdit.getDob().getMonth();
            int dayDate =patientToEdit.getDob().getDay();
            String age = ""+getAge(yearDate,monthDate,dayDate);
            ddn.setText("Age \n"+age);
            bldgroup.setText("Blood Group \n"+patientToEdit.getBloodGroup());
            gender.setText("Gender \n"+patientToEdit.getGender().toUpperCase());
            //fill the lsitview with procedures for each patient
            ArrayList<Cases>caseList=(ArrayList<Cases>)db.getCaseListIdPatient(idPatient);
            ArrayList<Procedure> procedureList = new ArrayList<Procedure>();
            for(int i=0;i<caseList.size();i++){
                int idpr=caseList.get(i).getIdProcedure();
                Procedure pc=db.getProcedure(idpr);
                procedureList.add(pc);
            }
            ListView procedurelv=(ListView)findViewById(R.id.procedurelv);
            final ArrayAdapter<Procedure> arrayProc;
            final Context context = this;
            arrayProc=new ArrayAdapter<Procedure>(context,android.R.layout.simple_list_item_1,procedureList);

            procedurelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Procedure p=(Procedure)parent.getItemAtPosition(position);
                        int idProcToEdit = p.getId();
                        Intent i=new Intent(context,ViewEditLogActivity.class);
                        i.putExtra("idProcedure",idProcToEdit);
                        i.putExtra("idDoctor", idDoctor);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                }
            });
            procedurelv.setAdapter(arrayProc);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    // update patient
    public void updatePatient(View view){

        if(isEmailValid(email.getText().toString()) && !lname.getText().toString().isEmpty() && !name.getText().toString().isEmpty()){
            patientToEdit.setFname(name.getText().toString());
            patientToEdit.setLname(lname.getText().toString());
            patientToEdit.setEmail(email.getText().toString());
            db.updatePatient(patientToEdit);
            Toast.makeText(getApplicationContext(), "Patient Updated", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, SearchPatientActivity.class);
            i.putExtra("idDoctor", idDoctor);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }else{
            Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_LONG).show();
        }

    }

    //calculate age
    public int getAge (int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }
    // checking if the email string is valid in the EditText
    public static boolean isEmailValid(String email) {
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
