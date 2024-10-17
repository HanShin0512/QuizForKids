package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;


public class SplashScreen extends AppCompatActivity {

    ImageView gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //this.deleteDatabase("account_info.db");

        // Load the GIF using Glide
        gif = findViewById(R.id.gif);
        Glide.with(this)
                .asGif()
                .load(R.drawable.logogif)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(gif);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseHelperLogin db = new DatabaseHelperLogin(SplashScreen.this);
                String email = db.getLastAddedEmail();
                if(email != null && db.isUserLoggedIn(email)){
                    String username = db.getUsernameByEmail(email);
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("Username", username);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, LogInSignUp.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 5000);
    }
}