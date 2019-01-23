package com.example.john_pc.prueba;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class form_event extends AppCompatActivity{

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
    String url2 = "https://test.portcolon2000.site/api/saveEvent";
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
    Bitmap bmp;
    Base64 imgBase64;
    JSONObject jsonenvio = new JSONObject();

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



                JSONArray respuesta = new JSONArray();

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
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", editText.getId());
                        parametros.put("valueInputField", obs_respuesta);
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", "");
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for (Iterator iterator = spinners.iterator(); iterator
                        .hasNext();) {

                    Spinner spinner = (Spinner) iterator.next();

                    int position = spinner.getSelectedItemPosition();

                    String nombre = spinner.getItemAtPosition(position).toString();

                    obj_params elegido = (obj_params) spinner.getItemAtPosition(position);

                    Log.w("Spinner", "spinner: " + spinner.getId() + " posicion: " + position + " " + elegido.getId());

                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", spinner.getId());
                        parametros.put("valueInputField", "");
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", elegido.getId());
                        parametros.put("valueFile", "");
                        respuesta.put(parametros);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                Log.w("json", "" + respuesta);
                try {
                    jsonenvio.put("idEvent", "0");
                    jsonenvio.put("idEventDependency", "0");
                    jsonenvio.put("dateEvent", "2019-01-08 12:30:15");
                    jsonenvio.put("posGeo", "123456789");
                    jsonenvio.put("idForm", idForm);
                    jsonenvio.put("P", respuesta);

                    Log.w("json", "" + jsonenvio);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                enviarformulario();

            }
        });

    }

    private void cargarFormulario(){

        mProgressDialog =  new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

        /*msj = Toast.makeText(this, "" + response, Toast.LENGTH_LONG);
        msj.show();*/
                mProgressDialog.hide();

                try {

                    for (int i = 0; i < response.length(); i++) {

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
                        //String photo_resolution = form.getString("PHOTO_RESOLUTION");
                        int file_size = form.getInt("FILE_SIZE");
                        int reg_begin = form.getInt("REG_BEGIN");
                        int reg_end = form.getInt("REG_END");
                        int is_mandatory = form.getInt("IS_MANDATORY");
                        int is_keybaule = form.getInt("IS_KEYVALUE");


                        JSONArray opciones = form.getJSONArray("P");

                        String[] listopcion = new String[opciones.length()];

                        ArrayList<obj_params> itemp = new ArrayList<obj_params>();

                        for (int j = 0; j < opciones.length(); j++) {

                            JSONObject op = opciones.getJSONObject(j);
                            int valor = op.getInt("IDVALUE");
                            String des = op.getString("DESCRIPTION");

                            listopcion[j] = des;

                            itemp.add(new obj_params(valor, des));

                            Log.w("Description opcion", listopcion[j]);

                        }

                        creartextview(description);
                        switch (type) {

                            case 1:

                                crearedittext(idField, " ", 20);

                                break;

                            case 2:

                                crearedittextmultilinea(idField, " ", 254);

                                break;

                            case 3:

                                //createSpinner(idField, listopcion);
                                createSpinner(idField, itemp);


                                break;

                            case 4:


                                break;

                            case 5:
                                break;

                            case 6:

                        /*Button btn;
                        btn =  new Button(this);
                        btn.setId(idField);
                        btn.setText("Capturar Foto");
                        llContenedor.addView(btn);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ir = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(ir, 0);

                            }
                        });*/

                        /*ImageView iv = new ImageView(this);
                        iv.setId(idField);
                        iv.setImageResource(R.drawable.ic_launcher_background);
                        llContenedor.addView(iv);

                        Button btncamara =  new Button(this);
                        btncamara.setText("Capturar Foto");
                        btncamara.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                camara.putExtra("thisone", "ArgumentFrom");
                                Bundle extras = new Bundle();
                                extras.putBoolean("thisalso", true);
                                camara.putExtras(extras);
                                startActivityForResult(camara, 1888);

                            }
                        });
                        llContenedor.addView(btncamara);*/


                                break;

                            case 7:

                        /*Button btn2 = null;
                        btn2.setId(idField);
                        btn2 =  new Button(this);
                        btn2.setText("Elegir Archivo");
                        llContenedor.addView(btn2);
                        btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {



                            }
                        });*/

                                break;

                            case 8:

                                //createSpinner(idField, listopcion);
                                createSpinner(idField, itemp);

                                break;

                            case 9:
                                break;


                            default:
                                break;

                        }

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
            /*msj = Toast.makeText(this, "" + e, Toast.LENGTH_LONG);
            msj.show();*/

                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressDialog.hide();
        /*msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();*/

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

    // crear un spinner en el contenedor

    public void createSpinner(int idField, ArrayList<obj_params> aux){

        //String [] aux = { "nombre1", "nombre2", "nombre3", "nombre4"};

        Spinner sp = new Spinner(this);
        sp.setId(idField);
        adapter_params adapter = new adapter_params(form_event.this, aux);
        sp.setAdapter(adapter);
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, aux);
        sp.setAdapter(adapter);*/
        spinners.add(sp);
        llContenedor.addView(sp);
    }

    // camara
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1888 && resultCode== Activity.RESULT_OK){

            Bundle ext = data.getExtras();
            bmp = (Bitmap) ext.get("data");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

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

    private void enviarformulario(){

        Log.w("url", url2);

        JsonObjectRequest mjsonObjectRequest;

        RequestQueue mRequestQueue2;

        mProgressDialog =  new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        mRequestQueue2 = Volley.newRequestQueue(this);

        mjsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2, jsonenvio, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.w("mio", "" + response);
                msj = Toast.makeText(form_event.this, "" + response, Toast.LENGTH_LONG);
                msj.show();
                mProgressDialog.hide();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.w("mio", "" + error);
                mProgressDialog.hide();

            }
        }){

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", auth); //authentication
                return headers;
            }

        };

        mRequestQueue2.add(mjsonObjectRequest);

    }

}
