package com.example.user.medicallogbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.medicallogbook.sqLite.Cases;
import com.example.user.medicallogbook.sqLite.DatabaseHandler;
import com.example.user.medicallogbook.sqLite.Patient;
import com.example.user.medicallogbook.sqLite.Perform;
import com.example.user.medicallogbook.sqLite.Procedure;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SearchPatientActivity extends AppCompatActivity implements PatientDialogFragment.Communicator {

    private ListView lv;
    private SearchView sv;
    private DatabaseHandler db;
    private int idDoctor, idPatient;
    private String namePt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient);

        setTitle("Search Patients");
        final ArrayAdapter<Patient> arrayPatient;
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        idDoctor = (int) b.get("idDoctor");

        lv= (ListView) findViewById(R.id.lv);
        sv= (SearchView) findViewById(R.id.sv);

        db=DatabaseHandler.getInstance();
        ArrayList<Patient> patientsList= (ArrayList<Patient>) db.getAllPatientsByDoc(idDoctor);
        if(patientsList.size()!=0)
        {
            final Context context = this;
            arrayPatient=new ArrayAdapter<Patient>(this,android.R.layout.simple_list_item_1,patientsList);
            lv.setAdapter(arrayPatient);

            //search view
            sv.setQueryHint("Search Patients");
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    arrayPatient.getFilter().filter(newText);
                    return false;
                }
            });

            //on item click on listview.
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Patient p=(Patient)parent.getItemAtPosition(position);
                    idPatient=p.getId();
                    namePt=p.getFname()+" "+p.getLname();

                    Log.d("id patient search", "" + idPatient);

                    android.app.FragmentManager manager = getFragmentManager();

                    PatientDialogFragment dialog = new PatientDialogFragment();
                    dialog.show(manager, "dialog");

                }
            });
        } else{
             TextView tvnull= (TextView) findViewById(R.id.tvnull);
            tvnull.setText("There are no patients, go to Logs to add one.");
        }

    }

    private void showAlertConfirm() {

        // final String item_name = list_items.get(item_position_clicked);

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete " + namePt
                + "?");

        // Setting Positive "Yes" Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db = DatabaseHandler.getInstance();
                        try {
                            Patient patientTodlt=db.getPatient(idPatient);
                            //delete cases rows of the selected patient
                            List<Cases> caseToDel=db.getCaseListIdPatient(idPatient);
                            for(int i=0;i<caseToDel.size();i++){

                                int idprocTodel=caseToDel.get(i).getIdProcedure();
                                Procedure proctoDelt=db.getProcedure(idprocTodel);
                                //delete the procedure associated with the patient
                                db.deleteProcedure(proctoDelt);
                                Perform performTodelt=db.getPerformProcDoc(idDoctor,idprocTodel);
                                //delete the perform associated with the patient
                                db.deletePerform(performTodelt);
                                //delete the case associated with the patient
                                db.deleteCase(caseToDel.get(i));
                            }
                            //delete the patient
                            db.deletePatient(patientTodlt);
                            Toast.makeText(getApplicationContext(), "Patient has been deleted", Toast.LENGTH_LONG).show();
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
    @Override
    public void message(int data) {
        switch (data) {
            case 0:
                // open view edit log activity
                Intent i=new Intent(this,NewCaseActivity.class);
                    i.putExtra("idPatient",idPatient);
                    i.putExtra("idDoctor", idDoctor);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                break;

            case 1:
                // go to view/edit patient
                Intent intent=new Intent(this,ViewEditPatientActivity.class);
                intent.putExtra("idPatient", idPatient);
                intent.putExtra("idDoctor", idDoctor);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case 2:
                // delete patient
                showAlertConfirm();
                break;

        }
    }
}
