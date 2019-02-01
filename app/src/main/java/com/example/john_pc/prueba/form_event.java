package com.example.john_pc.prueba;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class form_event extends AppCompatActivity implements View.OnClickListener {

    int idField;
    int opcion;
    String auth;
    String userName;
    String idForm;
    LinearLayout llContenedor;
    Button btnSave;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonArrayRequest mJsonArrayRequest;

    final int codigoCamera = 20;
    final int codigoFile = 10;

    private final String camara_raiz = "misImagenesSistema/";
    private final String ruta_imagen = camara_raiz + "misFotos";
    String path;

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
    ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    ArrayList<ToggleButton> toggleButtons = new ArrayList<ToggleButton>();
    ArrayList<Switch> switches = new ArrayList<Switch>();
    ArrayList<TextView> textViewsDate = new ArrayList<TextView>();
    ArrayList<TextView> textViewsHour = new ArrayList<TextView>();
    ArrayList<TextView> textViewsFiles = new ArrayList<TextView>();
    Bitmap bit;
    Uri output;
    Bitmap bmp;
    Base64 imgBase64;
    JSONObject jsonenvio = new JSONObject();
    String textDate = "Haga clic para obtener la Fecha";
    String textHour = "Haga clic para obtener la Hora";
    String textFile = "Haga clic para obtener el Archivo";
    String textImage = "Imagen";
    String obligatorio = "obligatorio";

    Handler hand = new Handler();
    String fecha_2;
    String fecha_1;

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


        hand.removeCallbacks(actualizar);
        hand.postDelayed(actualizar, 100);

        validarPermisos();

        url = url + idForm;

        cargarFormulario();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int validar = 0;

                JSONArray respuesta = new JSONArray();

                for (Iterator iterator = editTexts.iterator(); iterator
                        .hasNext();) {

                    EditText editText = (EditText) iterator.next();
                    String obs_respuesta = editText.getText().toString().trim();
                    String control =  editText.getHint().toString().trim();

                    Log.w("controlEditText", control);

                    if (obs_respuesta.equals("") && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaEditText", "" + validar);

                    }

                    Log.w("control", "" + validar);

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

                for (Iterator iterator = textViewsDate.iterator(); iterator
                        .hasNext();) {

                    TextView textView = (TextView) iterator.next();
                    String obs_respuesta = textView.getText().toString().trim();
                    String control = textView.getHint().toString().trim();

                    Log.w("controlTextViewDate", control);

                    if (obs_respuesta.equals(textDate) && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaTextViewDate", "" + validar);

                    }

                    Log.w("TextViewDate", "TextView" + " " + textView.getId() + " " + obs_respuesta);
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", textView.getId());
                        parametros.put("valueInputField", "");
                        parametros.put("valueInputDateField", obs_respuesta);
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", "");
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for (Iterator iterator = textViewsHour.iterator(); iterator
                        .hasNext();) {

                    TextView textView = (TextView) iterator.next();
                    String obs_respuesta = textView.getText().toString().trim();
                    String control = textView.getHint().toString().trim();

                    if (obs_respuesta.equals(textHour) && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaTextViewHour", "" + validar);

                    }

                    Log.w("controlTextViewHour", control);

                    Log.w("TextViewHour", "TextView" + " " + textView.getId() + " " + obs_respuesta);
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", textView.getId());
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

                for (Iterator iterator = switches.iterator(); iterator
                        .hasNext();) {

                    Switch s = (Switch) iterator.next();

                    Log.w("Switch", "switches" + " " + s.getId() + " " + s.isChecked());
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", s.getId());
                        parametros.put("valueInputField", s.isChecked());
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", "");
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                for (Iterator iterator = imageViews.iterator(); iterator
                        .hasNext();) {

                    ImageView imageView = (ImageView) iterator.next();


                    String[] parts = imageView.getContentDescription().toString().trim().split("-");
                    String description = parts[0];
                    String control = parts[parts.length - 1];

                    if (description.equals(textImage) && control.equals(obligatorio))
                    {

                        validar++;
                        Log.w("sumaImageView", "" + validar);

                    }

                    Log.w("controlImageView", control);

                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();

                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    if (description.equals(textImage)) {

                        encoded ="";

                    }

                    Log.w("Imagen", "imageView" + " " + imageView.getId() + " " + encoded);
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", imageView.getId());
                        parametros.put("valueInputField", "");
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", encoded);
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for (Iterator iterator = textViewsFiles.iterator(); iterator
                        .hasNext();) {

                    TextView textView = (TextView) iterator.next();

                    String obs_respuesta = textView.getText().toString().trim();
                    String control = textView.getHint().toString().trim();

                    Log.w("controlTextViewFiles", control);

                    if (obs_respuesta.equals(textFile) && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaTextViewFiles", "" + validar);

                    }

                    File file = new File(textView.getText().toString().trim());

                    String[] parts = textView.getText().toString().trim().split("/");
                    String nombre = parts[parts.length - 1];

                    byte[] fileArray = new byte[(int) file.length()];
                    InputStream inputStream;

                    String encodedFile = "";
                    try {
                        inputStream = new FileInputStream(file);
                        inputStream.read(fileArray);
                        encodedFile = Base64.encodeToString(fileArray, Base64.DEFAULT);
                    } catch (Exception e) {
                        // Manejar Error
                    }

                    if (obs_respuesta.equals(textFile)) {

                        encodedFile = "";

                    }

                    Log.w("File", "files" + " " + textView.getId() + "nombre" + nombre);
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", textView.getId());
                        parametros.put("valueInputField", nombre);
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", encodedFile);
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                //Log.w("json", "" + respuesta);
                try {
                    jsonenvio.put("idEvent", "0");
                    jsonenvio.put("idEventDependency", "0");
                    jsonenvio.put("dateEvent", fecha_2);
                    jsonenvio.put("posGeo", "123456789");
                    jsonenvio.put("idForm", idForm);
                    jsonenvio.put("P", respuesta);

                    //Log.w("json", "" + jsonenvio);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (validar == 0) {

                    enviarformulario();

                } else {

                    completarDatos();

                }

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

                Log.w("respuesta", "" + response);

                mProgressDialog.dismiss();

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
                        //int input_datemin = form.getInt("INPUT_DATEMIN");
                        //int input_datemax = form.getInt("INPUT_DATEMAX");
                        //String photo_resolution = form.getString("PHOTO_RESOLUTION");
                        int file_size = form.getInt("FILE_SIZE");
                        int reg_begin = form.getInt("REG_BEGIN");
                        int reg_end = form.getInt("REG_END");
                        String is_mandatory = form.getString("IS_MANDATORY");
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


                        switch (type) {

                            case 1:

                                creartextview(description);
                                crearedittext(idField, is_mandatory, input_max);

                                break;

                            case 2:

                                creartextview(description);
                                crearedittextmultilinea(idField, is_mandatory, input_max);

                                break;

                            case 3:

                                creartextview(description);
                                createSpinner(idField, itemp);


                                break;

                            case 4:

                                creartextview(description);
                                createTextViewDate(idField, is_mandatory);

                                break;

                            case 5:

                                creartextview(description);
                                createTextViewHour(idField, is_mandatory);

                                break;

                            case 6:

                                creartextview(description);
                                createImageView(idField, is_mandatory);

                                break;

                            case 7:

                                creartextview(description);
                                createTextviewFile(idField,is_mandatory);

                                break;

                            case 8:

                                creartextview(description);
                                createSpinner(idField, itemp);

                                break;

                            case 9:

                                createSwitch(idField, description);

                                break;


                            default:
                                break;

                        }

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    Log.w("jsonException", "" + e);

                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressDialog.dismiss();

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
        et.setHint(opcion);
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
        et.setHint(opcion);
        et.setId(id_opcion);
        InputFilter[] ifet = new InputFilter[1];
        ifet[0] = new InputFilter.LengthFilter(descripcion);
        et.setFilters(ifet);
        llContenedor.addView(et);
        editTexts.add(et);

    }

    // crear TextViewDate en el contenedor

    public void createTextViewDate(int id, String option) {

        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setText(textDate);
        textView.setHint(option);
        textView.setOnClickListener(form_event.this);

        textViewsDate.add(textView);
        llContenedor.addView(textView);

    }

    // crear TextViewHour en el contenedor
    public void createTextViewHour(int id, String option) {

        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setText(textHour);
        textView.setHint(option);
        textView.setOnClickListener(form_event.this);

        textViewsHour.add(textView);
        llContenedor.addView(textView);

    }

    // crear radiobutton en el contenedor

    public void crearradiobutton(int idField, ArrayList<obj_params> items) {

        RadioGroup rg = new RadioGroup(this);
        rg.setId(idField);

        Log.w("RadioButton", "llegue aqui");

        Log.w("RadioButton", "" + items);

        for (Iterator iterator = items.iterator(); iterator
                .hasNext();) {

            Log.w("RadioButton", "llegue aqui2");

            obj_params obj = (obj_params) iterator.next();

            RadioButton rb = new RadioButton(this);

            rb.setId(obj.getId());
            rb.setText(obj.getDescription());

            rg.addView(rb);

            Log.w("RadioButton", "editText" + " " + obj.getId() + " " + obj.getDescription());

        }

        llContenedor.addView(rg);
        radioGroups.add(rg);

    }

    // crear ToggleButton
    public void createToggleButton(int idField){

        ToggleButton tb = new ToggleButton(this);
        tb.setId(idField);
        tb.setChecked(true);

        llContenedor.addView(tb);
        toggleButtons.add(tb);

    }

    // crear Switch
    public void createSwitch(int idField, String description){


        Switch s = new Switch(this);
        s.setId(idField);
        s.setText(description);
        s.setTextOn("Si");
        s.setTextOff("No");
        s.setChecked(true);

        Log.w("Switch", "su id es  " + s.getId());

        llContenedor.addView(s);
        switches.add(s);


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

        Spinner sp = new Spinner(this);
        sp.setId(idField);
        adapter_params adapter = new adapter_params(form_event.this, aux);
        sp.setAdapter(adapter);
        spinners.add(sp);
        llContenedor.addView(sp);
    }

    // Crear imageview en el contenedor

    public void createImageView(int idField, String option){

        ImageView iv = new ImageView(this);
        iv.setId(idField);
        iv.setImageResource(R.drawable.camera);
        iv.setContentDescription(textImage + "-" + option);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400,400);
        iv.setLayoutParams(lp);
        llContenedor.addView(iv);
        imageViews.add(iv);

        iv.setOnClickListener(this);

    }

    public void createTextviewFile(int idField, String option) {

        TextView textView = new TextView(this);
        textView.setId(idField);
        textView.setText(textFile);
        textView.setHint(option);
        textView.setOnClickListener(this);

        llContenedor.addView(textView);
        textViewsFiles.add(textView);

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
                mProgressDialog.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.w("mio", "" + error);
                mProgressDialog.dismiss();

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

    @Override
    public void onClick(View v) {

        msj = Toast.makeText(this, "" + v.getId(), Toast.LENGTH_LONG);
        msj.show();

        opcion = v.getId();

        boolean usarCamara = false;
        boolean date = false;
        boolean uploadFile = false;

        for(Iterator iterator = imageViews.iterator(); iterator
                .hasNext();) {

            ImageView imageView = (ImageView) iterator.next();

            if(imageView.getId() == opcion) {

                usarCamara = true;

            }

            Log.w("Edit", imageView.getId() + " = " + opcion);


        }

        for (Iterator iterator = textViewsDate.iterator(); iterator
                .hasNext();) {

            TextView textView = (TextView) iterator.next();

            if(textView.getId() == opcion) {

                date = true;

            }

        }

        for (Iterator iterator = textViewsHour.iterator(); iterator
                .hasNext();) {

            TextView textView = (TextView) iterator.next();

            if(textView.getId() == opcion) {

                getHour();

            }

        }

        for (Iterator iterator = textViewsFiles.iterator(); iterator
                .hasNext();) {

            TextView textView = (TextView) iterator.next();

            if(textView.getId() == opcion) {

                uploadFile = true;

            }

        }

        if (date){

            getDate();

        }

        if(usarCamara) {

            tomarFotografia();

        }

        if(uploadFile) {

            recoverDataFile();

        }



    }

    //recuperar archivo
    public void recoverDataFile() {

        new MaterialFilePicker()
                .withActivity(form_event.this)
                .withRequestCode(codigoFile)
                .start();

    }


    // camara
    private void tomarFotografia() {

        /*File fileImagen = new File(Environment.getExternalStorageDirectory(), ruta_imagen);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";

        if(isCreada == false) {

            isCreada = fileImagen.mkdir();

        }

        if(isCreada == true) {

            nombreImagen = (System.currentTimeMillis()/1000) + ".jpg";

        }

        path = Environment.getExternalStorageDirectory() + File.separator + ruta_imagen + File.separator + nombreImagen;

        File imagen =  new File(path);*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        startActivityForResult(intent, codigoCamera);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode== RESULT_OK){


            switch (requestCode) {

                case codigoCamera:

                    Bundle ext = data.getExtras();
                    bmp = (Bitmap) ext.get("data");

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();

                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    ImageView iv = findViewById(opcion);

                    String[] parts = iv.getContentDescription().toString().trim().split("-");
                    String description = parts[0] + "captura";
                    String control = parts[parts.length - 1];

                    iv.setContentDescription(description + "-" + control);

                    iv.setImageBitmap(bmp);

                    break;

                case codigoFile:

                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

                    TextView textView = findViewById(opcion);
                    textView.setText(filePath);

                    Log.w("path", filePath);

                    break;

            }

            /*MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {

                    Log.i("Ruta de almacenamiento", "path: " + path);

                }
            });

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ImageView iv = findViewById(opcion);
            iv.setImageBitmap(bitmap);*/




        }
    }

    private boolean validarPermisos(){

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            return true;

        }
        if((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){

            return true;

        }

        if ((shouldShowRequestPermissionRationale(CAMERA))||(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {

            cargardialogo();

        }
        else {

            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);

        }

        return false;

    }

    private void cargardialogo(){

        AlertDialog.Builder builder = new AlertDialog.Builder(form_event.this);
        builder.setTitle("Permisos Desactivados");
        builder.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
                }

            }
        });
        builder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100) {

            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {



            } else {

                cargardialogo2();

            }

        }

    }

    private void cargardialogo2(){

        final CharSequence[] op = {"si", "no"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(form_event.this);
        builder.setTitle("Desea configurar los permisos manualmente?");
        builder.setItems(op, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(op[which].equals("si")){

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent .setData(uri);
                    startActivity(intent);

                }
                else {

                    msj = Toast.makeText(form_event.this, "los permisos no fueron aceptados", Toast.LENGTH_LONG);
                    msj.show();
                    dialog.dismiss();

                }

            }
        });
        builder.show();

    }

    // actualiza la fecha

    private Runnable actualizar = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            SimpleDateFormat fmt1 = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            fecha_1 = fmt1.format(date);
            fecha_2 = fmt2.format(date);
            hand.postDelayed(this, 1000);

        }

    };

    //alert dialog para obtener fecha
    public void getDate(){

        int mYear, mMonth, mDay;
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay  =mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mdatePickerDialog = new DatePickerDialog(form_event.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                TextView textView = new TextView(form_event.this);
                textView = findViewById(opcion);
                int aux = month + 1;
                String fecha = "" + year + "-" + aux + "-" + dayOfMonth;
                Log.w("fecha", fecha);
                textView.setText(fecha);

            }
        },mYear,mMonth,mDay);
        mdatePickerDialog.setTitle("Selecione la fecha");
        mdatePickerDialog.show();

    }

    //alert dialog para obtener hora
    public void getHour(){

        int mHour, mMinute;
        Calendar mcurrentDate = Calendar.getInstance();
        mHour = mcurrentDate.get(Calendar.HOUR_OF_DAY);
        mMinute = mcurrentDate.get(Calendar.MINUTE);

        TimePickerDialog mTimePickerDialog = new TimePickerDialog(form_event.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                TextView textView = new TextView(form_event.this);
                textView =  findViewById(opcion);
                textView.setText(hourOfDay + ":" + minute);
                Log.w("Hora", hourOfDay + ":" + minute);

            }
        }, mHour, mMinute, false);

        mTimePickerDialog.setTitle("Selecione la Hora");
        mTimePickerDialog.show();

    }

    public void completarDatos(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("Debe completar todos los datos Requeridos");
        builder.setPositiveButton("Aceptar", null);
        builder.create();
        builder.show();

    }

}