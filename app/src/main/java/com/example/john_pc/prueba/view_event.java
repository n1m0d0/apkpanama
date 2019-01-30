package com.example.john_pc.prueba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class view_event extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llContenedor;
    Button btnCheckOut;
    String auth;
    String userName;
    String idEvent;
    String url = "https://test.portcolon2000.site/api/openEvent/";
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonArrayRequest mJsonArrayRequest;
    ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    ArrayList<TextView> textViews = new ArrayList<TextView>();
    int option;
    Intent ir;
    int generaForm;
    int idForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        llContenedor = findViewById(R.id.llContenedor);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        idEvent = parametros.getString("idEvent");

        Log.w("idEvent", idEvent);
        url = url + idEvent;
        //mostrar datos

        cargarFormulario();

        btnCheckOut = findViewById(R.id.btnCheckOut);
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ir = new Intent(view_event.this, checkout.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("idForm", "" + generaForm);
                ir.putExtra("idEvent", "" + idEvent);
                startActivity(ir);

                Log.w("generaForm", "" + "" + generaForm);

            }
        });

        btnCheckOut.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        option = v.getId();

        String urls = "";

        for (Iterator iterator = imageViews.iterator(); iterator
                .hasNext();) {

            ImageView imageView = (ImageView) iterator.next();

            if (imageView.getId() == option) {

                
                urls = "" + imageView.getContentDescription();
                Uri uri = Uri.parse(urls);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


            }

            Log.w("Edit", imageView.getId() + " = " + urls);


        }

        for (Iterator iterator = textViews.iterator(); iterator
                .hasNext();) {

            TextView textView = (TextView) iterator.next();

            if (textView.getId() == option) {

                Uri uri = Uri.parse(textView.getText().toString().trim());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


            }

            Log.w("Edit", textView.getId() + " = " + urls);


        }


    }

    private void cargarFormulario(){

        mProgressDialog =  new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                mProgressDialog.hide();

                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject form = response.getJSONObject(i);

                        int idEvent = form.getInt("idEvent");
                        int idEventDependency = form.getInt("idEventDependency");
                        String dateEvent = form.getString("dateEvent");
                        String posGeo = form.getString("posGeo");
                        idForm = form.getInt("idForm");
                        generaForm = form.getInt("generaForm");
                        JSONArray opciones = form.getJSONArray("P");

                        if(generaForm > 0) {

                            btnCheckOut.setVisibility(View.VISIBLE);

                        }

                        //ArrayList<obj_params> itemp = new ArrayList<obj_params>();

                        for (int j = 0; j < opciones.length(); j++) {

                            JSONObject op = opciones.getJSONObject(j);
                            int idField = op.getInt("idField");
                            String valueInputField = op.getString("valueInputField");
                            String valueInputDateField = op.getString("valueInputDateField");
                            String valueListField = op.getString("valueListField");
                            String valueFile = op.getString("valueFile");
                            int type = op.getInt("type");
                            String description = op.getString("description");

                            switch (type){

                                case 1:

                                    createTextView(description);
                                    createEditText(valueInputField);

                                    break;

                                case 2:

                                    createTextView(description);
                                    createEditTextMultiLinea(valueInputField);

                                    break;

                                case 3:

                                    createTextView(description);
                                    createEditText(valueInputField);

                                    break;

                                case 4:

                                    createTextView(description);
                                    createEditText(valueInputDateField);

                                    break;

                                case 5:

                                    createTextView(description);
                                    createEditText(valueInputDateField);

                                    break;

                                case 6:

                                    createTextView(description);
                                    createImageView(idField, valueFile, valueInputField);

                                    break;

                                case 7:

                                    createTextViewpath(idField, valueInputField);

                                    break;

                                case 8:

                                    createTextView(description);
                                    createEditText(valueInputField);

                                    break;

                                case 9:

                                    createSwitch(description, valueInputField);

                                    break;

                            }


                            //itemp.add(new obj_params(idField, des));

                        }

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

        mRequestQueue.add(mJsonArrayRequest);

    }

    public void createTextView(String description) {

        TextView textView = new TextView(this);
        textView.setText(description);
        llContenedor.addView(textView);


    }

    public void createEditText(String description) {

        EditText editText = new EditText(this);
        editText.setText(description);
        editText.setEnabled(false);
        llContenedor.addView(editText);

    }

    public void createEditTextMultiLinea(String descripcion) {

        EditText et = new EditText(this);
        et.setSingleLine(false);
        et.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et.setLines(5);
        et.setMaxLines(10);
        et.setVerticalScrollBarEnabled(true);
        et.setMovementMethod(ScrollingMovementMethod.getInstance());
        et.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        et.setText(descripcion);
        InputFilter[] ifet = new InputFilter[1];
        ifet[0] = new InputFilter.LengthFilter(254);
        et.setFilters(ifet);
        et.setEnabled(false);
        llContenedor.addView(et);

    }

    public void createSwitch(String description, String valor) {

        boolean variable = Boolean.parseBoolean (valor);
        Switch s = new Switch(this);
        s.setText(description);
        s.setTextOn("Si");
        s.setTextOff("No");
        s.setChecked(variable);
        s.setEnabled(false);
        llContenedor.addView(s);

    }

    public void createImageView(int id, String imagen, String url){

        ImageView imageView = new ImageView(this);

        imageView.setId(id);
        imageView.setContentDescription(url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(imagen, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(decodedImage);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400,400);
        imageView.setLayoutParams(lp);
        imageView.setOnClickListener(this);
        imageViews.add(imageView);
        llContenedor.addView(imageView);

    }

    public void createTextViewpath(int id, String description) {

        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setText(description);
        textView.setOnClickListener(this);
        textViews.add(textView);
        llContenedor.addView(textView);

    }

}
