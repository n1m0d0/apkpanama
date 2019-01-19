package com.example.john_pc.prueba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
    String urlParametros = "https://test.portcolon2000.site/api/parGeneral/";
    String urlParametros2;
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

        /*msj = Toast.makeText(this, "" + response, Toast.LENGTH_LONG);
        msj.show();*/
        mProgressDialog.hide();

        try{

            for(int i=0;i<response.length();i++) {

                JSONObject form = response.getJSONObject(i);

                int idField = form.getInt("IDFIELD");
                String name = form.getString("NAME");
                String description = form.getString("DESCRIPTION");
                int position = form.getInt("POSITION");
                int type = form.getInt("TYPE");
                int idparameter = form.getInt("IDPARAMETER");
                int input_max = form.getInt("INPUT_MAX");
                String input_regx = form.getString("INPUT_REGEX");
                int input_datemin = form.getInt("INPUT_DATEMIN");
                int input_datemax = form.getInt("INPUT_DATEMAX");
                String photo_resolution = form.getString("PHOTO_RESOLUTION");
                int file_size = form.getInt("FILE_SIZE");
                int reg_begin = form.getInt("REG_BEGIN");
                int reg_end = form.getInt("REG_END");
                int is_mandatory = form.getInt("IS_MANDATORY");
                int is_keybaule = form.getInt("IS_KEYVALUE");

                creartextview(description);
                switch (type) {

                    case 1:

                        crearedittext(idField," ",20);

                        break;

                    case 2:

                        crearedittextmultilinea(idField," ",254);

                        break;

                    case 3:

                        urlParametros2 = urlParametros + idparameter;
                        new crearspinner2().execute();

                        break;

                    case 4:
                        break;

                    case 5:
                        break;

                    case 6:
                        break;

                    case 7:
                        break;

                    case 8:

                        /*urlParametros2 = urlParametros + idparameter;
                        cargarParametros();*/
                        //crearspinner(idField);


                        break;

                    case 9:
                        break;


                    default:
                        break;

                }

            }

        }catch (JSONException e) {

            e.printStackTrace();
            /*msj = Toast.makeText(this, "" + e, Toast.LENGTH_LONG);
            msj.show();*/

        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.hide();
        /*msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();*/

    }

    private ArrayList<String> cargarParametros(){

        /*mProgressDialog =  new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();*/

        final ArrayList<String> miarray = new ArrayList<>();

        Log.w("urlParametros", urlParametros2);

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlParametros2, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                msj = Toast.makeText(form_event.this, "" + response, Toast.LENGTH_LONG);
                msj.show();
                Log.w("respuesta", "" + response);
                //mProgressDialog.hide();

                try{

                    for(int i=0;i<response.length();i++) {

                        JSONObject parameter = response.getJSONObject(i);
                        int value = parameter.getInt("value");
                        String description = parameter.getString("description");

                        miarray.add(description);

                    }

                }catch (JSONException e) {

                    e.printStackTrace();
                    msj = Toast.makeText(form_event.this, "" + e, Toast.LENGTH_LONG);
                    msj.show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //mProgressDialog.hide();
                msj = Toast.makeText(form_event.this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
                msj.show();

            }
        }){

            @Override
            public Map getHeaders() throws AuthFailureError {

                HashMap headers = new HashMap();
                headers.put("Authorization", auth); //authentication
                return headers;

            }

        };

        mRequestQueue.add(mJsonArrayRequest);

        return miarray;
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
        //et.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        //et.setPadding(2, 2, 2, 2);
        //et.setHint(opcion);
        et.setId(id_opcion);
        InputFilter[] ifet = new InputFilter[1];
        ifet[0] = new InputFilter.LengthFilter(descripcion);
        et.setFilters(ifet);
        llContenedor.addView(et);
        editTexts.add(et);

    }

    // crear edittext multininea en el contenedor

    public void crearedittextmultilinea(int id_opcion, String opcion, int descripcion) {

        EditText et = new EditText(this);
        et.setSingleLine(false);
        et.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et.setLines(5);
        et.setMaxLines(10);
        et.setVerticalScrollBarEnabled(true);
        et.setMovementMethod(ScrollingMovementMethod.getInstance());
        et.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        //et.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        //et.setPadding(2, 2, 2, 2);
        //et.setHint(opcion);
        et.setId(id_opcion);
        InputFilter[] ifet = new InputFilter[1];
        ifet[0] = new InputFilter.LengthFilter(descripcion);
        et.setFilters(ifet);
        llContenedor.addView(et);
        editTexts.add(et);

    }

    // crear edittext datetami en el contenedor

    public void crearedittextdate(int id_opcion, String opcion, int descripcion) {


        EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        //et.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        //et.setPadding(2, 2, 2, 2);
        //et.setHint(opcion);
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

    // crear spinner en el contenedor

    public  void crearspinner(int id_opcion) {

        Spinner sp = new Spinner(this);
        sp.setId(id_opcion);
        ArrayList<String> miarray = new ArrayList<>();
        miarray = cargarParametros();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, miarray);
        sp.setAdapter(adapter);
        llContenedor.addView(sp);

    }

    private class crearspinner2 extends AsyncTask<String, String, Integer> {

        Spinner sp = new Spinner(form_event.this);
        ArrayList<String> miarray = new ArrayList<>();

        protected void onPreExecute() {



        }

        protected Integer doInBackground(String... urls) {

            miarray = cargarParametros();

            return 250;
        }

        protected void onProgressUpdate (Float... valores) {

        }

        protected void onPostExecute(Integer bytes) {


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(form_event.this,
                    android.R.layout.simple_spinner_item, miarray);
            sp.setAdapter(adapter);
            llContenedor.addView(sp);
        }
    }


    /*private class MiTarea extends AsyncTask<String, String, Integer>{

        protected void onPreExecute() {



        }

        protected Integer doInBackground(String... urls) {

            cargarParametros();

            return 250;
        }

        protected void onProgressUpdate (Float... valores) {

        }

        protected void onPostExecute(Integer bytes) {

            //mProgressDialog.hide();
        }
    }*/

}
