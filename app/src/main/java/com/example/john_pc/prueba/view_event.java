package com.example.john_pc.prueba;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class view_event extends AppCompatActivity implements View.OnClickListener {

    EditText etDataTime, etLongitude, etLatitude, etLocationEvent, etEvent, etLicensePlate, etClient,
            etBoat, etTypeVehicle, etReasonVisit, etDestination, etCustomsVisit, etLicensePlate2, etPassenger,
            etCarrierCompany;
    Button btnCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        etDataTime = findViewById(R.id.etDateTime);
        etLongitude = findViewById(R.id.etLongitude);
        etLatitude = findViewById(R.id.etLatitude);
        etLocationEvent = findViewById(R.id.etLocationEvent);
        etEvent = findViewById(R.id.etEvent);
        etLicensePlate = findViewById(R.id.etLicensePlate);
        etClient = findViewById(R.id.etClient);
        etBoat = findViewById(R.id.etBoat);
        etTypeVehicle = findViewById(R.id.etTypeVehicle);
        etReasonVisit = findViewById(R.id.etReasonVisit);
        etDestination = findViewById(R.id.etDestination);
        etCustomsVisit = findViewById(R.id.etCustomsVisit);
        etLicensePlate2 = findViewById(R.id.etLicensePlate2);
        etPassenger = findViewById(R.id.etPassenger);
        etCarrierCompany = findViewById(R.id.etCarrierCompany);

        btnCheckOut = findViewById(R.id.btnCheckOut);
        btnCheckOut.setOnClickListener(this);

        //funcion para obtener datos

        //mostrar datos
        etDataTime.setText("1");
        etLongitude.setText("2");
        etLatitude.setText("2");
        etLocationEvent.setText("2");
        etEvent.setText("2");
        etLicensePlate.setText("2");
        etClient.setText("2");
        etBoat.setText("2");
        etTypeVehicle.setText("2");
        etReasonVisit.setText("2");
        etDestination.setText("2");
        etCustomsVisit.setText("2");
        etLicensePlate2.setText("2");
        etPassenger.setText("2");
        etCarrierCompany.setText("2");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnCheckOut:

                Intent ir = new Intent(this, checkout.class);
                startActivity(ir);

                break;

        }

    }
}
