package com.example.user.medicallogbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.medicallogbook.sqLite.DatabaseHandler;
import com.example.user.medicallogbook.sqLite.Doctor;
import com.example.user.medicallogbook.sqLite.Speciality;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private int idDoctor;
    private DatabaseHandler db;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private String username, password;
    private EditText email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView registerScreen = (TextView) findViewById(R.id.link_signup);


        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Sign up screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        //creating database instance
        DatabaseHandler.createInstance(this);
        Log.d("Create Instance: ", "creating ..");
         email = (EditText) findViewById(R.id.email);
         pass = (EditText) findViewById(R.id.password);
        if(username!=null && password!=null){
            email.setText(username);
            pass.setText(password);
        }

    }

    //This method is used to create the shared preferences and to write the contents in it by Editor.
    public void sharedPrefernces() {
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();
        toEdit.putString("Username", username);
        toEdit.putString("Password", password);
        toEdit.commit();
    }


    // go to Logs Activity
    public void goLogsActivity(View view) {

       db = DatabaseHandler.getInstance();
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);

        //check if the mail is valid
        if (isEmailValid(email.getText().toString())) {


            Doctor doctor = db.getDoctor(email.getText().toString());

            //if the mail exists in the database
            if (doctor == null) {
                Toast.makeText(getApplicationContext(), "Email does not exist!", Toast.LENGTH_LONG).show();
            } else if (!pass.getText().toString().equals(doctor.getPassword())) {
                Toast.makeText(getApplicationContext(), "Password is not correct!", Toast.LENGTH_LONG).show();
            } else {
                idDoctor = doctor.getId();
                //save user's session until logout
               /* SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("email",email.getText().toString());*/

                //sharedPrefernces() method will be called which will create a new file named as “Login Credentials” and
                // write the data inside it by take from username and password fields.
                username = email.getText().toString();
                password = pass.getText().toString();
                sharedPrefernces();
                // go to the next activity
                Intent intent = new Intent(this, LogsActivity.class);
                intent.putExtra("idDoctor", idDoctor);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } else
            Toast.makeText(getApplicationContext(), "Email form is not valid!", Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aboutLogin) {
            Intent i = new Intent(this,AboutActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
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
