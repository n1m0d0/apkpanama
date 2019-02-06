package com.example.john_pc.prueba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class bd {

    // TABLA USUARIO
    private static final String idUser = "_id";
    private static final String nameUser = "nameUser";
    private static final String codeUser = "codeUser";

    // TABLA DE FORMULARIOS
    private static final String idForm = "_id";
    private static final String formJson = "formJson";

    // TABLA EVENTOS
    private static final String idEvent = "_id";
    private static final String eventJson = "eventJson";

    // TABLA REGISTRO
    private static final String idReg = "_id";
    private static final String regJson = "regJson";


    // BASE DE DATOS TABLAS
    private static final String BD = "BD_SISFORM";
    private static final String user = "user";
    private static final String form = "form";
    private static final String event = "event";
    private static final String  reg = "reg";
    private static final int VERSION_BD = 1;

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
                    + " TEXT NOT NULL, " + codeUser + " TEXT NOT NULL) ;");

            db.execSQL("CREATE TABLE " + form + "(" + idForm
                    + " INTEGER PRIMARY KEY NOT NULL, " + formJson
                    + " TEXT NOT NULL) ;");

            db.execSQL("CREATE TABLE " + event + "(" + idEvent
                    + " INTEGER PRIMARY KEY NOT NULL, " + eventJson
                    + " TEXT NOT NULL) ;");

            db.execSQL("CREATE TABLE " + reg + "(" + idReg
                    + " INTEGER PRIMARY KEY NOT NULL, " + regJson
                    + " TEXT NOT NULL) ;");


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

    // User
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

    /*public void modificar_tipos_u(String id_tipo_u, String nom_tipo_u,
                                  String est_tipo_u, String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(NOM_TIPO_U, nom_tipo_u);
        cv.put(EST_TIPO_U, est_tipo_u);
        cv.put(FECHA_MOD_TIPO_U, fecha_mod);
        nBD.update(TIPOS_U, cv, ID_TIPO_U + "='" + id_tipo_u + "'", null);

    }

    // USUARIOS
    /*public Cursor all_usuarios() throws SQLException {

        String selectQuery = "SELECT * FROM " + USUARIOS;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor buscar_usuario(String cod_usuario, String pass_usuario)
            throws SQLException {

        String selectQuery = "SELECT * FROM " + USUARIOS + " WHERE "
                + COD_USUARIO + "='" + cod_usuario + "' and " + PASS_USUARIO
                + "='" + pass_usuario + "' and " + EST_USUARIO + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor buscar_usuario2(String cod_usuario, String pass_usuario)
            throws SQLException {

        String selectQuery = "SELECT * FROM " + USUARIOS + " WHERE "
                + COD_USUARIO + "='" + cod_usuario + "'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor buscar_usuario3(String id_usuario) throws SQLException {

        String selectQuery = "SELECT * FROM " + USUARIOS + " WHERE "
                + ID_USUARIO + "='" + id_usuario + "'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor usuarios_activos() throws SQLException {

        String selectQuery = "SELECT * FROM " + USUARIOS + " WHERE "
                + EST_USUARIO + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long registrar_usuario(String id_usuario, String tipo_u_id,
                                  String cod_usuario, String pass_usuario, String est_usuario,
                                  String fecha_reg, String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(ID_USUARIO, id_usuario);
        cv.put(TIPO_U_ID_U, tipo_u_id);
        cv.put(COD_USUARIO, cod_usuario);
        cv.put(PASS_USUARIO, pass_usuario);
        cv.put(EST_USUARIO, est_usuario);
        cv.put(FECHA_REG_USUARIO, fecha_reg);
        cv.put(FECHA_MOD_USUARIO, fecha_mod);
        return nBD.insert(USUARIOS, null, cv);

    }

    public void modificar_usuario(String id_usuario, String tipo_u_id,
                                  String cod_usuario, String pass_usuario, String est_usuario,
                                  String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(TIPO_U_ID_U, tipo_u_id);
        cv.put(COD_USUARIO, cod_usuario);
        cv.put(PASS_USUARIO, pass_usuario);
        cv.put(EST_USUARIO, est_usuario);
        cv.put(FECHA_MOD_USUARIO, fecha_mod);
        nBD.update(USUARIOS, cv, ID_USUARIO + "='" + id_usuario + "'", null);

    }

    // BUSES
    public Cursor all_buses() throws SQLException {

        String selectQuery = "SELECT * FROM " + BUSES;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor buses_activos() throws SQLException {

        String selectQuery = "SELECT * FROM " + BUSES + " WHERE " + EST_BUS
                + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long registrar_bus(String id_bus, String nom_bus, String est_bus,
                              String fecha_reg, String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(ID_BUS, id_bus);
        cv.put(NOM_BUS, nom_bus);
        cv.put(EST_BUS, est_bus);
        cv.put(FECHA_REG_BUS, fecha_reg);
        cv.put(FECHA_MOD_BUS, fecha_mod);
        return nBD.insert(BUSES, null, cv);

    }

    public void modificar_bus(String id_bus, String nom_bus, String est_bus,
                              String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(NOM_BUS, nom_bus);
        cv.put(EST_BUS, est_bus);
        cv.put(FECHA_MOD_BUS, fecha_mod);
        nBD.update(BUSES, cv, ID_BUS + "='" + id_bus + "'", null);

    }

    // RUTAS
    public Cursor all_rutas() throws SQLException {

        String selectQuery = "SELECT * FROM " + RUTAS;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor rutas_activas() throws SQLException {

        String selectQuery = "SELECT * FROM " + RUTAS + " WHERE " + EST_RUTA
                + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long registrar_ruta(String id_ruta, String nom_ruta,
                               String est_ruta, String fecha_reg, String fecha_mod)
            throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(ID_RUTA, id_ruta);
        cv.put(NOM_RUTA, nom_ruta);
        cv.put(EST_RUTA, est_ruta);
        cv.put(FECHA_REG_RUTA, fecha_reg);
        cv.put(FECHA_MOD_RUTA, fecha_mod);
        return nBD.insert(RUTAS, null, cv);

    }

    public void modificar_parada(String id_ruta, String nom_ruta,
                                 String est_ruta, String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(NOM_RUTA, nom_ruta);
        cv.put(EST_RUTA, est_ruta);
        cv.put(FECHA_MOD_RUTA, fecha_mod);
        nBD.update(RUTAS, cv, ID_RUTA + "='" + id_ruta + "'", null);

    }

    // PARADAS
    public Cursor all_paradas() throws SQLException {

        String selectQuery = "SELECT * FROM " + PARADAS;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor paradas_activas() throws SQLException {

        String selectQuery = "SELECT * FROM " + PARADAS + " WHERE "
                + EST_PARADA + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor paradas_por_ruta(String id_ruta) throws SQLException {

        String selectQuery = "SELECT * FROM " + PARADAS + " WHERE "
                + EST_PARADA + "='ACTIVO' AND " + RUTA_ID_P + " = '" + id_ruta
                + "'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long registrar_parada(String id_parada, String nom_parada,
                                 String id_ruta, String est_parada, String fecha_reg,
                                 String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(ID_PARADA, id_parada);
        cv.put(NOM_PARADA, nom_parada);
        cv.put(RUTA_ID_P, id_ruta);
        cv.put(EST_PARADA, est_parada);
        cv.put(FECHA_REG_PARADA, fecha_reg);
        cv.put(FECHA_MOD_PARADA, fecha_mod);
        return nBD.insert(PARADAS, null, cv);

    }

    public void modificar_parada(String id_parada, String nom_parada,
                                 String id_ruta, String est_parada, String fecha_mod)
            throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(NOM_PARADA, nom_parada);
        cv.put(RUTA_ID_P, id_ruta);
        cv.put(EST_PARADA, est_parada);
        cv.put(FECHA_MOD_PARADA, fecha_mod);
        nBD.update(PARADAS, cv, ID_PARADA + "='" + id_parada + "'", null);

    }

    // FORMULARIOS
    public Cursor all_formularios() throws SQLException {

        String selectQuery = "SELECT * FROM " + FORMULARIOS;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor formularios_activos() throws SQLException {

        String selectQuery = "SELECT * FROM " + FORMULARIOS + " WHERE "
                + EST_FORMULARIO + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long registrar_formulario(String id_formulario,
                                     String nom_formulario, String est_formulario, String fecha_reg,
                                     String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(ID_FORMULARIO, id_formulario);
        cv.put(NOM_FORMULARIO, nom_formulario);
        cv.put(EST_FORMULARIO, est_formulario);
        cv.put(FECHA_REG_FORMULARIO, fecha_reg);
        cv.put(FECHA_MOD_FORMULARIO, fecha_mod);
        return nBD.insert(FORMULARIOS, null, cv);

    }

    public void modificar_formulario(String id_formulario,
                                     String nom_formulario, String est_formulario, String fecha_mod)
            throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(NOM_FORMULARIO, nom_formulario);
        cv.put(EST_FORMULARIO, est_formulario);
        cv.put(FECHA_MOD_FORMULARIO, fecha_mod);
        nBD.update(FORMULARIOS, cv, ID_FORMULARIO + "='" + id_formulario + "'",
                null);

    }

    // TIPOS_P
    public Cursor all_tipos_p() throws SQLException {

        String selectQuery = "SELECT * FROM " + TIPOS_P;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor tipos_p_activos() throws SQLException {

        String selectQuery = "SELECT * FROM " + TIPOS_P + " WHERE "
                + EST_TIPO_P + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long registrar_tipo_p(String id_tipo_p, String nom_tipo_p,
                                 String est_tipo_p, String fecha_reg, String fecha_mod)
            throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(ID_TIPO_P, id_tipo_p);
        cv.put(NOM_TIPO_P, nom_tipo_p);
        cv.put(EST_TIPO_P, est_tipo_p);
        cv.put(FECHA_REG_TIPO_P, fecha_reg);
        cv.put(FECHA_MOD_TIPO_P, fecha_mod);
        return nBD.insert(TIPOS_P, null, cv);

    }

    public void modificar_tipo_p(String id_tipo_p, String nom_tipo_p,
                                 String est_tipo_p, String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(NOM_TIPO_P, nom_tipo_p);
        cv.put(EST_TIPO_P, est_tipo_p);
        cv.put(FECHA_MOD_TIPO_P, fecha_mod);
        nBD.update(TIPOS_P, cv, ID_TIPO_P + "='" + id_tipo_p + "'", null);

    }

    // PREGUNTAS
    public Cursor all_preguntas() throws SQLException {

        String selectQuery = "SELECT * FROM " + PREGUNTAS;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor preguntas_activas() throws SQLException {

        String selectQuery = "SELECT * FROM " + PREGUNTAS + " WHERE "
                + EST_PREGUNTA + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long registrar_preguntas(String id_pregunta, String glosa_pregunta,
                                    String nom_pregunta, String tipo_p_id, String formulario_id,
                                    String des_pregunta, String est_pregunta, String fecha_reg,
                                    String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(ID_PREGUNTA, id_pregunta);
        cv.put(GLOSA_PREGUNTA, glosa_pregunta);
        cv.put(NOM_PREGUNTA, nom_pregunta);
        cv.put(TIPO_P_ID_P, tipo_p_id);
        cv.put(FORMULARIO_ID_P, formulario_id);
        cv.put(DES_PREGUNTA, des_pregunta);
        cv.put(EST_PREGUNTA, est_pregunta);
        cv.put(FECHA_REG_PREGUNTA, fecha_reg);
        cv.put(FECHA_MOD_PREGUNTA, fecha_mod);
        return nBD.insert(PREGUNTAS, null, cv);

    }

    public void modificar_preguntas(String id_pregunta, String glosa_pregunta,
                                    String nom_pregunta, String tipo_p_id, String formulario_id,
                                    String des_pregunta, String est_pregunta, String fecha_mod)
            throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(GLOSA_PREGUNTA, glosa_pregunta);
        cv.put(NOM_PREGUNTA, nom_pregunta);
        cv.put(TIPO_P_ID_P, tipo_p_id);
        cv.put(FORMULARIO_ID_P, formulario_id);
        cv.put(DES_PREGUNTA, des_pregunta);
        cv.put(EST_PREGUNTA, est_pregunta);
        cv.put(FECHA_MOD_PREGUNTA, fecha_mod);
        nBD.update(PREGUNTAS, cv, ID_PREGUNTA + "='" + id_pregunta + "'", null);

    }

    // OPCIONES
    public Cursor all_opciones() throws SQLException {

        String selectQuery = "SELECT * FROM " + OPCIONES;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor opciones_activas() throws SQLException {

        String selectQuery = "SELECT * FROM " + OPCIONES + " WHERE "
                + EST_OPCION + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long registrar_opciones(String id_opcion, String glosa_opcion,
                                   String nom_opcion, String pregunta_id, String est_opcion,
                                   String fecha_reg, String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(ID_OPCION, id_opcion);
        cv.put(GLOSA_OPCION, glosa_opcion);
        cv.put(NOM_OPCION, nom_opcion);
        cv.put(PREGUNTA_ID_O, pregunta_id);
        cv.put(EST_OPCION, est_opcion);
        cv.put(FECHA_REG_OPCION, fecha_reg);
        cv.put(FECHA_MOD_OPCION, fecha_mod);
        return nBD.insert(OPCIONES, null, cv);

    }

    public void modificar_opciones(String id_opcion, String glosa_opcion,
                                   String nom_opcion, String pregunta_id, String est_opcion,
                                   String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(GLOSA_OPCION, glosa_opcion);
        cv.put(NOM_OPCION, nom_opcion);
        cv.put(PREGUNTA_ID_O, pregunta_id);
        cv.put(EST_OPCION, est_opcion);
        cv.put(FECHA_MOD_OPCION, fecha_mod);
        nBD.update(OPCIONES, cv, ID_OPCION + "='" + id_opcion + "'", null);

    }

    // REGISTROS
    public Cursor all_registros() throws SQLException {

        String selectQuery = "SELECT * FROM " + REGISTROS;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor registros_activos() throws SQLException {

        String selectQuery = "SELECT * FROM " + REGISTROS + " WHERE "
                + EST_REGISTRO + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long registrar_registros(String cod_registro, String parada_id,
                                    String bus_id, String usuario_id, String fecha_reg, String fecha_mod)
            throws SQLException {
        // TODO Auto-generated method stub

        String est_registro = "ACTIVO";

        ContentValues cv = new ContentValues();
        cv.put(COD_REGISTRO, cod_registro);
        cv.put(PARADA_ID_R, parada_id);
        cv.put(BUS_ID_R, bus_id);
        cv.put(USUARIO_ID_R, usuario_id);
        cv.put(EST_REGISTRO, est_registro);
        cv.put(FECHA_REG_REGISTRO, fecha_reg);
        cv.put(FECHA_MOD_REGISTRO, fecha_mod);
        return nBD.insert(REGISTROS, null, cv);

    }

    public void modificar_registros(String id_registro, String cod_registro,
                                    String parada_id, String bus_id, String usuario_id,
                                    String est_registro, String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(COD_REGISTRO, cod_registro);
        cv.put(PARADA_ID_R, parada_id);
        cv.put(BUS_ID_R, bus_id);
        cv.put(USUARIO_ID_R, usuario_id);
        cv.put(EST_REGISTRO, est_registro);
        cv.put(FECHA_MOD_REGISTRO, fecha_mod);
        nBD.update(REGISTROS, cv, ID_REGISTRO + "='" + id_registro + "'", null);

    }

    public JSONObject json_registros_no_enviados() throws SQLException {

        String selectQuery = "SELECT * FROM " + REGISTROS + " WHERE "
                + EST_REGISTRO + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();

        if (cursor.moveToFirst() == false) {

            return null;

        } else {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {

                obj = new JSONObject();
                try {

                    obj.put("ID_REGISTRO", cursor.getString(0));
                    obj.put("COD_REGISTRO", cursor.getString(1));
                    obj.put("ID_PARADA", cursor.getString(2));
                    obj.put("ID_BUS", cursor.getString(3));
                    obj.put("ID_USUARIO", cursor.getString(4));
                    obj.put("EST_REGISTRO", cursor.getString(5));
                    obj.put("FECHA_REG", cursor.getString(6));
                    obj.put("FECHA_MOD", cursor.getString(7));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(obj);
            }
            JSONObject finalobject = new JSONObject();
            try {
                finalobject.put("data", jsonArray);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return finalobject;

        }

    }

    public void enviar_registros(String id_registro) throws SQLException {
        // TODO Auto-generated method stub

        String est_registro = "ENVIADO";
        ContentValues cv = new ContentValues();
        cv.put(EST_REGISTRO, est_registro);
        nBD.update(REGISTROS, cv, ID_REGISTRO + "='" + id_registro + "'", null);

    }

    // RESPUESTAS
    public Cursor all_respuestas() throws SQLException {

        String selectQuery = "SELECT * FROM " + RESPUESTAS;
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public Cursor respuestas_activas() throws SQLException {

        String selectQuery = "SELECT * FROM " + RESPUESTAS + " WHERE "
                + EST_RESPUESTA + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long registrar_respuestas(String registro_cod, int opcion_id,
                                     String obs_respuesta, String fecha_reg, String fecha_mod)
            throws SQLException {
        // TODO Auto-generated method stub

        String est_respuesta = "ACTIVO";

        ContentValues cv = new ContentValues();
        cv.put(REGISTRO_COD_R, registro_cod);
        cv.put(OPCION_ID_R, opcion_id);
        cv.put(OBS_RESPUESTA, obs_respuesta);
        cv.put(EST_RESPUESTA, est_respuesta);
        cv.put(FECHA_REG_RESPUESTA, fecha_reg);
        cv.put(FECHA_MOD_RESPUESTA, fecha_mod);
        return nBD.insert(RESPUESTAS, null, cv);

    }

    public void modificar_respuestas(String id_respuesta, String registro_cod,
                                     String opcion_id, String obs_respuesta, String est_respuesta,
                                     String fecha_reg, String fecha_mod) throws SQLException {
        // TODO Auto-generated method stub

        ContentValues cv = new ContentValues();
        cv.put(REGISTRO_COD_R, registro_cod);
        cv.put(OPCION_ID_R, opcion_id);
        cv.put(OBS_RESPUESTA, obs_respuesta);
        cv.put(EST_RESPUESTA, est_respuesta);
        cv.put(FECHA_MOD_RESPUESTA, fecha_mod);
        nBD.update(RESPUESTAS, cv, ID_RESPUESTA + "='" + id_respuesta + "'",
                null);

    }

    public JSONObject json_respuestas_no_enviadas() throws SQLException {

        String selectQuery = "SELECT * FROM " + RESPUESTAS + " WHERE "
                + EST_RESPUESTA + "='ACTIVO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();

        if (cursor.moveToFirst() == false) {

            return null;

        } else {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {

                obj = new JSONObject();
                try {

                    obj.put("ID_RESPUESTA", cursor.getString(0));
                    obj.put("COD_REGISTRO", cursor.getString(1));
                    obj.put("ID_OPCION", cursor.getString(2));
                    obj.put("OBS_RESPUESTA", cursor.getString(3));
                    obj.put("EST_RESPUESTA", cursor.getString(4));
                    obj.put("FECHA_REG", cursor.getString(5));
                    obj.put("FECHA_MOD", cursor.getString(6));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(obj);
            }
            JSONObject finalobject = new JSONObject();
            try {
                finalobject.put("data", jsonArray);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return finalobject;

        }

    }

    public void enviar_respuestas(String id_respuesta) throws SQLException {
        // TODO Auto-generated method stub

        String est_respuesta = "ENVIADO";
        ContentValues cv = new ContentValues();
        cv.put(EST_RESPUESTA, est_respuesta);
        nBD.update(RESPUESTAS, cv, ID_RESPUESTA + "='" + id_respuesta + "'",
                null);

    }

    // SESIONES
    public Cursor verificar_sesion() throws SQLException {

        String selectQuery = "SELECT * FROM " + SESIONES + " WHERE "
                + EST_SESION + "='ABIERTO'";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    public long inicio_sesion(String usuario_id, String fecha_inicio)
            throws SQLException {
        // TODO Auto-generated method stub

        String est_sesion = "ABIERTO";
        ContentValues cv = new ContentValues();
        cv.put(USUARIO_ID, usuario_id);
        cv.put(EST_SESION, est_sesion);
        cv.put(FECHA_INICIO, fecha_inicio);
        cv.put(FECHA_FIN, fecha_inicio);
        return nBD.insert(SESIONES, null, cv);

    }

    public void cierre_sesion(String id_sesion, String fecha_fin)
            throws SQLException {
        // TODO Auto-generated method stub

        String est_sesion = "CERRADO";
        ContentValues cv = new ContentValues();
        cv.put(FECHA_FIN, fecha_fin);
        cv.put(EST_SESION, est_sesion);
        nBD.update(SESIONES, cv, ID_SESION + "='" + id_sesion + "'", null);

    }

    // consultas

    // preguntas_x_formulario
    public Cursor all_preguntas_f(String id_formulario) throws SQLException {

        String selectQuery = "SELECT * FROM " + PREGUNTAS + " WHERE "
                + FORMULARIO_ID_P + "='" + id_formulario + "' AND " + EST_PREGUNTA + "='ACTIVO'" + " ORDER BY " + GLOSA_PREGUNTA + " ASC";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }

    // opciones_x_pregunta
    public Cursor all_opciones_p(String id_pregunta) throws SQLException {

        String selectQuery = "SELECT * FROM " + OPCIONES + " WHERE "
                + PREGUNTA_ID_O + "='" + id_pregunta + "' AND " + EST_OPCION + "='ACTIVO'" + " ORDER BY " + GLOSA_OPCION + " ASC";
        Cursor cursor = nBD.rawQuery(selectQuery, null);
        return cursor;

    }*/

}