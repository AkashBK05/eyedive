package com.example.eyedive;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyedive.Prevalent.Prevalent;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class payActivity extends AppCompatActivity implements PaymentResultListener {
    private TextView productPrice;
    private String price = "";
    private Button btPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        productPrice = (TextView) findViewById(R.id.total_);

        price = getIntent().getStringExtra("Total price");
        productPrice.setText("Total Price = "+" " + "\u20B9" +String.valueOf(price));
        btPay = (Button) findViewById(R.id.bt_pay);

        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_1Zj0wmU8aoj1Ez");
                checkout.setImage(R.drawable.rzp_logo);
                JSONObject object = new JSONObject();
                try {
                    object.put("name","EyeDive");
                    object.put("See clear","");
                    object.put("currency","INR");
                    object.put("amount",price);
                    object.put("number", Prevalent.currentOnlineUser.getPhone());
                    checkout.open(payActivity.this,object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("payment Id");
        builder.setMessage(s);
        Toast.makeText(this, "Order has been placed", Toast.LENGTH_SHORT).show();
        builder.show();
    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}