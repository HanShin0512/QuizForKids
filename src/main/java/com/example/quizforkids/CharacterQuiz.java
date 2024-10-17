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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CharacterQuiz extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView guess_cartoon_img;
    RadioGroup radioGroup;
    RadioButton option1, option2, option3;
    ImageButton nav_btn_character, acc_photo, backwardArrow, forwardArrow;
    NavigationView navigationView_character;
    Button submit_btn;
    TextView acc_name, acc_email;
    int score_cartoon = 0;
    int question_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_quiz);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView_character = findViewById(R.id.navigationView_character);
        nav_btn_character = findViewById(R.id.nav_btn_character);
        guess_cartoon_img = findViewById(R.id.guess_cartoon_img);
        radioGroup = findViewById(R.id.cartoon_mcq);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        backwardArrow = findViewById(R.id.backArrow_cartoon);
        forwardArrow = findViewById(R.id.forwardArrow_cartoon);
        submit_btn = findViewById(R.id.submit_btn);
        View headerView = getLayoutInflater().inflate(R.layout.drawer_header, navigationView_character, false);

        // Find acc_name and acc_email within the headerView
        acc_name = headerView.findViewById(R.id.acc_name);
        acc_email = headerView.findViewById(R.id.acc_email);

        // Add the header view to the navigationView
        navigationView_character.addHeaderView(headerView);

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

        //make 4 random questions
        List<Integer> generatedQuestions = new ArrayList<>();
        while(generatedQuestions.size() <4){
            int question = genRandomNum();
            if(!generatedQuestions.contains(question)){
                generatedQuestions.add(question);
            }
        }

        //make submit button invisible until the last question
        submit_btn.setVisibility(View.INVISIBLE);

        // make backward arrow invisible on the first page
        backwardArrow.setVisibility(View.INVISIBLE);

        //make first quiz
        genQuiz(generatedQuestions.get(0));

        //next quiz
        forwardArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAnswer(generatedQuestions.get(question_number));
                Log.d("Score:", "score is " + score_cartoon );
                Log.d("Number", "question number: " + String.valueOf(question_number));
                question_number += 1;
                genQuiz(generatedQuestions.get(question_number));

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
                score_cartoon -= 1;
                Log.d("Score:", "score is " + score_cartoon );
                Log.d("Number", "question number: " + String.valueOf(question_number));
                genQuiz(generatedQuestions.get(question_number));

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
        DatabaseHelperQuiz db_cartoon = new DatabaseHelperQuiz(this);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check last answer
                checkAnswer(generatedQuestions.get(3)); // check last question answer
                Log.d("Score:", "score is " + score_cartoon );
                Log.d("Number", "question number: " + String.valueOf(question_number));

                //get incorrect score
                int incorrectScore = 4 - score_cartoon;
                int total_score_cartoon = (score_cartoon * 3) - incorrectScore; //get current point

                //add score to the database
                db_cartoon.addScore(getEmail, "Cartoons",total_score_cartoon);

                int totalScore = db_cartoon.getTotalScore(getEmail); // get overall score

                //show results
                AlertDialog.Builder builder = new AlertDialog.Builder(CharacterQuiz.this);
                builder.setTitle("Quiz Result")
                        .setMessage("Well done "+getAccName + ", you have finished the “Cartoons” quiz with "
                                + score_cartoon +" correct and " + incorrectScore +
                                " incorrect answers or " + total_score_cartoon + " points for this attempt." +
                                " Overall you have "+ totalScore +" points.")
                        .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //restart score_animal
                                Intent intent = new Intent(CharacterQuiz.this, CharacterQuiz.class);
                                intent.putExtra("Username", getAccName);
                                intent.putExtra("Email", getEmail);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(CharacterQuiz.this, MainActivity.class);
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
        nav_btn_character.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.open();
            }
        });

        //control which navigation icon does what
        navigationView_character.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                    int totalScore = db_cartoon.getTotalScore(getEmail);
                    AlertDialog.Builder builder = new AlertDialog.Builder(CharacterQuiz.this)
                            .setTitle("Are you sure you want to log out?")
                            .setMessage(getAccName + ", you have overall " + totalScore + " points")
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseHelperLogin db = new DatabaseHelperLogin(CharacterQuiz.this);
                                    db.logoutUser(getEmail);
                                    Intent intent = new Intent(CharacterQuiz.this, LogInSignUp.class);
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

    public int genRandomNum(){
        Random rand = new Random();
        int random = rand.nextInt(10) + 1;
        return random;
    }

    public void genQuiz(int num){
        switch (num){
            case 1:
                guess_cartoon_img.setImageResource(R.drawable.adventuretime);
                option1.setText("Bubblegum Princess");
                option2.setText("Fin and Jake");
                option3.setText("Ice King");
                break;
            case 2:
                guess_cartoon_img.setImageResource(R.drawable.avatar);
                option1.setText("Ang");
                option2.setText("Prince Zuko");
                option3.setText("Yue");
                break;
            case 3:
                guess_cartoon_img.setImageResource(R.drawable.bambi);
                option1.setText("The Great Prince of the Forest");
                option2.setText("Flower");
                option3.setText("Bambi");
                break;
            case 4:
                guess_cartoon_img.setImageResource(R.drawable.dora);
                option1.setText("Dora Márquez");
                option2.setText("Boots");
                option3.setText("Map");
                break;
            case 5:
                guess_cartoon_img.setImageResource(R.drawable.kim);
                option1.setText("Kim Possible");
                option2.setText("Ron Stoppable");
                option3.setText("Shego");
                break;
            case 6:
                guess_cartoon_img.setImageResource(R.drawable.kongfupanda);
                option1.setText("Shifu");
                option2.setText("Po");
                option3.setText("Tigress");
                break;
            case 7:
                guess_cartoon_img.setImageResource(R.drawable.lionking);
                option1.setText("Mufasa");
                option2.setText("Scar");
                option3.setText("Simba");
                break;
            case 8:
                guess_cartoon_img.setImageResource(R.drawable.minion);
                option1.setText("Lucy");
                option2.setText("Gru");
                option3.setText("The minions");
                break;
            case 9:
                guess_cartoon_img.setImageResource(R.drawable.prince);
                option1.setText("Moses");
                option2.setText("The Queen");
                option3.setText("Aaron");
                break;
            case 10:
                guess_cartoon_img.setImageResource(R.drawable.spirit);
                option1.setText("Murphy");
                option2.setText("The Colonel");
                option3.setText("Spirit");
                break;
        }
    }

    public void checkAnswer(int num){
        int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();

        switch (num){
            case 1:
                if (selectedRadioButtonID == R.id.option2) {
                    score_cartoon += 1;
                }
                break;
            case 2:
                if (selectedRadioButtonID == R.id.option1){
                    score_cartoon += 1;
                }
                break;
            case 3:
                if (selectedRadioButtonID == R.id.option3){
                    score_cartoon += 1;
                }
                break;
            case 4:
                if (selectedRadioButtonID == R.id.option1){
                    score_cartoon += 1;
                }
                break;
            case 5:
                if (selectedRadioButtonID == R.id.option1){
                    score_cartoon += 1;
                }
                break;
            case 6:
                if (selectedRadioButtonID == R.id.option2) {
                    score_cartoon += 1;
                }
                break;
            case 7:
                if (selectedRadioButtonID == R.id.option3){
                    score_cartoon += 1;
                }
                break;
            case 8:
                if (selectedRadioButtonID == R.id.option2) {
                    score_cartoon += 1;
                }
                break;
            case 9:
                if (selectedRadioButtonID == R.id.option1){
                    score_cartoon += 1;
                }
                break;
            case 10:
                if (selectedRadioButtonID == R.id.option3){
                    score_cartoon += 1;
                }
                break;
        }
        radioGroup.clearCheck();
    }
}