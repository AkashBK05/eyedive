package com.example.eyedive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {


    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber,InputPassword , InputConfirmPassword, InputEmailid;
    private ProgressDialog LoadingBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        InputEmailid = (EditText) findViewById(R.id.register_email_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputConfirmPassword = (EditText) findViewById(R.id.register_confirm_password_input);
        LoadingBar= new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String emailId = InputEmailid.getText().toString();
        String password = InputPassword.getText().toString();
        String confirmPassword = InputConfirmPassword.getText().toString();


        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
        }else if(!(isValidPhone(phone))){
            Toast.makeText(this, "Enter correct phone number", Toast.LENGTH_SHORT).show();
        }else if(!(isValidPassword(password))){
            Toast.makeText(this, "password must contains upper case,lower case alphabet,digits and special characters.", Toast.LENGTH_SHORT).show();
        }else if(!(isValidEmail(emailId))){
            Toast.makeText(this, "Enter valid emailId", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this, "confirm your password", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(confirmPassword)){
            Toast.makeText(this, "passwords doesn't match", Toast.LENGTH_SHORT).show();
        }else{
            LoadingBar.setTitle("Create Account");
            LoadingBar.setMessage("please wait");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();

            InsertData(name,phone,password,emailId);
        }

    }

    private boolean isValidEmail(String emailId) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (emailId == null)
            return false;
        return pat.matcher(emailId).matches();
    }

    private boolean isValidPhone(String phone) {
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher m = p.matcher(phone);
        return (m.find() && m.group().equals(phone));
    }

    private boolean isValidPassword(String password)
    {
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

    private void InsertData(String name, String phone, String password, String emailId) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);
                    userdataMap.put("emailId",emailId);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "account has been created", Toast.LENGTH_SHORT).show();
                                        LoadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }else{
                                        LoadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network error try again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }else{
                    Toast.makeText(RegisterActivity.this,  phone + " " +"already exists", Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "try with different phone number", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}