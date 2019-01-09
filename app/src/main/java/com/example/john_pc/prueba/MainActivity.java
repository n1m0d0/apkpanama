package com.example.john_pc.prueba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etUser, etPassword;
    Button btnLogin, btnForgotPassword;
    Toast msj;
    Intent ir;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonObjectRequest mJsonObjectRequest;
    String url = "https://test.portcolon2000.site/api/authUser";
    String credentials;
    String auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnLogin:

                if (etUser.getText().toString().trim().equalsIgnoreCase("") || etPassword.getText().toString().trim().equalsIgnoreCase("")) {

                    msj = Toast.makeText(this, "debe coompletar los datos", Toast.LENGTH_LONG);
                    msj.show();
                }
                else {

                    if (!validarEmail(etUser.getText().toString())) {

                        msj = Toast.makeText(this,"la direcion de correo no es valida", Toast.LENGTH_LONG);
                        msj.show();

                    }
                    else {

                        //llamar la funcion de login

                        credentials = etUser.getText().toString().trim()+":"+etPassword.getText().toString().trim();
                        auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                        cargarLogin();

                    }

                }

                break;

            case R.id.btnForgotPassword:

                Intent ir = new Intent(this, forgotPassword.class);
                startActivity(ir);

                break;
        }

    }

    private boolean validarEmail(String email) {

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();

    }

    private void cargarLogin(){

        mProgressDialog =  new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url,null, this, this){

            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", auth); //authentication
                return headers;
            }

        };

        mRequestQueue.add(mJsonObjectRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.hide();
        msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();

    }

    @Override
    public void onResponse(JSONObject response) {

        msj = Toast.makeText(this, "Bienvenido!!" + response, Toast.LENGTH_LONG);
        msj.show();
        mProgressDialog.hide();

        ir = new Intent(this, events.class);
        ir.putExtra("auth", auth);
        ir.putExtra("userName", etUser.getText().toString().trim());
        startActivity(ir);

    }

}
