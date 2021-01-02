package com.example.eyedive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class payActivity extends AppCompatActivity {
    private TextView productPrice;
    private String price = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        productPrice = (TextView) findViewById(R.id.total_);

        price = getIntent().getStringExtra("Total price");
        productPrice.setText("Total Price = "+" " + "\u20B9" +String.valueOf(price));

    }
}