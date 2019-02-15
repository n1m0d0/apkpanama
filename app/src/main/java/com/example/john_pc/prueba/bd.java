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
    // TABLA REGISTRO
    private static final String idAnswers= "_id";
    private static final String nameUserAnswers = "nameUserAnswers";
    private static final String certificateAnswers = "certificateAnswers";
    private static final String answersJson = "answersJson";
    private static final String stateAnswers = "stateAnswers";
    // TABLA SESEION


    // BASE DE DATOS TABLAS
    private static final String BD = "BD_GEO";
    private static final String user = "user";
    private static final String  answers = "answers";
    private static final int VERSION_BD = 12;

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

            db.execSQL("CREATE TABLE " + answers + "(" + idAnswers
                    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + answersJson
                    + " TEXT NOT NULL, " + nameUserAnswers
                    + " TEXT NOT NULL, " + certificateAnswers
                    + " TEXT NOT NULL, "  + stateAnswers + " TEXT NOT NULL);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

            db.execSQL("DROP TABLE IF EXISTS " + user);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS " + answers);
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

    //Answers
    public long createAnswers(String name, String auth, String path)
            throws SQLException {
        // TODO Auto-generated method stub

        String state = "ACTIVO";
        ContentValues cv = new ContentValues();
        cv.put(nameUserAnswers, name);
        cv.put(certificateAnswers, auth);
        cv.put(answersJson, path);
        cv.put(stateAnswers, state);
        return nBD.insert(answers, null, cv);

    }

    public Cursor searchActive() throws SQLException {

        String selectQuery = "SELECT * FROM " + answers + " WHERE " + stateAnswers
                + " = 'ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public void updateAnswers(String id)
            throws SQLException {
        // TODO Auto-generated method stub

        String state = "DESACTIVADO";
        ContentValues cv = new ContentValues();
        cv.put(stateAnswers, state);
        nBD.update(answers, cv, idAnswers + "='" + id + "'", null);

    }

}