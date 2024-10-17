package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText email, username, password;
    Button login;
    TextView forgotPsw, hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_btn2);
        forgotPsw = findViewById(R.id.forgot_psw);
        hide = findViewById(R.id.hide_login);

        DatabaseHelperLogin db = new DatabaseHelperLogin(this);

        //use only when gibberish data is added into the database
        //this.deleteDatabase("account_info.db");

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email2 = email.getText().toString();
                String username2 = username.getText().toString();
                String password2 = password.getText().toString();

                if(email2.isEmpty() || username2.isEmpty() || password2.isEmpty()){
                    Toast.makeText(Login.this, "Please enter the unfilled fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean ok = db.checkLogin(email2, username2, password2);
                    if(ok){
                        db.setUserLoggedIn(email2);
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("Email",email2);
                        intent.putExtra("Username", username2);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        hide.setOnClickListener(new View.OnClickListener(){
            boolean isHidden = true;
            @Override
            public void onClick(View v){
                if(isHidden){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hide.setText("Hide");
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hide.setText("Show");
                }

                isHidden = !isHidden;
            }
        });

        forgotPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ChangePassword.class);
                startActivity(intent);
                finish();
            }
        });

    }
}