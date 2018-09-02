package com.example.user.medicallogbook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.medicallogbook.sqLite.DatabaseHandler;
import com.example.user.medicallogbook.sqLite.DoctorRole;
import com.example.user.medicallogbook.sqLite.Hospital;
import com.example.user.medicallogbook.sqLite.Procedure;
import com.example.user.medicallogbook.sqLite.ProcedureType;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class ViewEditLogActivity extends AppCompatActivity {

    private int idProcedure,idDoctor;
    private DatabaseHandler db;
    private EditText nameP;
    private TextView datep,roled,hospp,typep;
    private Date updateD;
    private int mYear, mMonth, mDay;
    private EditText datePick;
    static final int DATE_DIALOG_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_log);
        setTitle("Procedure's Details");

        db=DatabaseHandler.getInstance();
        datePick=(EditText)findViewById(R.id.logdate);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        idDoctor=(int)b.get("idDoctor");
        idProcedure = (int) b.get("idProcedure");

        // get the procedure's date
        final Calendar c = Calendar.getInstance();
        try {
            Procedure proc=db.getProcedure(idProcedure);
            mYear=proc.getDate().getYear();
            mMonth=proc.getDate().getMonth();
            mDay=proc.getDate().getDay();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            //get the procedure and the put the procedure's information in the activity
            Procedure procToEdit=db.getProcedure(idProcedure);
            Log.d("idprocedureedit",""+idProcedure);
            //procedure name
            nameP=(EditText)findViewById(R.id.procdureNameid);
            nameP.setText(procToEdit.getName());
            //hospital's name
            hospp=(TextView)findViewById(R.id.hospitalsNameid);
            int idHosp=procToEdit.getIdHospital();
            Hospital hos=db.getHospital(idHosp);
            hospp.setText("Hospital's name \n"+hos.getName().toUpperCase());
            //doctor's role
            roled=(TextView)findViewById(R.id.doctorRoleid);
            int idRole=procToEdit.getIdRole();
            Log.d("idroleet8ayare!",""+idRole);
            DoctorRole role=db.getDoctorRole(idRole);
            roled.setText("Doctor's role \n"+role.getRole().toUpperCase());

            //procedure's type
            typep=(TextView)findViewById(R.id.procedureTypeid);
            int idtype=procToEdit.getIdProcedureType();
            ProcedureType type=db.getProcedureType(idtype);
            typep.setText("Procedure's type \n"+type.getType().toUpperCase());

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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Details can be edited");

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.show();
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
    public void updateProcedure(View view) throws ParseException {
        //get the procedure by id
        db=DatabaseHandler.getInstance();
        Procedure procToEdit=db.getProcedure(idProcedure);
        //get the new name from the editText
        nameP=(EditText)findViewById(R.id.procdureNameid);

        updateD = new Date(mYear,mMonth,mDay);
        ///change the procedure's name by setName
        if(nameP.getText().toString().isEmpty() || datePick.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_LONG).show();
        }
        else {
            procToEdit.setName(nameP.getText().toString());
            procToEdit.setDate(updateD);
            //call the method db.updateProcedure to update the procedure
            db.updateProcedure(procToEdit);
            Toast.makeText(getApplicationContext(), "Procedure has been updated", Toast.LENGTH_LONG).show();
            //go to LogsActivity
            Intent i = new Intent(this, LogsActivity.class);
            i.putExtra("idDoctor", idDoctor);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }



}
