package com.example.eyedive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eyedive.Admin.AdminCategoryActivity;
import com.example.eyedive.Model.Users;
import com.example.eyedive.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        LoadingBar= new ProgressDialog(this);
        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordkey);
        String CurrentDb = Paper.book().read(Prevalent.db);

        if (UserPhoneKey !=  null && UserPasswordKey != null) {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {

                AllowAcess(UserPhoneKey,UserPasswordKey, CurrentDb);

                LoadingBar.setTitle("Already logged in");
                LoadingBar.setMessage("Please wait...");
                LoadingBar.setCanceledOnTouchOutside(false);
                LoadingBar.show();

            }
        }

    }

    private void AllowAcess(String phone, String password , String db) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(db).child(phone).exists()){
                    Users usersdata = snapshot.child(db).child(phone).getValue(Users.class);
                    if (usersdata.getPhone().equals(phone)){
                        if (usersdata.getPassword().equals(password)){
                            if(db.equals("Admin")){
                                Toast.makeText(MainActivity.this, "Logged in successfully admin", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this, AdminCategoryActivity.class);
                                Prevalent.currentOnlineUser = usersdata;
                                startActivity(intent);
                            }else if (db.equals("Users")){
                                Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersdata;
                                startActivity(intent);
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            LoadingBar.dismiss();
                        }
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Account with this" + phone + "DOESN'T exists", Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}