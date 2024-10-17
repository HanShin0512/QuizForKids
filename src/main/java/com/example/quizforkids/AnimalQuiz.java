package com.example.quizforkids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.LinkedList;
import java.util.Random;

public class AnimalQuiz extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton nav_btn_animal, backwardArrow, forwardArrow;
    TextView acc_name, acc_email;
    ImageView guess_animal_img;
    EditText answerQuizAnimal;
    Button submit_btn;
    int score_animal = 0;
    int question_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_quiz);

        //this.deleteDatabase("score_record_animal.db");

        drawerLayout = findViewById(R.id.drawerLayout);
        nav_btn_animal = findViewById(R.id.nav_btn_animal);
        navigationView = findViewById(R.id.navigationView);
        guess_animal_img = findViewById(R.id.guess_animal_img);
        answerQuizAnimal = findViewById(R.id.answerQuizAnimal);
        backwardArrow = findViewById(R.id.backArrow);
        forwardArrow = findViewById(R.id.forwardArrow);
        submit_btn = findViewById(R.id.submit_btn);
        View headerView = getLayoutInflater().inflate(R.layout.drawer_header, navigationView, false);

        // Find acc_name and acc_email within the headerView
        acc_name = headerView.findViewById(R.id.acc_name);
        acc_email = headerView.findViewById(R.id.acc_email);

        // Add the header view to the navigationView
        navigationView.addHeaderView(headerView);

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

        //make submit button invisible until the last question
        submit_btn.setVisibility(View.INVISIBLE);

        //generate quiz questions
        LinkedList<Integer> generatedQuestions = new LinkedList<>();
        while(generatedQuestions.size() < 4){
            int newQuestion = generateQuestion();
            if(!generatedQuestions.contains(newQuestion)){
                generatedQuestions.add(newQuestion);
            }
        }

        // make backward arrow invisible on the first page
        backwardArrow.setVisibility(View.INVISIBLE);

        //make first quiz
        setQuizImg(generatedQuestions.get(0));

        //next quiz
        forwardArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAnswer(generatedQuestions.get(question_number));
                Log.d("Score:", "score is " + score_animal );
                Log.d("Number", "question number: " + String.valueOf(question_number));
                question_number += 1;
                setQuizImg(generatedQuestions.get(question_number));

                //handle backward arrow visibility
                if(question_number == 0){
                    backwardArrow.setVisibility(View.INVISIBLE);
                } else {
                    backwardArrow.setVisibility(View.VISIBLE);
                }

                //handle forward arrow visibility
                if(question_number == 3){
                    forwardArrow.setVisibility(View.INVISIBLE);
                    submit_btn.setVisibility(View.VISIBLE);
                } else {
                    forwardArrow.setVisibility(View.VISIBLE);
                    submit_btn.setVisibility(View.INVISIBLE);
                }
            }
        });

        //previous quiz
        backwardArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question_number -= 1;
                score_animal -= 1;
                Log.d("Score:", "score is " + score_animal );
                Log.d("Number", "question number: " + String.valueOf(question_number));
                setQuizImg(generatedQuestions.get(question_number));

                //handle backward arrow visibility
                if(question_number == 0){
                    backwardArrow.setVisibility(View.INVISIBLE);
                } else {
                    backwardArrow.setVisibility(View.VISIBLE);
                }

                //handle forward arrow visibility
                if(question_number == 3){
                    forwardArrow.setVisibility(View.INVISIBLE);
                    submit_btn.setVisibility(View.VISIBLE);
                } else {
                    forwardArrow.setVisibility(View.VISIBLE);
                    submit_btn.setVisibility(View.INVISIBLE);
                }
            }
        });

        //submit button
        DatabaseHelperQuiz db_animal = new DatabaseHelperQuiz(this);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check last answer
                checkAnswer(generatedQuestions.get(3)); // check last question answer
                Log.d("Score:", "score is " + score_animal );
                Log.d("Number", "question number: " + String.valueOf(question_number));

                //get incorrect score
                int incorrectScore = 4 - score_animal;
                int total_score_animal = (score_animal * 3) - incorrectScore; // get current score

                //add score to the database
                db_animal.addScore(getEmail,"Animals",total_score_animal);

                int totalScore = db_animal.getTotalScore(getEmail); // get overall score

                //show results
                AlertDialog.Builder builder = new AlertDialog.Builder(AnimalQuiz.this);
                builder.setTitle("Quiz Result")
                        .setMessage("Well done "+getAccName + ", you have finished the “Animals” quiz with "
                                + score_animal +" correct and " + incorrectScore +
                                " incorrect answers or " + total_score_animal + " points for this attempt." +
                                " Overall you have "+ totalScore +" points.")
                        .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //restart score_animal
                                Intent intent = new Intent(AnimalQuiz.this, AnimalQuiz.class);
                                intent.putExtra("Username", getAccName);
                                intent.putExtra("Email", getEmail);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(AnimalQuiz.this, MainActivity.class);
                                intent.putExtra("Username", getAccName);
                                intent.putExtra("Email", getEmail);
                                startActivity(intent);
                                finish();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //open navigation bar
        nav_btn_animal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.open();
            }
        });

        //control which navigation icon does what
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                    int totalScore = db_animal.getTotalScore(getEmail);
                    AlertDialog.Builder builder = new AlertDialog.Builder(AnimalQuiz.this)
                            .setTitle("Are you sure you want to log out?")
                            .setMessage(getAccName + ", you have overall " + totalScore + " points")
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseHelperLogin db = new DatabaseHelperLogin(AnimalQuiz.this);
                                    db.logoutUser(getEmail);
                                    Intent intent = new Intent(AnimalQuiz.this, LogInSignUp.class);
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

    public int generateQuestion(){
        Random random = new Random();
        return random.nextInt(10) + 1;
    }

    public void setQuizImg(int number){

        switch (number){
            case 1:
                guess_animal_img.setImageResource(R.drawable.caterpillar);
                break;
            case 2:
                guess_animal_img.setImageResource(R.drawable.crab);
                break;
            case 3:
                guess_animal_img.setImageResource(R.drawable.horse);
                break;
            case 4:
                guess_animal_img.setImageResource(R.drawable.koala);
                break;
            case 5:
                guess_animal_img.setImageResource(R.drawable.lion);
                break;
            case 6:
                guess_animal_img.setImageResource(R.drawable.owl);
                break;
            case 7:
                guess_animal_img.setImageResource(R.drawable.pig);
                break;
            case 8:
                guess_animal_img.setImageResource(R.drawable.snail);
                break;
            case 9:
                guess_animal_img.setImageResource(R.drawable.snake);
                break;
            case 10:
                guess_animal_img.setImageResource(R.drawable.turtle);
                break;
        }

    }

    //check if answer is correct
    public void checkAnswer(int question){

        //get answer
        String answer = answerQuizAnimal.getText().toString().trim().toLowerCase();

        if(answer.isEmpty()){
            Log.d("tag", "Answer is empty");
            return;
        }

        //check answer correct using question number
        switch (question) {
            case 1:
                if(answer.equals("caterpillar")){
                    score_animal += 1;
                }
                break;
            case 2:
                if(answer.equals("crab")){
                    score_animal += 1;
                }
                break;
            case 3:
                if(answer.equals("horse")){
                    score_animal += 1;
                }
                break;
            case 4:
                if(answer.equals("koala")){
                    score_animal += 1;
                }
                break;
            case 5:
                if(answer.equals("lion")){
                    score_animal += 1;
                }
                break;
            case 6:
                if(answer.equals("owl")){
                    score_animal += 1;
                }
                break;
            case 7:
                if(answer.equals("pig")){
                    score_animal += 1;
                }
                break;
            case 8:
                if(answer.equals("snail")){
                    score_animal += 1;
                }
                break;
            case 9:
                if(answer.equals("snake")){
                    score_animal += 1;
                }
                break;
            case 10:
                if(answer.equals("turtle")){
                    score_animal += 1;
                }
                break;
        }
        answerQuizAnimal.setText("");
    }
}