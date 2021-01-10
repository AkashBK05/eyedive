package com.example.eyedive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyedive.Admin.AdminCategoryActivity;
import com.example.eyedive.Admin.AdminChangePaawordActivity;
import com.example.eyedive.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class UserChangePasswordActivity extends AppCompatActivity {
    private EditText oldPassword,newPassword;
    private TextView changepassword;
    private DatabaseReference passwordRef;
    private Button applyChangesbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);
        applyChangesbtn = findViewById(R.id.change_user_password_btn);
        oldPassword = findViewById(R.id.user_old_password);
        newPassword = findViewById(R.id.user_new_password);
        changepassword = findViewById(R.id.user_change_password);
        passwordRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        applyChangesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChange();
            }
        });

    }

    private void applyChange() {
        String oldpassword = oldPassword.getText().toString();
        String newpassword = newPassword.getText().toString();
        String old= Paper.book().read(Prevalent.UserPasswordkey);

        if (TextUtils.isEmpty(oldpassword)){
            Toast.makeText(this, "Enter old password", Toast.LENGTH_SHORT).show();
        }else if(!(isValidPassword(newpassword))){
            Toast.makeText(this, "new password must contains upper case,lower case alphabet,digits and special characters.", Toast.LENGTH_SHORT).show();

        }else if (oldpassword.equals(old)) {
            Toast.makeText(this, "password doesn't match", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<String, Object> passwordMap = new HashMap<>();
            passwordMap.put("password", newpassword);

            passwordRef.updateChildren(passwordMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserChangePasswordActivity.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserChangePasswordActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }

    }

    private boolean isValidPassword(String newpassword) {
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (newpassword == null) {
            return false;
        }
        Matcher m = p.matcher(newpassword);
        return m.matches();
    }
}