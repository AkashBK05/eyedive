package com.example.eyedive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eyedive.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText NameEditText,PhoneEditText,AddressEdirText,cityEditText;
    private Button confirmorderbtn;
    private  String totalamt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalamt = getIntent().getStringExtra("Total price");

        confirmorderbtn = (Button) findViewById(R.id.confirm_final_order_btn);
        NameEditText= (EditText)findViewById(R.id.shipment_name);
        PhoneEditText= (EditText)findViewById(R.id.shipment_phone_number);
        AddressEdirText= (EditText)findViewById(R.id.shipment_address);
        cityEditText= (EditText)findViewById(R.id.shipment_city);


        confirmorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check() {


        if(TextUtils.isEmpty(NameEditText.getText().toString())){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
        }else if(!(isValidPhone(PhoneEditText.getText().toString()))) {
            Toast.makeText(this, "Enter correct phone number", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(AddressEdirText.getText().toString())){
            Toast.makeText(this, "enter address", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(this, "enter city", Toast.LENGTH_SHORT).show();
        }else{
            confirmOrder();
        }

    }

    private boolean isValidPhone(String toString) {
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher m = p.matcher(toString);
        return (m.find() && m.group().equals(toString));
    }

    private void confirmOrder() {
        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM , dd , yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("total_amount",totalamt);
        ordersMap.put("name",NameEditText.getText().toString());
        ordersMap.put("phone", PhoneEditText.getText().toString());
        ordersMap.put("address", AddressEdirText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
       ordersMap.put("state", "not shipped");


       orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   FirebaseDatabase.getInstance().getReference().child("Cart List")
                           .child("User View")
                           .child(Prevalent.currentOnlineUser.getPhone())
                           .removeValue()
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       Toast.makeText(ConfirmFinalOrderActivity.this, "Order has been placed", Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       startActivity(intent);
                                       finish();
                                   }
                               }
                           });
               }
           }
       });
    }



}