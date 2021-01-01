package com.example.eyedive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText name,phone,address,city;
    private Button confirmorderbtn;
    private  String totalamt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalamt = getIntent().getStringExtra("Total price");

        confirmorderbtn = (Button) findViewById(R.id.confirm_final_order_btn);
        name= (EditText)findViewById(R.id.shipment_name);
        phone= (EditText)findViewById(R.id.shipment_phone_number);
        address= (EditText)findViewById(R.id.shipment_address);
        city= (EditText)findViewById(R.id.shipment_city);
    }
}