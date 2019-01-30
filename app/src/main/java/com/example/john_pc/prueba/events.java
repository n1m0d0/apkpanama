package com.example.john_pc.prueba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class events extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {

    ListView lvEvents;
    FloatingActionButton fbAdd;
    String id_events;
    ArrayList<obj_events> itemEvents = new ArrayList<obj_events>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        lvEvents = findViewById(R.id.lvEvents);
        fbAdd = findViewById(R.id.fbAdd);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");

        //funcionn para llenar el array de itemEvents y mostrarlo en el ListView
        msj = Toast.makeText(this, auth + " " + userName, Toast.LENGTH_LONG);
        msj.show();

        cargarEventos();

        //funcionalidad a la lista
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                obj_events elegido = (obj_events) parent.getItemAtPosition(position);

                // recuperamos el id
                id_events = "" + elegido.getId();
                msj = Toast.makeText(events.this, "Evento elegido: " + id_events, Toast.LENGTH_LONG);
                msj.show();
                // llamar a la funcion para ver el evento
                ir = new Intent(events.this, view_event.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("idEvent", id_events);
                startActivity(ir);


            }
        });

        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ir = new Intent(events.this, forms.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                startActivity(ir);

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

        mProgressDialog.hide();

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

                itemEvents.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm, personNumber, containerNumber, eventState));

            }

            adapter_events adapter = new adapter_events(events.this, itemEvents);
            lvEvents.setAdapter(adapter);

        }catch (JSONException e) {

            e.printStackTrace();

        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.hide();
        msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();
        Log.w("respuesta", "" + error);

    }

}
