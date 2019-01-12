package com.example.john_pc.prueba;

import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    String url = "https://test.portcolon2000.site/api/openEvent";
    int idEvent;
    int idEventDependency;
    String dateEvent;
    String posGeo;
    int idForm;
    JSONObject p;
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



        //funcionalidad a la lista
        /*lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                obj_events elegido = (obj_events) parent.getItemAtPosition(position);

                // recuperamos el id
                id_events = "" + elegido.getId();
                // llamar a la funcion para ver el evento


            }
        });*/

        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            cargarEventos();

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

        msj = Toast.makeText(this, "" + response, Toast.LENGTH_LONG);
        msj.show();
        mProgressDialog.hide();
        // Process the JSON
        try{
            // Loop through the array elements
            for(int i=0;i<response.length();i++) {
                // Get current json object
                JSONObject event = response.getJSONObject(i);

                // Get the current student (json object) data
                idEvent = event.getInt("idEvent");
                idEventDependency = event.getInt("idEventDependency");
                dateEvent = event.getString("dateEvent");
                posGeo = event.getString("posGeo");
                idForm = event.getInt("idForm");
                p = event.getJSONObject("p");

                for(int j=0;j<p.length();j++) {



                }


            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    /*@Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.hide();
        msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();

    }

    @Override
    public void onResponse(JSONObject response) {


        msj = Toast.makeText(this, "" + response, Toast.LENGTH_LONG);
        msj.show();
        mProgressDialog.hide();

    }*/
}
