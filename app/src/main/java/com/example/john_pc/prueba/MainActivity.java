package com.example.john_pc.prueba;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etUser, etPassword;
    TextView btnLogin, btnForgotPassword;
    Toast msj;
    Intent ir;
    ImageView logo;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonObjectRequest mJsonObjectRequest;
    String url = "https://test.portcolon2000.site/api/authUser";
    String credentials;
    String auth;
    int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        logo = findViewById(R.id.img);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(this);

    }



    public void logo(View v) {

                Uri uri = Uri.parse("https://www.portcolon2000.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

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

                        String password = encryptPassword(etPassword.getText().toString().trim());

                        credentials = etUser.getText().toString().trim()+":"+password;
                        auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                        if(compruebaConexion(this)) {

                            cargarLogin();

                        } else {

                            msj = Toast.makeText(this, "Sin conexion a Internet", Toast.LENGTH_LONG);
                            msj.setGravity(Gravity.CENTER, 0, 0);
                            msj.show();

                            try {

                                bd conexion = new bd(this);
                                conexion.abrir();
                                Cursor usuario = conexion.login(etUser.getText().toString().trim(), auth);
                                if (usuario.moveToFirst() == false) {

                                    msj = Toast.makeText(this, "Usuario o Clave incorrecto!!", Toast.LENGTH_LONG);
                                    msj.setGravity(Gravity.CENTER, 0, 0);
                                    msj.show();

                                } else {

                                    ir = new Intent(this, events.class);
                                    ir.putExtra("auth", auth);
                                    ir.putExtra("userName", etUser.getText().toString().trim());
                                    startActivity(ir);
                                    conexion.cerrar();

                                }

                            } catch (Exception e) {

                                e.printStackTrace();

                            }

                        }



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

        mProgressDialog.dismiss();
        msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();

    }

    @Override
    public void onResponse(JSONObject response) {

        mProgressDialog.dismiss();

        try {
            code = response.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(code == 0) {

            msj = Toast.makeText(this, "Bienvenido!!", Toast.LENGTH_LONG);
            msj.setGravity(Gravity.CENTER, 0, 0);
            msj.show();

            try {

                bd conexion = new bd(this);
                conexion.abrir();
                Cursor usuario = conexion.searchUser(etUser.getText().toString().trim());
                if (usuario.moveToFirst() == false) {

                    Log.w("aqui", "aqui");
                    conexion.createUser(etUser.getText().toString().trim(), auth);

                }
                else {
                    conexion.updateUser(etUser.getText().toString().trim(), auth);
                }
                conexion.cerrar();

            } catch (Exception e) {

                e.printStackTrace();

            }

            ir = new Intent(this, events.class);
            ir.putExtra("auth", auth);
            ir.putExtra("userName", etUser.getText().toString().trim());
            startActivity(ir);

        } else {

            msj = Toast.makeText(this, "Usuario o Clave incorrecto!!", Toast.LENGTH_LONG);
            msj.setGravity(Gravity.CENTER, 0, 0);
            msj.show();

        }

    }

    private static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
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

}
