package com.example.john_pc.prueba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

public class form_event extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {

    String auth;
    String userName;
    String idForm;
    LinearLayout llContenedor;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonArrayRequest mJsonArrayRequest;
    String url = "https://test.portcolon2000.site/api/parFormFields/";
    Intent ir;
    Toast msj;
    ArrayList<EditText> editTexts = new ArrayList<EditText>();
    ArrayList<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    ArrayList<RadioGroup> radioGroups = new ArrayList<RadioGroup>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_event);

        llContenedor = findViewById(R.id.llContenedor);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        idForm = parametros.getString("idForm");

        msj = Toast.makeText(this, "idFrom: " + idForm, Toast.LENGTH_LONG);
        msj.show();

        url = url + idForm;

        cargarFormulario();

    }

    private void cargarFormulario(){

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

        try{

            for(int i=0;i<response.length();i++) {

                JSONObject form = response.getJSONObject(i);

                int idFile = form.getInt("IDFILE");
                String name = form.getString("NAME");
                String description = form.getString("DESCRIPTION");
                int position = form.getInt("POSITION");
                int type = form.getInt("TYPE");
                int idparameter = form.getInt("IDPARAMETER");
                int input_max = form.getInt("INPUT_MAX");
                int input_regx = form.getInt("INPUT_REGEX");
                int input_datemin = form.getInt("INPUT_DATEMIN");
                int input_datemax = form.getInt("INPUT_DATEMAX");
                int photo_resolution = form.getInt("PHOTO_RESOLUTION");
                int file_size = form.getInt("FILE_SIZE");
                int reg_begin = form.getInt("REG_BEGIN");
                int reg_end = form.getInt("REG_END");
                int is_mandatory = form.getInt("IS_MANDATORY");
                int is_keybaule = form.getInt("IS_KEYVALUE");

                creartextview(description);
                switch (type) {

                    case 1:
                        break;

                    case 2:
                        break;

                    case 3:
                        break;

                    default:
                        break;

                }

            }

        }catch (JSONException e) {

            e.printStackTrace();
            msj = Toast.makeText(this, "" + e, Toast.LENGTH_LONG);
            msj.show();

        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.hide();
        msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();

    }

    // crear textview en el contenedor

    public void creartextview(String texto) {

        TextView tv;
        tv = new TextView(this);
        tv.setText(texto);
        tv.getTextSize();
        tv.setTextColor(getResources().getColor(R.color.colorBlack));
        llContenedor.addView(tv);

    }

    // crear edittext en el contenedor

    public void crearedittext(int id_opcion, String opcion, int descripcion) {

        EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        et.setPadding(2, 2, 2, 2);
        et.setHint(opcion);
        et.setId(id_opcion);
        InputFilter[] ifet = new InputFilter[1];
        ifet[0] = new InputFilter.LengthFilter(descripcion);
        et.setFilters(ifet);
        llContenedor.addView(et);
        editTexts.add(et);

    }

    // crear radiobutton en el contenedor

    public void crearradiobutton(RadioGroup rg, int id_opcion, String opcion) {

        RadioButton rb = new RadioButton(this);
        rb.setText(opcion);
        rb.setTextColor(getResources().getColor(R.color.colorBlack));
        rb.setId(id_opcion);
        rg.addView(rb);

    }

    // crear checkbox en el contenedor

    public void crearcheckbox(int id_opcion, String opcion) {

        CheckBox cb = new CheckBox(this);
        cb.setText(opcion);
        cb.setTextColor(getResources().getColor(R.color.colorBlack));
        cb.setId(id_opcion);
        llContenedor.addView(cb);
        checkBoxs.add(cb);

    }

}
