package com.example.user.medicallogbook.sqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 12/9/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MedicalLogbook";

    // tables name
    private static final String TABLE_Doctors = "DOCTORS";
    private static final String TABLE_Patients = "PATIENTS";
    private static final String TABLE_Procedures = "PROCEDURES";
    private static final String TABLE_Cases = "CASES";
    private static final String TABLE_Perform = "PERFORM";
    private static final String TABLE_ProcedureType = "PROCEDURE_TYPE";
    private static final String TABLE_Hospitals = "HOSPITALS";
    private static final String TABLE_DoctorRole = "DOCTOR_ROLES";
    private static final String TABLE_DoctorSpecialty = "DOCTOR_SPECIALITY";

    //Tables Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IDPROCEDURETYPE = "idProcedureType";
    private static final String KEY_IDHOSPITAL = "idHospital";
    private static final String KEY_IDROLE = "idRole";
    private static final String KEY_IDPROCEDURE = "idProcedure";
    private static final String KEY_IDPATIENT = "idPatient";
    private static final String KEY_IDDOCTOR = "idDoctor";
    private static final String KEY_IDSPECIALITY = "idSpeciality";
    private static final String KEY_IDDOCTORPT = "idDoctorPt";

    private static DatabaseHandler instance = null;

    public static void createInstance(Context c) {
        if (instance == null)
            instance = new DatabaseHandler(c);
    }

    public static DatabaseHandler getInstance() {
        return instance;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        initializeAllTables();
    }

    private void initializeAllTables() {
        if(getSpecialityCount()==0)
        {initializeSpecialities();}
        if(getDoctorsRoleCount()==0)
        {initializeDoctorRole();}
        if(getHospitalsCount()==0)
        {initializeHospitals();}
        if(getProcedureTypesCount()==0)
        {initializeProceduresTypes();}
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("create tables", "creating");
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Doctors);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Patients);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Procedures);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Perform);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Cases);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DoctorRole);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ProcedureType);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Hospitals);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DoctorSpecialty);
    }

    //create doctor's seciality table
    public void createDoctorSpecialityTable(SQLiteDatabase db){
        String create_DoctorSpeciality_table="create table "+ TABLE_DoctorSpecialty+"("
                +KEY_ID+" INTEGER PRIMARY KEY,"+" SPECIALITY "+" TEXT" + ")";
        db.execSQL(create_DoctorSpeciality_table);
    }
    //create doctor's table
    public void createDoctorsTable(SQLiteDatabase db) {
        String CREATE_DOCTORS_TABLE = "CREATE TABLE " + TABLE_Doctors + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + " FNAME"+ " TEXT,"
                + " LNAME" + " TEXT, " +"EMAIL"+" TEXT, "+"PASSWORD"+" TEXT, "
                +KEY_IDSPECIALITY+ " INTEGER FOREIGNKEY REFERENCES "+ TABLE_DoctorSpecialty+"("+KEY_ID+"))";
        db.execSQL(CREATE_DOCTORS_TABLE);
    }

    public void createProceduresTable(SQLiteDatabase db) {
        String CREATE_PROCEDURES_TABLE = "CREATE TABLE " + TABLE_Procedures + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + "NAME"+ " TEXT,"
                + " DATEP" + " date, " +KEY_IDPROCEDURETYPE+ " INTEGER FOREIGNKEY REFERENCES "+ TABLE_ProcedureType+"("+KEY_ID+"), "
                +KEY_IDHOSPITAL+ " INTEGER FOREIGNKEY REFERENCES "+ TABLE_Hospitals+"("+KEY_ID+"), "
                +KEY_IDROLE+ " INTEGER FOREIGNKEY REFERENCES "+ TABLE_DoctorRole+"("+KEY_ID+")"
                + ")";
        db.execSQL(CREATE_PROCEDURES_TABLE);
    }

    public void createPatientTable(SQLiteDatabase db) {
        String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_Patients + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + "FNAME"+ " TEXT,"
                + "LNAME" + " TEXT, " +"EMAIL "+"TEXT, "+"BLOOD_GROUP "+"TEXT, "+"GENDER "+"TEXT, "
                + "DATE_OF_BIRTH" + " date, "
                + KEY_IDDOCTORPT+ " INTEGER FOREIGNKEY REFERENCES "+ TABLE_Doctors+"("+KEY_ID+")"
                + ")";
        db.execSQL(CREATE_PATIENTS_TABLE);
    }
    public void createCasesTable(SQLiteDatabase db) {
        String CREATE_CASES_TABLE = "CREATE TABLE " + TABLE_Cases + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"+KEY_IDPROCEDURE+ " INTEGER FOREIGNKEY REFERENCES "+ TABLE_Procedures+"("+KEY_ID+"),"
                +KEY_IDPATIENT+ " INTEGER FOREIGNKEY REFERENCES "+ TABLE_Patients+"("+KEY_ID+")"+ ")";
        db.execSQL(CREATE_CASES_TABLE);
    }

    public void createPerformTable(SQLiteDatabase db) {
        String CREATE_PERFORM_TABLE = "CREATE TABLE " + TABLE_Perform + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"+KEY_IDDOCTOR+" INTEGER FOREIGNKEY REFERENCES "+ TABLE_Doctors+"("+KEY_ID+"),"+
                KEY_IDPROCEDURE+" INTEGER FOREIGNKEY REFERENCES "+ TABLE_Procedures+"("+KEY_ID+")"
                +")";
        db.execSQL(CREATE_PERFORM_TABLE);
    }

    // doctor's role table
    public void createDoctorRoleTable(SQLiteDatabase db){
        String create_ROLES_table="create table "+ TABLE_DoctorRole+"("
                +KEY_ID+" INTEGER PRIMARY KEY, "+" ROLE "+" TEXT" + ")";
        db.execSQL(create_ROLES_table);
    }

    //Hospitals table
    public void createHospitalsTable(SQLiteDatabase db){
        String create_HOSPITALS_table="create table "+ TABLE_Hospitals+"("
                +KEY_ID+" INTEGER PRIMARY KEY,"+" NAME "+" TEXT" + ")";
        db.execSQL(create_HOSPITALS_table);
    }


    //procedure's type table
    public void createProcedureTypeTable(SQLiteDatabase db){
        String create_ProcedureTYPE_table="create table "+ TABLE_ProcedureType+"("
                +KEY_ID+" INTEGER PRIMARY KEY,"+" TYPE "+" TEXT" + ")";
        db.execSQL(create_ProcedureTYPE_table);
    }


    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // String date = sdf.format(new Date());

    private void createTables(SQLiteDatabase db) {
        Log.d("dddd","creating tables");
        createDoctorSpecialityTable(db);
        Log.d("DoctorsSpec Table", "created");
        createDoctorsTable(db);
        Log.d("creating doctor table, ", "created");
        createDoctorRoleTable(db);
        Log.d("creating Roles table, ", "created");
        createHospitalsTable(db);
        Log.d("hospitals table,", "created");

        createProcedureTypeTable(db);
        Log.d("ProcedureTypes table,", "created");
        createProceduresTable(db);
        Log.d("Procedure table,", "created");
        createPatientTable(db);
        Log.d("Patients table,", "created");
        createCasesTable(db);
        Log.d("cases table,", "created");
        createPerformTable(db);
        Log.d("Perform table,", "created");

    }

    // Doctors table
    // adding a new doctor
    public void addDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FNAME", doctor.getFname());
        values.put("LNAME", doctor.getLname());
        values.put("EMAIL", doctor.getEmail());
        values.put("PASSWORD", doctor.getPassword());
        values.put(KEY_IDSPECIALITY, doctor.getSpeciality());

        // Inserting Row
        db.insert(TABLE_Doctors, null, values);
        db.close(); // Closing database connection

    }

    // Getting a single doctor by email

    public Doctor getDoctor(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Doctor doctor=null;
        Cursor cursor = db.query(TABLE_Doctors, new String[]{KEY_ID,
                        "FNAME", "LNAME", " EMAIL", " PASSWORD", KEY_IDSPECIALITY}, "EMAIL" + "=?",
                new String[]{String.valueOf(email)}, null, null, null, null);

        if (cursor.getCount()!=0) {
            cursor.moveToFirst();

            doctor = new Doctor(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)));
        }
        cursor.close();
        return doctor;
    }

