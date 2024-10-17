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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class Feedback extends AppCompatActivity {

    ImageView star1, star2, star3, star4, star5;
    EditText userFeedback;
    Button submit_feedback;
    DrawerLayout drawerLayout;
    NavigationView navigationView_feedback;
    TextView acc_name, acc_email;
    ImageButton nav_btn_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        drawerLayout = findViewById(R.id.drawerLayout_feedback);
        nav_btn_feedback = findViewById(R.id.nav_btn_feedback);
        navigationView_feedback = findViewById(R.id.navigationView_feedback);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        userFeedback = findViewById(R.id.userFeedback);
        submit_feedback = findViewById(R.id.submit_feedback);
        View headerView = getLayoutInflater().inflate(R.layout.drawer_header, navigationView_feedback, false);

        // Find acc_name and acc_email within the headerView
        acc_name = headerView.findViewById(R.id.acc_name);
        acc_email = headerView.findViewById(R.id.acc_email);

        // Add the header view to the navigationView
        navigationView_feedback.addHeaderView(headerView);

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


        star1.setOnClickListener(new View.OnClickListener() {
            boolean filled = false;
            @Override
            public void onClick(View v) {
                if(!filled){
                    star1.setImageResource(R.drawable.filledstar);
                } else {
                    star1.setImageResource(R.drawable.unfilledstar);
                }
                filled = !filled;
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            boolean filled = false;
            @Override
            public void onClick(View v) {
                if(!filled) {
                    star1.setImageResource(R.drawable.filledstar);
                    star2.setImageResource(R.drawable.filledstar);
                } else {
                    star1.setImageResource(R.drawable.unfilledstar);
                    star2.setImageResource(R.drawable.unfilledstar);
                }
                filled = !filled;
            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            boolean filled = false;
            @Override
            public void onClick(View v) {
                if(!filled) {
                    star1.setImageResource(R.drawable.filledstar);
                    star2.setImageResource(R.drawable.filledstar);
                    star3.setImageResource(R.drawable.filledstar);
                } else {
                    star1.setImageResource(R.drawable.unfilledstar);
                    star2.setImageResource(R.drawable.unfilledstar);
                    star3.setImageResource(R.drawable.unfilledstar);
                }
                filled = !filled;
            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            boolean filled = false;
            @Override
            public void onClick(View v) {
                if(!filled) {
                    star1.setImageResource(R.drawable.filledstar);
                    star2.setImageResource(R.drawable.filledstar);
                    star3.setImageResource(R.drawable.filledstar);
                    star4.setImageResource(R.drawable.filledstar);
                } else {
                    star1.setImageResource(R.drawable.unfilledstar);
                    star2.setImageResource(R.drawable.unfilledstar);
                    star3.setImageResource(R.drawable.unfilledstar);
                    star4.setImageResource(R.drawable.unfilledstar);
                }
                filled = !filled;
            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            boolean filled = false;
            @Override
            public void onClick(View v) {
                if(!filled) {
                    star1.setImageResource(R.drawable.filledstar);
                    star2.setImageResource(R.drawable.filledstar);
                    star3.setImageResource(R.drawable.filledstar);
                    star4.setImageResource(R.drawable.filledstar);
                    star5.setImageResource(R.drawable.filledstar);
                } else {
                    star1.setImageResource(R.drawable.unfilledstar);
                    star2.setImageResource(R.drawable.unfilledstar);
                    star3.setImageResource(R.drawable.unfilledstar);
                    star4.setImageResource(R.drawable.unfilledstar);
                    star5.setImageResource(R.drawable.unfilledstar);
                }
                filled = !filled;
            }
        });

        submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFeedback.setText("");
            }
        });

        //open navigation bar
        nav_btn_feedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.open();
            }
        });

        //control which navigation icon does what
        navigationView_feedback.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                    DatabaseHelperQuiz db = new DatabaseHelperQuiz(Feedback.this);
                    int totalScore = db.getTotalScore(getEmail);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Feedback.this)
                            .setTitle("Are you sure you want to log out?")
                            .setMessage(getAccName + ", you have overall " + totalScore + " points")
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseHelperLogin db = new DatabaseHelperLogin(Feedback.this);
                                    db.logoutUser(getEmail);
                                    Intent intent = new Intent(Feedback.this, LogInSignUp.class);
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