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

public class SignUp extends AppCompatActivity {

    EditText email, username, password, confirmPsw;
    Button signup;
    TextView hide, hide2, checkPass, checkCfmPass, checkEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPsw = findViewById(R.id.confirm_psw);
        signup = findViewById(R.id.signUp_btn2);
        hide = findViewById(R.id.hide_signup);
        hide2 = findViewById(R.id.hide2_signup);
        checkPass = findViewById(R.id.checkPass);
        checkCfmPass = findViewById(R.id.checkCfmPass);
        checkEmail = findViewById(R.id.checkEmail);

        DatabaseHelperLogin db = new DatabaseHelperLogin(this);

        //sign up button
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email2 = email.getText().toString();
                String username2 = username.getText().toString();
                String password2 = password.getText().toString();
                String confirmPsw2 = confirmPsw.getText().toString();

                boolean ok = checkBeforeCreate(email2, username2, password2, confirmPsw2);
                if(ok){
                    boolean accountCreated = db.insertSignUpInfo(email2, username2, password2);
                    db.setUserLoggedIn(email2);
                    if(accountCreated){
                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                        intent.putExtra("Email",email2);
                        intent.putExtra("Username", username2);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            boolean isHidden = true;

            @Override
            public void onClick(View v) {
                if (isHidden) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hide.setText("Hide");
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hide.setText("Show");
                }

                isHidden = !isHidden;
            }
        });

        hide2.setOnClickListener(new View.OnClickListener() {
            boolean isHidden = true;

            @Override
            public void onClick(View v) {
                if (isHidden) {
                    confirmPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hide2.setText("Hide");
                } else {
                    confirmPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hide2.setText("Show");
                }

                isHidden = !isHidden;
            }
        });

        //text watcher on password
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password.length() < 7){
                    checkPass.setText("Password should contain at least 7 characters");
                    checkPass.setTextColor(getResources().getColor(R.color.Error));
                } else {
                    checkPass.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(confirmPsw.length() < 7){
                     checkCfmPass.setText("Password should contain at least 7 characters");
                     checkCfmPass.setTextColor(getResources().getColor(R.color.Error));
                 } else {
                    checkCfmPass.setTextColor(getResources().getColor(R.color.white));
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

    }

    public boolean checkBeforeCreate(String email, String username, String password, String confirmPsw){
        DatabaseHelperLogin db = new DatabaseHelperLogin(SignUp.this);
        if(email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPsw.isEmpty()){
            Toast.makeText(SignUp.this, "Please enter the unfilled fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if(db.isEmailExists(email)){
            Toast.makeText(SignUp.this, "Email exists, login instead?", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!email.contains("@gmail.com") && email.length() <= 10){
            Toast.makeText(SignUp.this, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!password.equals(confirmPsw)){
            Toast.makeText(SignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        } else if(password.length() < 7){
            Toast.makeText(SignUp.this, "Password should contain at least 7 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}