//get doctor by id
    public Doctor getDoctorbyID(int idD) {
        SQLiteDatabase db = this.getReadableDatabase();
        Doctor doctor=null;
        Cursor cursor = db.query(TABLE_Doctors, new String[]{KEY_ID,
                        "FNAME", "LNAME", "EMAIL", "PASSWORD", KEY_IDSPECIALITY}, KEY_ID + "=?",
                new String[]{String.valueOf(idD)}, null, null, null, null);

        if (cursor.getCount()!=0) {
            cursor.moveToFirst();

            doctor = new Doctor(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)));
        }
        cursor.close();
        return doctor;
    }
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctorList = new ArrayList<Doctor>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Doctors;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Doctor doc = new Doctor();
                doc.setId(Integer.parseInt(cursor.getString(0)));
                doc.setEmail(cursor.getString(3));
                // Adding doctor to list
                doctorList.add(doc);
            } while (cursor.moveToNext());
        }

        // return doctors list
        return doctorList;
    }

    // Getting Doctors Count
    public int getDoctorsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_Doctors;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c=cursor.getCount();
        cursor.close();

        // return count
        return c;
    }
    // Updating doctor
    public int updateDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FNAME", doctor.getFname());
        values.put("LNAME", doctor.getLname());
        values.put("EMAIL", doctor.getEmail());
        values.put("PASSWORD", doctor.getPassword());
        values.put(KEY_IDSPECIALITY, doctor.getSpeciality());

        // updating row
        return db.update(TABLE_Doctors, values, KEY_ID + " = ?",
                new String[]{String.valueOf(doctor.getId())});
    }

    // Deleting a Doctor
    public void deleteDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Doctors, KEY_ID + " = ?",
                new String[]{String.valueOf(doctor.getId())});
        db.close();
    }

    // Doctor Role table
    // adding a new role
    public void addDoctorRole(DoctorRole doctorRole) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ROLE", doctorRole.getRole());

        // Inserting Row
        db.insert(TABLE_DoctorRole, null, values);
        db.close();
        // / Closing database connection
    }
    //get one doctor's role
    public DoctorRole getDoctorRole(int idRole) {
        SQLiteDatabase db = this.getReadableDatabase();
        DoctorRole role=null;
        Cursor cursor = db.query(TABLE_DoctorRole, new String[]{KEY_ID,
                        "ROLE"}, KEY_ID + "=?",
                new String[]{String.valueOf(idRole)}, null, null, null, null);

        if (cursor.getCount()!=0) {
            cursor.moveToFirst();

            role= new DoctorRole(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
        }
        cursor.close();
        return role;
    }

    //get Doctors role
    public List<DoctorRole> getAllDoctorsRole() {
        List<DoctorRole> doctorRoleList = new ArrayList<DoctorRole>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DoctorRole;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DoctorRole docRole = new DoctorRole();
                docRole.setId(Integer.parseInt(cursor.getString(0)));
                docRole.setRole(cursor.getString(1));
                // Adding role to list
                doctorRoleList.add(docRole);
            } while (cursor.moveToNext());
        }

        // return DoctorRole list
        return doctorRoleList;
    }
        // initilize doctor role
    public void initializeDoctorRole() {
        String[] role = new String[]{"Primary Surgeon", "Assistant Surgeon", "Anesthesiologist", "Allied Health Professional","Nonsurgical Physician"};
        for (int i = 0; i < role.length; i++) {
            addDoctorRole(new DoctorRole(role[i]));
        }
    }
    // Getting Doctors Count
    public int getDoctorsRoleCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DoctorRole;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c=cursor.getCount();
        cursor.close();

        // return count
        return c;
    }

    //Hospitals table
    // adding a hospital
    public void addHospital(Hospital hospital) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NAME", hospital.getName());

        // Inserting Row
        db.insert(TABLE_Hospitals, null, values);
        db.close();
        // Closing database connection
    }
    //get Hospital
    public List<Hospital> getAllHospitals() {
        List<Hospital> hospitalList = new ArrayList<Hospital>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Hospitals;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Hospital hospital=new Hospital();
                hospital.setId(Integer.parseInt(cursor.getString(0)));
                hospital.setName(cursor.getString(1));
                // Adding hospital to list
                hospitalList.add(hospital);
            } while (cursor.moveToNext());
        }

        // return hospitals list
        return hospitalList;
    }
    //get one hospital
    public Hospital getHospital(int idHospital){
        SQLiteDatabase db = this.getReadableDatabase();
        Hospital hospital=null;
        Cursor cursor = db.query(TABLE_Hospitals, new String[]{KEY_ID,
                        "NAME"}, KEY_ID + "=?",
                new String[]{String.valueOf(idHospital)}, null, null, null, null);

        if (cursor.getCount()!=0) {
            cursor.moveToFirst();

            hospital= new Hospital(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
        }
        cursor.close();
        return hospital;
    }
    // initialize Hospitals
    public void initializeHospitals() {
        String[] hospitals = new String[]{"Hôtel-Dieu de France - Beirut", "Centre Hospitalier Universitaire Notre Dame des Secours - Jbeil", "University Medical Center-Rizk Hospital - Beirut",
                "Lebanon Heart Hospital - Tripoli","Middle East Laser Clinic - Hazmieh","Rafik Hariri University Hospital - Jnah","Saint George Hospital - Beirut",
                "Nabatiye Governmental hospital - Nabatiye","Zahraa University Hospital - Beirut","Hammoud Hospital University Medical Center - Saida",
                "AL-Salam Hospital - Tripoli","Hiram Hospital - Tyre - Jal El Bahr","Advanced BMI Weight Loss Clinic - Mount Lebanon"};
        for (int i = 0; i < hospitals.length; i++) {
            addHospital(new Hospital(hospitals[i]));
        }
    }
    // Getting hospitals Count
    public int getHospitalsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_Hospitals;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c=cursor.getCount();
        cursor.close();

        // return count
        return c;
    }


    //Procedure's Type table
    // adding a type
    public void addProcedureType(ProcedureType type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("TYPE", type.getType());

        // Inserting Row
        db.insert(TABLE_ProcedureType, null, values);
        db.close();
        // Closing database connection
    }
    //get one type
    public ProcedureType getProcedureType(int idType){
        SQLiteDatabase db = this.getReadableDatabase();
        ProcedureType type=null;
        Cursor cursor = db.query(TABLE_ProcedureType, new String[]{KEY_ID,
                        "TYPE"}, KEY_ID + "=?",
                new String[]{String.valueOf(idType)}, null, null, null, null);

        if (cursor.getCount()!=0) {
            cursor.moveToFirst();

            type= new ProcedureType(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
        }
        cursor.close();
        return type;
    }
    //get Types
    public List<ProcedureType> getAllProcedureTypes() {
        List<ProcedureType> typesList = new ArrayList<ProcedureType>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ProcedureType;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProcedureType type=new ProcedureType();
                type.setId(Integer.parseInt(cursor.getString(0)));
                type.setType(cursor.getString(1));
                // Adding type to list
                typesList.add(type);
            } while (cursor.moveToNext());
        }

        // return procedure types list
        return typesList;
    }
    // initialize procedure Types
    public void initializeProceduresTypes() {
        String[] types = new String[]{"Transplant", "Biopsy", "Fasciotomy", "Cardiac surgery","LASIK","Mastectomy","Breast implant","Hip replacement",
                "Liposuction","Facial Implants","Neck Lift","Heart Transplant","Radiofrequency Ablation","Cardiomyoplasty","Spinal Fusion & Reconstruction",
                "Ulnar Nerve Surgery","Brain Tumor Removal","Spinal Fusion & Reconstruction","Cervical Arthroplasty","Hair loss and restoration","Soft-tissue augmentation",
                "Dermabrasion"};
        for (int i = 0; i < types.length; i++) {
            addProcedureType(new ProcedureType(types[i]));
        }
    }
    // Getting procedureTypes Count
    public int getProcedureTypesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ProcedureType;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c=cursor.getCount();
        cursor.close();

        // return count
        return c;
    }
    //Doctor's speciality table
    // adding a speciality
    public void addSpeciality(Speciality speci) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("SPECIALITY", speci.getSeciality());

        // Inserting Row
        db.insert(TABLE_DoctorSpecialty, null, values);
        db.close();
        // Closing database connection
    }
    //get Specialities
    public List<Speciality> getAllSpecialities() {
        List<Speciality> speciaList = new ArrayList<Speciality>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DoctorSpecialty;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Speciality speciality=new Speciality();
                speciality.setId(Integer.parseInt(cursor.getString(0)));
                speciality.setSeciality(cursor.getString(1));
                // Adding speciality to list
                speciaList.add(speciality);
            } while (cursor.moveToNext());
        }

        // return specialities list
        return speciaList;
    }
    //get a Speciality by its id;
    public Speciality getSpeciality(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Speciality speciality=null;
        Cursor cursor = db.query(TABLE_DoctorSpecialty, new String[]{KEY_ID,
                        "SPECIALITY"}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor.getCount()!=0) {
            cursor.moveToFirst();

            speciality= new Speciality(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
        }
        cursor.close();
        return speciality;
    }
    // initialize specialities
    public void initializeSpecialities() {
        String[] spec = new String[]{"Cancer/Oncology", "Cardiovascular Health", "Family Medicine", "General and Internal Medicine",
                "Orthopedic Surgery","Dermatology","Gastroenterology","Surgery","Neurology & Neurosurgery","Pediatrics","Plastic Surgery"};
        for (int i = 0; i < spec.length; i++) {
            addSpeciality(new Speciality(spec[i]));
        }
    }
    // Getting Speciality Count
    public int getSpecialityCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DoctorSpecialty;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c=cursor.getCount();
        cursor.close();
        // return count
        return c;
    }

    //Procedure Table
    // adding a new procedure
    public void addProcedure(Procedure procedure) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date=procedure.getDate();
        ContentValues values = new ContentValues();
        values.put("NAME", procedure.getName());
        values.put("DATEP", dateFormat.format(date));
        values.put(KEY_IDPROCEDURETYPE, procedure.getIdProcedureType());
        values.put(KEY_IDHOSPITAL, procedure.getIdHospital());
        values.put(KEY_IDROLE, procedure.getIdRole());

        // Inserting Row
        db.insert(TABLE_Procedures, null, values);
        db.close(); // Closing database connection

    }

    // Getting a single procedure by id
  public Procedure getProcedure(int id) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Procedure procedure=null;
        Cursor cursor = db.query(TABLE_Procedures, new String[]{KEY_ID,
                        "NAME", "DATEP", KEY_IDPROCEDURETYPE, KEY_IDHOSPITAL, KEY_IDROLE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Log.d("procedureCount in db:, ", "" + cursor.getCount());
        if (cursor!=null && cursor.moveToFirst()) {
            Log.d("procedure in db:, ",""+cursor.getCount());
            cursor.moveToFirst();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            procedure = new Procedure(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    dateFormat.parse(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));

        }
       cursor.close();
       Log.d("procedure in db:, ", "" + procedure.getName());
        return procedure;
    }

    //getting a procedure by name
    public Procedure getProcedurebyName(String name) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Procedure procedure=null;
        Cursor cursor = db.query(TABLE_Procedures, new String[]{KEY_ID,
                        "NAME", "DATEP", KEY_IDPROCEDURETYPE,KEY_IDHOSPITAL, KEY_IDROLE}, "NAME" + "=?",
                new String[]{String.valueOf(name)}, null, null, null, null);

        Log.d("procedureCount in db:, ", "" + cursor.getCount());
        if (cursor!=null && cursor.moveToFirst()) {
            Log.d("procedure in db:, ",""+cursor.getCount());
            cursor.moveToFirst();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            procedure = new Procedure(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    dateFormat.parse(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));

        }
        cursor.close();
        //Log.d("procedure in db:, ", "" + procedure.getName());
        return procedure;
    }

    // get list of procedures
    public List<Procedure> getAllProcedures() {
        List<Procedure> procedureList = new ArrayList<Procedure>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Procedures +"ORDER BY DATE DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Procedure procedure=new Procedure();
                procedure.setId(Integer.parseInt(cursor.getString(0)));
                procedure.setName(cursor.getString(1));
                procedure.setDate(java.sql.Date.valueOf(cursor.getString(2)));
                // Adding procedure to list
                procedureList.add(procedure);
            } while (cursor.moveToNext());
        }

        // return procedures list
        return procedureList;
    }

    // Getting Procedures Count
    public int getProceduresCount() {
        String countQuery = "SELECT  * FROM " + TABLE_Procedures;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c=cursor.getCount();
        cursor.close();

        // return count
        return c;
    }
    // Updating procedure
    public int updateProcedure(Procedure procedure) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date=procedure.getDate();
        ContentValues values = new ContentValues();
        values.put("NAME", procedure.getName());
        values.put("DATEP",dateFormat.format(date));

        // updating row
        return db.update(TABLE_Procedures, values, KEY_ID + " = ?",
                new String[]{String.valueOf(procedure.getId())});
    }

    // Deleting a Procedure
    public void deleteProcedure(Procedure procedure) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Procedures, KEY_ID + " = ?",
                new String[]{String.valueOf(procedure.getId())});
        db.close();
    }

    // Patients table
    // adding a new Patient
    public void addPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date=patient.getDob();
        ContentValues values = new ContentValues();
        values.put("FNAME", patient.getFname());
        values.put("LNAME", patient.getLname());
        values.put("EMAIL", patient.getEmail());
        values.put("BLOOD_GROUP",patient.getBloodGroup());
        values.put("GENDER", patient.getGender());
        values.put("DATE_OF_BIRTH",dateFormat.format(date));
        values.put(KEY_IDDOCTORPT,patient.getIdDoc());

        // Inserting Row
        db.insert(TABLE_Patients, null, values);
        db.close(); // Closing database connection

    }

    // Getting a single doctor

    public Patient getPatient(int id) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Patient patient=null;
        Cursor cursor = db.query(TABLE_Patients, new String[]{KEY_ID,
                        "FNAME", "LNAME", "EMAIL", "BLOOD_GROUP", "GENDER", "DATE_OF_BIRTH", KEY_IDDOCTORPT}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (cursor.getCount()!=0) {
            cursor.moveToFirst();
            Date date=dateFormat.parse(cursor.getString(6));
            patient = new Patient(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), (java.util.Date) date, Integer.parseInt(cursor.getString(7)));
        }
        cursor.close();
        return patient;
    }
    //getting all patients
    public List<Patient> getAllPatients() {
        List<Patient> patientsList = new ArrayList<Patient>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Patients;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Patient patient=new Patient();
                patient.setId(Integer.parseInt(cursor.getString(0)));
                patient.setFname(cursor.getString(1));
                patient.setLname(cursor.getString(2));
                patient.setEmail(cursor.getString(3));
                patient.setBloodGroup(cursor.getString(4));
                patient.setGender(cursor.getString(5));
                patient.setDob(java.sql.Date.valueOf(cursor.getString(6)));
                patient.setIdDoc(Integer.parseInt(cursor.getString(7)));
                // Adding patient to list
                patientsList.add(patient);
            } while (cursor.moveToNext());
        }

        // return patients list
        return patientsList;
    }
    //getting all patients
    public List<Patient> getAllPatientsByDoc(int idDoc) {
        List<Patient> patientsList = new ArrayList<Patient>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Patients+" WHERE "+KEY_IDDOCTORPT+" ="+idDoc+";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Patient patient=new Patient();
                patient.setId(Integer.parseInt(cursor.getString(0)));
                patient.setFname(cursor.getString(1));
                patient.setLname(cursor.getString(2));
                patient.setEmail(cursor.getString(3));
                patient.setBloodGroup(cursor.getString(4));
                patient.setGender(cursor.getString(5));
                patient.setDob(java.sql.Date.valueOf(cursor.getString(6)));
                patient.setIdDoc(Integer.parseInt(cursor.getString(7)));
                // Adding patient to list
                patientsList.add(patient);
            } while (cursor.moveToNext());
        }

        // return patients list
        return patientsList;
    }

    // Getting Patients Count
    public int getPatientsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_Patients;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c=cursor.getCount();
        cursor.close();

        // return count
        return c;
    }
    // Updating a patient
    public int updatePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FNAME", patient.getFname());
        values.put("LNAME", patient.getLname());
        values.put("EMAIL", patient.getEmail());

        // updating row
        return db.update(TABLE_Patients, values, KEY_ID + " = ?",
                new String[]{String.valueOf(patient.getId())});
    }

    // Deleting a Patient
    public void deletePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Patients, KEY_ID + " = ?",
                new String[]{String.valueOf(patient.getId())});
        db.close();
    }

    //Perform table
    // adding to perform table
    public void addPerform(Perform perform) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDDOCTOR, perform.getIdDoctor());
        values.put(KEY_IDPROCEDURE, perform.getIdProcedure());

        // Inserting Row
        db.insert(TABLE_Perform, null, values);
        db.close();
        // Closing database connection
    }

    // get list of perfom with an id
    public List<Perform> getPeformListId(int idDoc){
        List<Perform> performList=new ArrayList<Perform>();
        String selectQuery="SELECT * FROM "+TABLE_Perform+" WHERE "+KEY_IDDOCTOR+" ="+idDoc+";";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery, null);
        //loop
        if(cursor.moveToFirst()){
            do{
                Perform perform=new Perform();
                perform.setId(Integer.parseInt(cursor.getString(0)));
                perform.setIdDoctor(Integer.parseInt(cursor.getString(1)));
                perform.setIdProcedure(Integer.parseInt(cursor.getString(2)));
                performList.add(perform);
            }while(cursor.moveToNext());
        }
        for(int i=0;i<performList.size();i++){
            Log.d("perform",""+performList.get(i).getId());
        }
        return performList;
    }

    //get perform table by id doctor and id procedure
    public Perform getPerformProcDoc(int id,int idp) {
        SQLiteDatabase db = this.getReadableDatabase();
        Perform perform=null;
        Cursor cursor = db.query(TABLE_Perform, new String[]{KEY_ID, KEY_IDDOCTOR, KEY_IDPROCEDURE}, KEY_IDDOCTOR + "=? and " + KEY_IDPROCEDURE + "=?",
                new String[]{String.valueOf(id), String.valueOf(idp)}, null, null, null, null);

        if (cursor.getCount()!=0) {
            cursor.moveToFirst();

            perform = new Perform(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)) );
        }
        cursor.close();
        return perform;
    }
    // Getting a single perform
    public Perform getPerform(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Perform perform=null;
        Cursor cursor = db.query(TABLE_Perform, new String[]{KEY_ID, KEY_IDDOCTOR, KEY_IDPROCEDURE}, KEY_IDDOCTOR + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor.getCount()!=0) {
            cursor.moveToFirst();

            perform = new Perform(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)) );
        }
        cursor.close();
        return perform;
    }
    //get perform table
    public List<Perform> getAllPerform() {
        List<Perform> performList = new ArrayList<Perform>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Perform;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Perform perform=new Perform();
                perform.setId(Integer.parseInt(cursor.getString(0)));
                perform.setIdDoctor(Integer.parseInt(cursor.getString(1)));
                perform.setIdProcedure(Integer.parseInt(cursor.getString(2)));
                        // Adding perform to list
                        performList.add(perform);
            } while (cursor.moveToNext());
        }

        // return perform list
        return performList;
    }

    // Getting perform Count
    public int getPerformCount() {
        String countQuery = "SELECT  * FROM " + TABLE_Perform;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c=cursor.getCount();
        cursor.close();

        // return count
        return c;
    }
    // Deleting a row in Perform
    public void deletePerform(Perform perform) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Perform, KEY_ID + " = ?",
                new String[]{String.valueOf(perform.getId())});
        db.close();
    }

    //Cases table
    // adding to cases table
    public void addCase(Cases cases) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDPROCEDURE, cases.getIdProcedure());
        values.put(KEY_IDPATIENT, cases.getIdpatient());

        // Inserting Row
        db.insert(TABLE_Cases, null, values);
        db.close();
        // Closing database connection
    }

    //get Cases table
    public List<Cases> getAllCases() {
        List<Cases> caseList = new ArrayList<Cases>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Cases;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Cases cases=new Cases();
                cases.setId(Integer.parseInt(cursor.getString(0)));
                cases.setIdProcedure(Integer.parseInt(cursor.getString(1)));
                cases.setIdpatient(Integer.parseInt(cursor.getString(2)));
                // Adding case to list
                caseList.add(cases);
            } while (cursor.moveToNext());
        }

        // return case list
        return caseList;
    }
    // get list of cases with a procedure id
    public List<Cases> getCaseListId(int idProce){
        List<Cases> caseList=new ArrayList<Cases>();
        String selectQuery="SELECT * FROM "+TABLE_Cases+" WHERE "+KEY_IDPROCEDURE+" ="+idProce+";";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery, null);
        //loop
        if(cursor.moveToFirst()){
            do{
                Cases cases=new Cases();
                cases.setId(Integer.parseInt(cursor.getString(0)));
                cases.setIdProcedure(Integer.parseInt(cursor.getString(1)));
                cases.setIdpatient(Integer.parseInt(cursor.getString(2)));
                caseList.add(cases);
            }while(cursor.moveToNext());
        }
        for(int i=0;i<caseList.size();i++){
            Log.d("case",""+caseList.get(i).getId());
        }
        return caseList;
    }
    //get case from patient id
    public List<Cases> getCaseListIdPatient(int idPatient){
        List<Cases> caseList=new ArrayList<Cases>();
        String selectQuery="SELECT * FROM "+TABLE_Cases+" WHERE "+KEY_IDPATIENT+" ="+idPatient+";";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery, null);
        //loop
        if(cursor.moveToFirst()){
            do{
                Cases cases=new Cases();
                cases.setId(Integer.parseInt(cursor.getString(0)));
                cases.setIdProcedure(Integer.parseInt(cursor.getString(1)));
                cases.setIdpatient(Integer.parseInt(cursor.getString(2)));
                caseList.add(cases);
            }while(cursor.moveToNext());
        }
        for(int i=0;i<caseList.size();i++){
            Log.d("case",""+caseList.get(i).getId());
        }
        return caseList;
    }
    public Cases getCase(int idProc) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cases cases=null;
        Cursor cursor = db.query(TABLE_Cases, new String[]{KEY_ID, KEY_IDPROCEDURE, KEY_IDPATIENT}, KEY_IDPROCEDURE + "=?",
                new String[]{String.valueOf(idProc)}, null, null, null, null);

        if (cursor.getCount()!=0) {
            cursor.moveToFirst();

            cases= new Cases(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)) );
        }
        cursor.close();
        return cases;
    }

    // Getting case Count
    public int getCaseCount() {
        String countQuery = "SELECT  * FROM " + TABLE_Cases;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c=cursor.getCount();
        cursor.close();

        // return count
        return c;
    }
    // Deleting a case
    public void deleteCase(Cases cases) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Cases, KEY_ID + " = ?",
                new String[]{String.valueOf(cases.getId())});
        db.close();
    }


}

