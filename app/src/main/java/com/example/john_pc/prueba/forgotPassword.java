package com.example.john_pc.prueba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class forgotPassword extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etUser;
    Button btnSend;
    Toast msj;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonObjectRequest mJsonObjectRequest;
    String url = "https://test.portcolon2000.site/api/forgotPass";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etUser = findViewById(R.id.etUser);

        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        /*Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName"); */

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnSend:

                if (etUser.getText().toString().trim().equalsIgnoreCase("")) {

                    msj = Toast.makeText(this, "debe coompletar los datos", Toast.LENGTH_LONG);
                    msj.show();

                }else {

                    if (!validarEmail(etUser.getText().toString())) {

                        msj = Toast.makeText(this,"la direcion de correo no es valida", Toast.LENGTH_LONG);
                        msj.show();

                    }
                    else {

                        //llamar a la funcion forgotPassword
                        enviarEmail();

                    }

                }

                break;

        }

    }

    private boolean validarEmail(String email) {

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();

    }

    private void enviarEmail() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, this, this) {

            /*@Override
            public Map getParams() {
                Map params = new HashMap();

                params.put("email", etUser.getText().toString().trim());

                return params;
            }*/

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("email", etUser.getText().toString().trim()); //correo
                return headers;
            }

        };

        mRequestQueue.add(mJsonObjectRequest);

    }

    @Override
    public void onResponse(JSONObject response) {

        msj = Toast.makeText(this, "" + response, Toast.LENGTH_LONG);
        msj.show();
        mProgressDialog.hide();

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.hide();
        msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();

    }
}
