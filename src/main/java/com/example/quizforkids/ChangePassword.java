package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    EditText email, newPass, confirmPass;
    Button reset_btn;
    TextView show, show2, checkEmail, checkNewPass, checkCfmNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        email = findViewById(R.id.email);
        newPass = findViewById(R.id.newPass);
        confirmPass = findViewById(R.id.confirmPass);
        reset_btn = findViewById(R.id.reset_btn);
        show = findViewById(R.id.show);
        show2 = findViewById(R.id.show2);
        checkEmail = findViewById(R.id.checkEmail);
        checkNewPass = findViewById(R.id.checkNewPass);
        checkCfmNewPass = findViewById(R.id.checkCfmNewPass);

        DatabaseHelperLogin db = new DatabaseHelperLogin(this);

        reset_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //get user input
                String getEmail = email.getText().toString();
                //get username
                String username = db.getUsernameByEmail(getEmail);
                String getNewPass = newPass.getText().toString();
                String getConfirmPass = confirmPass.getText().toString();

                if (checkBeforeReset(getEmail, getNewPass, getConfirmPass)){
                    if(db.changePassword(getEmail, getNewPass)){
                        Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                        intent.putExtra("Email", getEmail);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ChangePassword.this, "Password change Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //text watcher on password
        newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(newPass.length() < 7){
                    checkNewPass.setText("Password should contain at least 7 characters");
                    checkNewPass.setTextColor(getResources().getColor(R.color.Error));
                } else {
                    checkNewPass.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(confirmPass.length() < 7){
                    checkCfmNewPass.setText("Password should contain at least 7 characters");
                    checkCfmNewPass.setTextColor(getResources().getColor(R.color.Error));
                } else {
                    checkCfmNewPass.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //textWatcher for email
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!String.valueOf(email).contains("@gmail.com") && email.length() <= 10){
                    checkEmail.setText("Invalid Email");
                    checkEmail.setTextColor(getResources().getColor(R.color.Error));
                } else {
                    checkEmail.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        show2.setOnClickListener(new View.OnClickListener() {
            boolean isHidden = true;

            @Override
            public void onClick(View v) {
                if (isHidden) {
                    newPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    show2.setText("Hide");
                } else {
                    newPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    show2.setText("Show");
                }

                isHidden = !isHidden;
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            boolean isHidden = true;

            @Override
            public void onClick(View v) {
                if (isHidden) {
                    confirmPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    show.setText("Hide");
                } else {
                    confirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    show.setText("Show");
                }

                isHidden = !isHidden;
            }
        });
    }

    public boolean checkBeforeReset(String email, String password, String confirmPsw){
        if(email.isEmpty() || password.isEmpty() || confirmPsw.isEmpty()){
            Toast.makeText(ChangePassword.this, "Please enter the unfilled fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!email.contains("@gmail.com") && email.length() <= 10){
            Toast.makeText(ChangePassword.this, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!password.equals(confirmPsw)){
            Toast.makeText(ChangePassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        } else if(password.length() < 7){
            Toast.makeText(ChangePassword.this, "Password should contain at least 7 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}