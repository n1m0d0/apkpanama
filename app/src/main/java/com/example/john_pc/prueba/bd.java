package com.example.john_pc.prueba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class bd {

    // TABLA USUARIO
    private static final String idUser = "_id";
    private static final String nameUser = "nameUser";
    private static final String codeUser = "codeUser";

    // TABLA DE FORMULARIOS
    private static final String idForm = "_id";
    private static final String formJson = "formJson";

    // TABLA EVENTO
    private static final String idEvent = "_id";
    private static final String eventJson = "eventJson";

    // TABLA REGISTRO
    private static final String idReg = "_id";
    private static final String regJson = "regJson";

    // TABLA DE LISTA DE EVENTOS
    // TABLA EVENTO
    private static final String idListEvents = "_id";
    private static final String listEventsJson = "path";

    // BASE DE DATOS TABLAS
    private static final String BD = "BD_GEO";
    private static final String user = "user";
    private static final String form = "form";
    private static final String event = "event";
    private static final String listEvents = "listEvents";
    private static final String  reg = "reg";
    private static final int VERSION_BD = 10;

    private BDHelper nHelper;
    private final Context nContexto;
    private SQLiteDatabase nBD;

    private static class BDHelper extends SQLiteOpenHelper {

        public BDHelper(Context context) {
            super(context, BD, null, VERSION_BD);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub

            db.execSQL("CREATE TABLE " + user + "(" + idUser
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + nameUser
                    + " TEXT NOT NULL, " + codeUser + " TEXT NOT NULL);");

            db.execSQL("CREATE TABLE " + form + "(" + idForm
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + formJson
                    + " TEXT NOT NULL);");

            db.execSQL("CREATE TABLE " + event + "(" + idEvent
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + eventJson
                    + " TEXT NOT NULL);");

            db.execSQL("CREATE TABLE " + reg + "(" + idReg
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + regJson
                    + " TEXT NOT NULL);");

            db.execSQL("CREATE TABLE " + listEvents + "(" + idListEvents
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + listEventsJson
                    + " TEXT NOT NULL);");


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

            db.execSQL("DROP TABLE IF EXISTS " + user);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + form);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + event);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + reg);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + listEvents);
            onCreate(db);

        }

    }

    public bd(Context c) {

        nContexto = c;

    }

    public bd abrir() throws Exception {

        nHelper = new BDHelper(nContexto);
        nBD = nHelper.getWritableDatabase();
        return this;

    }

    public void cerrar() {
        // TODO Auto-generated method stub

        nHelper.close();

    }

    //User
    public Cursor searchUser(String nameUser) throws SQLException {

        String selectQuery = "SELECT * FROM " + user + " WHERE "
                + this.nameUser + "='" + nameUser + "'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor login(String nameUser, String codeUser) throws SQLException {

        String selectQuery = "SELECT * FROM " + user + " WHERE " + this.nameUser
                + "='" + nameUser + "'" + " AND " + this.codeUser
                + "='" + codeUser + "'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long createUser(String nameUser, String codeUser)
            throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(this.nameUser, nameUser);
        cv.put(this.codeUser, codeUser);
        return nBD.insert(user, null, cv);

    }

    public void updateUser(String nameUser, String codeUser) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(this.codeUser, codeUser);
        nBD.update(user, cv, this.nameUser + "='" + nameUser + "'", null);

    }

    //listEvents
    public String  searchListEvents(long id) throws SQLException {

        String events = null;
        String selectQuery = "SELECT * FROM " + listEvents + " WHERE "
                + this.idListEvents + "='" + id + "'";
        Log.w("query", selectQuery);

        try {

            Cursor cursor = nBD.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            events = cursor.getString(1);

        } catch (Exception e) {
            e.printStackTrace();

        }


        return events;
    }

    public long createListEvents(String path)
            throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(listEventsJson, path);

        return nBD.insert(listEvents, null, cv);

    }

    public void updateListEvents(int id, String listEventsJson) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(this.listEventsJson, listEventsJson);
        nBD.update(listEvents, cv, this.idListEvents + "='" + id + "'", null);

    }

}