package com.example.quizforkids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

public class UserManual extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton nav_btn_userManual, acc_photo;
    NavigationView navigationView_UserManual;
    TextView acc_name, acc_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);

        drawerLayout = findViewById(R.id.drawerLayout);
        nav_btn_userManual = findViewById(R.id.nav_btn_userManual);
        navigationView_UserManual = findViewById(R.id.navigationView_userManual);
        View headerView = getLayoutInflater().inflate(R.layout.drawer_header, navigationView_UserManual, false);

        // Find acc_name and acc_email within the headerView
        acc_name = headerView.findViewById(R.id.acc_name);
        acc_email = headerView.findViewById(R.id.acc_email);

        // Add the header view to the navigationView
        navigationView_UserManual.addHeaderView(headerView);

        //get username and email
        String getAccName = getIntent().getStringExtra("Username");
        String getEmail = getIntent().getStringExtra("Email");

        //change it
        if (getAccName != null) {
            acc_name.setText(getAccName);
        } else {
            acc_name.setText("Username not available");
        }
        if (getEmail != null) {
            acc_email.setText(getEmail);
        } else {
            acc_email.setText("Email not available");
        }

        //open navigation bar
        nav_btn_userManual.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.open();
            }
        });

        //control which navigation icon does what
        navigationView_UserManual.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.home){
                    if(!this.getClass().getSimpleName().equals("MainActivity")){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Username", getAccName);
                        intent.putExtra("Email", getEmail);
                        startActivity(intent);
                        finish();
                    }
                } else if(itemId == R.id.manual){
                    Intent intent = new Intent(getApplicationContext(), UserManual.class);
                    if(!this.getClass().getSimpleName().equals("UserManual")){
                        intent.putExtra("Username", getAccName);
                        intent.putExtra("Email", getEmail);
                        startActivity(intent);
                        finish();
                    }
                } else if(itemId == R.id.history){
                    Intent intent = new Intent(getApplicationContext(), History.class);
                    if(!this.getClass().getSimpleName().equals("History")) {
                        intent.putExtra("Username", getAccName);
                        intent.putExtra("Email", getEmail);
                        startActivity(intent);
                        finish();
                    }
                } else if(itemId == R.id.TandC){
                    Intent intent = new Intent(getApplicationContext(), TandC.class);
                    if(!this.getClass().getSimpleName().equals("TandC")) {
                        intent.putExtra("Username", getAccName);
                        intent.putExtra("Email", getEmail);
                        startActivity(intent);
                        finish();
                    }
                } else if(itemId == R.id.feedback){
                    Intent intent = new Intent(getApplicationContext(), Feedback.class);
                    if(!this.getClass().getSimpleName().equals("TandC")) {
                        intent.putExtra("Username", getAccName);
                        intent.putExtra("Email", getEmail);
                        startActivity(intent);
                        finish();
                    }
                } else if(itemId == R.id.logOut){
                    DatabaseHelperQuiz db = new DatabaseHelperQuiz(UserManual.this);
                    int totalScore = db.getTotalScore(getEmail);
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserManual.this)
                            .setTitle("Are you sure you want to log out?")
                            .setMessage(getAccName + ", you have overall " + totalScore + " points")
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseHelperLogin db = new DatabaseHelperLogin(UserManual.this);
                                    db.logoutUser(getEmail);
                                    Intent intent = new Intent(UserManual.this, LogInSignUp.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                return false;
            }
        });
    }
}