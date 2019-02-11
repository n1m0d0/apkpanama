package com.example.john_pc.prueba;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class events extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {

    EditText etSearch;
    ListView lvEvents;
    FloatingActionButton fbAdd;
    String id_events;
    ArrayList<obj_events> itemEvents = new ArrayList<obj_events>();
    ArrayList<obj_events> itemEvents2 = new ArrayList<obj_events>();
    Toast msj;
    String auth;
    String userName;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonArrayRequest mJsonArrayRequest;
    String url = "https://test.portcolon2000.site/api/lastEvents";
    Intent ir;
    int idForm;
    int idEvent;
    String keyValue;
    String dateEventBegin;
    String dateEventEnd;
    int personNumber;
    int containerNumber;
    int eventState;
    String colorForm;
    String idIconForm;
    adapter_events adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lvEvents = findViewById(R.id.lvEvents);
        fbAdd = findViewById(R.id.fbAdd);
        etSearch = findViewById(R.id.etSearch);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");

        //funcionn para llenar el array de itemEvents y mostrarlo en el ListView
        /*msj = Toast.makeText(this, auth + " " + userName, Toast.LENGTH_LONG);
        msj.show();*/

        if(compruebaConexion(this)) {

            cargarEventos();

        } else {

            cargarEventosOffline();

        }

        //funcionalidad a la lista
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                obj_events elegido = (obj_events) parent.getItemAtPosition(position);

                // recuperamos el id
                id_events = "" + elegido.getId();
                /*msj = Toast.makeText(events.this, "Evento elegido: " + id_events, Toast.LENGTH_LONG);
                msj.show();*/
                // llamar a la funcion para ver el evento
                ir = new Intent(events.this, view_event.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("idEvent", id_events);
                startActivity(ir);
                finish();


            }
        });

        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ir = new Intent(events.this, forms.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                startActivity(ir);
                finish();

            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchItem("" + s);
                Log.w("buscar", "" + s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void cargarEventos(){

        mProgressDialog =  new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null, this, this){

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", auth); //authentication
                return headers;
            }

        };

        mRequestQueue.add(mJsonArrayRequest);

    }

    @Override
    public void onResponse(JSONArray response) {

        /*msj = Toast.makeText(this, "" + response, Toast.LENGTH_LONG);
        msj.show();*/
        Log.w("respuesta", "" + response);

        mProgressDialog.dismiss();

        try {

            bd conexion = new bd(this);
            String events = conexion.searchListEvents(1);
            if (events == null) {

                String path = createJson(response);
                Log.w("path000", path);
                conexion.createListEvents(path);

            }
            else {

                String path = createJson(response);
                conexion.updateListEvents(1, path);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        try{

            for(int i=0;i<response.length();i++) {

                JSONObject event = response.getJSONObject(i);

                idForm = event.getInt("idForm");
                idEvent = event.getInt("idEvent");
                keyValue = event.getString("keyValue");
                dateEventBegin = event.getString("dateEventBegin");
                dateEventEnd = event.getString("dateEventEnd");
                personNumber = event.getInt("personNumber");
                containerNumber = event.getInt("containerNumber");
                eventState = event.getInt("eventState");
                colorForm = event.getString("colorForm");
                idIconForm = event.getString("idIconForm");

                itemEvents.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm, personNumber, containerNumber, eventState, colorForm, idIconForm));
                itemEvents2.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm, personNumber, containerNumber, eventState, colorForm, idIconForm));

            }

            adapter = new adapter_events(events.this, itemEvents);
            lvEvents.setAdapter(adapter);

        }catch (JSONException e) {

            e.printStackTrace();
            msj = Toast.makeText(this, "No se tiene datos registrados", Toast.LENGTH_LONG);
            msj.show();

        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.dismiss();
        /*msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();*/
        Log.w("respuesta", "" + error);

    }

    public void searchItem(String texzo) {

        Log.w("buscar", texzo);
        itemEvents.clear();
        for(int i = 0; i < itemEvents2.size(); i++) {

            if(itemEvents2.get(i).getVariable().toLowerCase().contains(texzo.toLowerCase())) {

                itemEvents.add(itemEvents2.get(i));

            }

        }

        adapter.notifyDataSetChanged();

    }

    public void cargarEventosOffline() {

        try {
            Log.w("path", "estoy aqui");
            bd conexion = new bd(this);
            String path = conexion.searchListEvents(1);

            Log.w("path", "" + path);
            String events = readJsonFile(path);
            if (events == null) {

                msj = Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_LONG);
                msj.setGravity(Gravity.CENTER, 0, 0);
                msj.show();

            } else {

                try{

                    JSONArray response = new JSONArray(events);

                    for(int i=0;i<response.length();i++) {

                        JSONObject event = response.getJSONObject(i);

                        idForm = event.getInt("idForm");
                        idEvent = event.getInt("idEvent");
                        keyValue = event.getString("keyValue");
                        dateEventBegin = event.getString("dateEventBegin");
                        dateEventEnd = event.getString("dateEventEnd");
                        personNumber = event.getInt("personNumber");
                        containerNumber = event.getInt("containerNumber");
                        eventState = event.getInt("eventState");
                        colorForm = event.getString("colorForm");
                        idIconForm = event.getString("idIconForm");

                        itemEvents.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm, personNumber, containerNumber, eventState, colorForm, idIconForm));
                        itemEvents2.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm, personNumber, containerNumber, eventState, colorForm, idIconForm));

                    }

                    adapter = new adapter_events(events.this, itemEvents);
                    lvEvents.setAdapter(adapter);

                }catch (JSONException e) {

                    e.printStackTrace();

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static boolean compruebaConexion(Context context) {

        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    public String createJson(JSONArray jsonArray) {

        String path = null;
        String carpeta = "geoport";
        File fileJson = new File(Environment.getExternalStorageDirectory(), carpeta);
        boolean isCreada = fileJson.exists();
        String nombreJson = "";

        if(isCreada == false) {

            isCreada = fileJson.mkdir();

        }

        if(isCreada == true) {

            nombreJson = "events.json";

        }

        path = Environment.getExternalStorageDirectory() + File.separator + carpeta + File.separator + nombreJson;


        try {
            FileWriter writer = new FileWriter(path);
            writer.write(String.valueOf(jsonArray));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return path;
    }

    public String readJsonFile (String path) {

        Log.w("ver", path);

        String jsonevents = null;

        String json = null;
        try {
            File f = new File(path);
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(f)));

            jsonevents = fin.readLine();
            fin.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonevents;
    }

}
