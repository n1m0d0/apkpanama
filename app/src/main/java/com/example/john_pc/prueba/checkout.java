package com.example.john_pc.prueba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class checkout extends AppCompatActivity {

    EditText etDataTime, etLongitude, etLatitude, etLocationEvent, etEvent, etLicensePlate, etClient,
            etBoat, etTypeVehicle, etReasonVisit, etDestination, etCustomsVisit, etLicensePlate2, etPassenger,
            etCarrierCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

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

    }
}
