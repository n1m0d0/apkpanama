package com.example.john_pc.prueba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

public class forms extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {

    GridView gvForms;
    String id_form;
    ArrayList<obj_form> itemForms = new ArrayList<obj_form>();
    Toast msj;
    String auth;
    String userName;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonArrayRequest mJsonArrayRequest;
    String url = "https://test.portcolon2000.site/api/parForm";
    Intent ir;

    int idForm;
    String colorForm;
    String descriptionForm;
    String idIconForm;
    int positionForm;
    int typeDependency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);

        gvForms = findViewById(R.id.gvForms);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");

        cargarFormularios();

        //funcionalidad a la lista
        gvForms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                obj_form elegido = (obj_form) parent.getItemAtPosition(position);

                // recuperamos el id
                id_form = "" + elegido.getId();
                msj = Toast.makeText(forms.this, "Formulario elegido elegido: " + id_form, Toast.LENGTH_LONG);
                msj.show();
                // llamar a la funcion para ver el formulario
                ir = new Intent(forms.this, form_event.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("idForm", id_form);
                startActivity(ir);


            }
        });

    }

    private void cargarFormularios(){

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
        mProgressDialog.dismiss();

        try{

            for(int i=0;i<response.length();i++) {

                JSONObject form = response.getJSONObject(i);

                idForm = form.getInt("idForm");
                colorForm = form.getString("colorForm");
                descriptionForm = form.getString("descriptionForm");
                idIconForm = form.getString("idIconForm");
                positionForm = form.getInt("positionForm");
                typeDependency = form.getInt("typeDependency");

                itemForms.add(new obj_form(idForm, colorForm, descriptionForm, idIconForm, positionForm, typeDependency));

            }

            adapter_forms adapter = new adapter_forms(forms.this, itemForms);
            gvForms.setAdapter(adapter);

        }catch (JSONException e) {

            e.printStackTrace();
            msj = Toast.makeText(this, "" + e, Toast.LENGTH_LONG);
            msj.show();

            Log.w("respuesta", "" + e);

        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.dismiss();
        msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();

        Log.w("respuesta", "" + error);

    }

}
