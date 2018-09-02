package com.example.user.medicallogbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnLongClickListener;

import com.example.user.medicallogbook.sqLite.Cases;
import com.example.user.medicallogbook.sqLite.DatabaseHandler;
import com.example.user.medicallogbook.sqLite.Patient;
import com.example.user.medicallogbook.sqLite.Perform;
import com.example.user.medicallogbook.sqLite.Procedure;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        MyDialogFragment.Communicator{

    private DatabaseHandler db;
    private int idDoctor, idProcedure;
    private ImageButton imageButton;
    private SearchView sv;
    private String procedure_name;
    private ListView logs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        setTitle("All Logs");
        db = DatabaseHandler.getInstance();
        //getting extras...
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        idDoctor = (int) b.get("idDoctor");


        logs = (ListView) findViewById(R.id.listview);
        sv=(SearchView)findViewById(R.id.svlogs);
        final Context context = this;

        imageButton = (ImageButton) findViewById(R.id.addCase);
        ArrayList<LogItems> items = new ArrayList<LogItems>();
        String patientName;
        // 3ende ana id l doctor.
        // men l id lal doctor bshouf l perform table shou l id lal procedure
        TextView exists = (TextView) findViewById(R.id.existid);
        //Perform p=db.getPerform(idDoctor);
        List<Perform> plist = db.getPeformListId(idDoctor);
        //if the perform table is not null => there are logs for this doctor
        if (plist.size() != 0) {
            //exists.setText("there are logs " + plist.size());
            //set the imagebutton for addlog to invisible
            imageButton.setVisibility(View.INVISIBLE);
            Log.d("perform iddoctor, ", "" + idDoctor);
            // loop around  the perfom table
            for (int i = 0; i < plist.size(); i++) {
                Log.d("PLISTTTT, ", "" + plist.get(i).getId());
                //and get the procedure with the id present in the perform table
                idProcedure = plist.get(i).getIdProcedure();
                Log.d("perform id proc, ", "" + idProcedure);
                try {
                    //procedure's count
                    int c = db.getProceduresCount();
                    Log.d("procedure's count, ", "" + c);
                    //get the procedure for each row in perform table
                    Procedure proc = db.getProcedure(idProcedure);
                    Log.d("procedureidbaby, ", "" + proc.getId() + " " + proc.getName());
                    //if the procedure returned is not null
                    //if (proc != null) {
                    Log.d("not null baby, ", "lol");
                    //get the procedure name, date to display it the listview
                    String procedureName = proc.getName();
                    Log.d("name P: ", "" + procedureName);
                    int yearDate = proc.getDate().getYear();
                    int monthDate = proc.getDate().getMonth();
                    int dayDate = proc.getDate().getDay();
                    String dateProc = dayDate + "-" + monthDate + "-" + yearDate;
                    // to get the patient name, we need to see the cases
                    List<Cases> caseList = db.getCaseListId(idProcedure);
                    if (caseList.size() != 0) {
                        for (int i1 = 0; i1 < caseList.size(); i1++) {
                            int idpatient = caseList.get(i1).getIdpatient();
                            Patient patient = db.getPatient(idpatient);
                            Log.d("patient id",""+idpatient);
                            patientName = patient.getFname() + " " + patient.getLname();
                            // display the listview items
                            LogItems itemsObj = new LogItems(procedureName,patientName,dateProc);
                            items.add(itemsObj);

                            final CustomAdapter adapter = new CustomAdapter(this, items);
                            logs.setAdapter(adapter);
                            logs.setOnItemClickListener(this);

                            adapter.notifyDataSetChanged();
                            //search view
                            sv.setQueryHint("Search Logs");
                            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    adapter.getFilter().filter(newText);
                                    return false;
                                }
                            });
                            //when the user clicks on an item from the list
                        }//end for
                    }// end if(caseList.size!=0)

                }// end of try
                catch (ParseException e) {
                    e.printStackTrace();
                } // end of catch
            }//end of for

        } else {
            Log.d("perform is null", "perform in null");
            exists.setText("There are no Logs, tap on the button to add one");
            imageButton.setVisibility(View.VISIBLE);
        }

        addListenerOnButton();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SubMenu submenu = menu.addSubMenu(0, Menu.NONE, 1, "New").setIcon(R.drawable.addbtn);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
            Intent i = new Intent(this, SettingsActivity.class);
            i.putExtra("idDoctor", idDoctor);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        if(id==R.id.About){
            Intent i = new Intent(this,AboutActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        if (id == R.id.form1) {
            Intent i = new Intent(this, NewCaseActivity.class);
            i.putExtra("idDoctor", idDoctor);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }
        if (id == R.id.form2) {
            Intent i = new Intent(this, PatientActivity.class);
            i.putExtra("idDoctor", idDoctor);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }
        if(id==R.id.form3){
            Intent i = new Intent(this, SearchPatientActivity.class);
            i.putExtra("idDoctor", idDoctor);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addListenerOnButton() {

        imageButton = (ImageButton) findViewById(R.id.addCase);
        final Context context = this;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NewCaseActivity.class);
                i.putExtra("idDoctor", idDoctor);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

    }


    @Override
    public void message(int data) {
        switch (data) {
            case 0:
                // open view edit log activity
                Intent i=new Intent(this,ViewEditLogActivity.class);
                try {
                    Procedure procToEdit=db.getProcedurebyName(procedure_name);
                    int idProcToEdit=procToEdit.getId();
                    i.putExtra("idProcedure",idProcToEdit);
                    i.putExtra("idDoctor", idDoctor);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                // Delete log
                showAlertConfirm();
                break;
        }
    }

    private void showAlertConfirm() {

       // final String item_name = list_items.get(item_position_clicked);

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete " + procedure_name
                + "?");

        // Setting Positive "Yes" Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db = DatabaseHandler.getInstance();
                        try {
                            Procedure procToDel = db.getProcedurebyName(procedure_name);

                            int idProcTodel=procToDel.getId();
                            Perform performToDel=db.getPerformProcDoc(idDoctor,idProcTodel);
                            //delete perform row of the selected procedure
                            db.deletePerform(performToDel);
                            //delete cases row of the selected procedure
                            Cases caseToDel=db.getCase(idProcTodel);
                            db.deleteCase(caseToDel);
                            //delete the procedure
                            db.deleteProcedure(procToDel);
                            Toast.makeText(getApplicationContext(), "Procedure has been deleted", Toast.LENGTH_LONG).show();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        LogItems li=(LogItems)parent.getItemAtPosition(position);
        procedure_name=li.getPatient_name();
        Log.d("procedure name",procedure_name);

        android.app.FragmentManager manager = getFragmentManager();

        MyDialogFragment dialog = new MyDialogFragment();
        dialog.show(manager, "dialog");
    }

}

