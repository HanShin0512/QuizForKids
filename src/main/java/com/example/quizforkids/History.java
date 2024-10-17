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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class History extends AppCompatActivity {

    DrawerLayout drawerLayout_history;
    NavigationView navigationView_history;
    ImageButton nav_btn_history;
    TextView totalPoint, acc_name, acc_email, filterDate, filterArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        drawerLayout_history = findViewById(R.id.drawerLayout_history);
        nav_btn_history = findViewById(R.id.nav_btn_history);
        navigationView_history = findViewById(R.id.navigationView_history);
        totalPoint = findViewById(R.id.totalPoints);
        filterDate = findViewById(R.id.filterDate);
        filterArea = findViewById(R.id.filterArea);
        View headerView = getLayoutInflater().inflate(R.layout.drawer_header, navigationView_history, false);

        // Find acc_name and acc_email within the headerView
        acc_name = headerView.findViewById(R.id.acc_name);
        acc_email = headerView.findViewById(R.id.acc_email);

        // Add the header view to the navigationView
        navigationView_history.addHeaderView(headerView);

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

        //display record
        DatabaseHelperQuiz dbHelper = new DatabaseHelperQuiz(this);
        List<String> scores = dbHelper.displayScoreHistory(getEmail);
        for (String score : scores) {
            // Display score
            TextView textView = new TextView(this);
            textView.setText(score);
            // Add textView
            LinearLayout historyLayout = findViewById(R.id.historyLayout);
            historyLayout.addView(textView);
        }
        int total = dbHelper.getTotalScore(getEmail);
        totalPoint.setText(total + " points Total");

        //when filter date is pressed
        filterDate.setOnClickListener(new View.OnClickListener() {
            boolean isAsc = true;

            @Override
            public void onClick(View v) {
                LinearLayout historyLayout = findViewById(R.id.historyLayout);
                // Remove any existing TextView before adding new ones
                historyLayout.removeAllViews();

                if (isAsc) {
                    List<String> scores = dbHelper.filterByDateDESC(getEmail);
                    for (String score : scores) {
                        // Display score
                        TextView textView = new TextView(History.this);
                        textView.setText(score);
                        // Add textView
                        historyLayout.addView(textView);
                    }
                } else {
                    List<String> scores = dbHelper.filterByDateASC(getEmail);
                    for (String score : scores) {
                        // Display score
                        TextView textView = new TextView(History.this);
                        textView.setText(score);
                        // Add textView
                        historyLayout.addView(textView);
                    }
                }
                int total = dbHelper.getTotalScore(getEmail);
                totalPoint.setText(total + " points Total");
                isAsc = !isAsc;
            }
        });

//when filter quiz area is pressed
        filterArea.setOnClickListener(new View.OnClickListener() {
            boolean isAnimal = true;

            @Override
            public void onClick(View v) {
                LinearLayout historyLayout = findViewById(R.id.historyLayout);
                // Remove any existing TextView before adding new ones
                historyLayout.removeAllViews();

                if (isAnimal) {
                    List<String> scores = dbHelper.filterByQuizType(getEmail,"Animals");
                    for (String score : scores) {
                        // Display score
                        TextView textView = new TextView(History.this);
                        textView.setText(score);
                        // Add textView
                        historyLayout.addView(textView);
                    }
                } else {
                    List<String> scores = dbHelper.filterByQuizType(getEmail,"Cartoons");
                    for (String score : scores) {
                        // Display score
                        TextView textView = new TextView(History.this);
                        textView.setText(score);
                        // Add textView
                        historyLayout.addView(textView);
                    }
                }
                int total = dbHelper.getTotalScore(getEmail);
                totalPoint.setText(total + " points Total");
                isAnimal = !isAnimal;
            }
        });

        //open navigation bar
        nav_btn_history.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout_history.open();
            }
        });

        //control which navigation icon does what
        navigationView_history.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                    DatabaseHelperQuiz db = new DatabaseHelperQuiz(History.this);
                    int totalScore = db.getTotalScore(getEmail);
                    AlertDialog.Builder builder = new AlertDialog.Builder(History.this)
                            .setTitle("Are you sure you want to log out?")
                            .setMessage(getAccName + ", you have overall " + totalScore + " points")
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseHelperLogin db = new DatabaseHelperLogin(History.this);
                                    db.logoutUser(getEmail);
                                    Intent intent = new Intent(History.this, LogInSignUp.class);
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