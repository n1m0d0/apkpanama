package com.example.john_pc.prueba;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class form_event extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {

    int idField;
    String auth;
    String userName;
    String idForm;
    LinearLayout llContenedor;
    Button btnSave;
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
    ArrayList<Spinner> spinners = new ArrayList<Spinner>();
    ArrayList<String> fotos = new ArrayList<String>();
    ArrayList<String> archivos = new ArrayList<String>();
    Bitmap bit;
    Uri output;
    String path;
    Base64 imgBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_event);

        llContenedor = findViewById(R.id.llContenedor);
        btnSave = findViewById(R.id.btnSave);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        idForm = parametros.getString("idForm");

        url = url + idForm;

        cargarFormulario();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Iterator iterator = editTexts.iterator(); iterator
                        .hasNext();) {

                    EditText editText = (EditText) iterator.next();
                    String obs_respuesta = editText.getText().toString().trim();

                    if (!obs_respuesta.equals("")) {

                        msj = Toast.makeText(form_event.this, obs_respuesta
                                + " " + editText.getId(), Toast.LENGTH_LONG);
                        msj.show();

                    }

                    Log.w("Edit", "editText" + " " + editText.getId() + " " + obs_respuesta);

                }

                for (Iterator iterator = spinners.iterator(); iterator
                        .hasNext();) {

                    int id = 0;
                    Spinner spinner = (Spinner) iterator.next();

                    int position = spinner.getSelectedItemPosition();



                    Log.w("Spinner", "spinner" + " " + spinner.getId() + " " + position);

                }


            }
        });

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

                idField = form.getInt("IDFIELD");
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

                        Button btn;
                        btn =  new Button(this);
                        btn.setId(idField);
                        btn.setText("Capturar Foto");
                        llContenedor.addView(btn);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ir = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                startActivityForResult(ir, 2);

                            }
                        });

                        break;

                    case 7:

                        Button btn2 = null;
                        btn2.setId(idField);
                        btn2 =  new Button(this);
                        btn2.setText("Elegir Archivo");
                        llContenedor.addView(btn2);
                        btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {



                            }
                        });

                        break;

                    case 8:

                        urlParametros2 = urlParametros + idparameter;
                        new crearspinner2().execute();


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

    private ArrayList<obj_params> cargarParametros(){

        /*mProgressDialog =  new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();*/
        final ArrayList<obj_params> itemParams = new ArrayList<obj_params>();
        //final ArrayList<String> miarray = new ArrayList<>();

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

                        //miarray.add(description);
                        itemParams.add(new obj_params(value, description));

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

        return itemParams;
        //return miarray;
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


        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setId(id_opcion);
        InputFilter[] ifet = new InputFilter[1];
        ifet[0] = new InputFilter.LengthFilter(descripcion);
        et.setFilters(ifet);
        llContenedor.addView(et);
        editTexts.add(et);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                et.setText(sdf.format(myCalendar.getTime()));
            }

        };

        et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(form_event.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


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

    private class crearspinner2 extends AsyncTask<String, Integer, ArrayList<obj_params>> {

        Spinner sp = new Spinner(form_event.this);

        ArrayList<obj_params> miarray = new ArrayList<>();

        protected void onPreExecute() {

            sp.setId(idField);
            spinners.add(sp);
            llContenedor.addView(sp);

        }

        protected ArrayList<obj_params> doInBackground(String... params) {

            //String idsp = params[1];
            //Log.w("idfield", idsp);
            miarray = cargarParametros();

            return miarray;
        }

        protected void onPostExecute(ArrayList<obj_params> datos) {

            sp.setAdapter(new adapter_params(form_event.this, datos));
            //llContenedor.addView(sp);
        }

        protected void onProgressUpdate (Integer... values) {

        }

    }

    // camara
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode== Activity.RESULT_OK){



        }
    }

    //direccion del path
    private String getPath(Uri uri) {
        String[] projection = { android.provider.MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
