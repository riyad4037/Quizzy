package com.example.quizzy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {


    ArrayList<Question> questions;
    int index = 0;
    Question question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswers = 0;

    TextView timerTextViewQuizActivity, OptionOne, OptionTwo, OptionThree, OptionFour, QuestionTextView, QuestionCounterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        timerTextViewQuizActivity = findViewById(R.id.timer);
        OptionOne = findViewById(R.id.option_1);
        OptionTwo = findViewById(R.id.option_2);
        OptionThree = findViewById(R.id.option_3);
        OptionFour = findViewById(R.id.option_4);
        QuestionTextView = findViewById(R.id.question);
        QuestionCounterTextView = findViewById(R.id.questionCounter);

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        final String catId = getIntent().getStringExtra("catId");

        Random random = new Random();
        final int rand = random.nextInt(12);

        database.collection("categories").document(catId).collection("questions").whereGreaterThanOrEqualTo("index", rand).orderBy("index").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size()<5){
                    database.collection("categories").document(catId).collection("questions").whereLessThanOrEqualTo("index", rand).orderBy("index").limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot snapshot: queryDocumentSnapshots) {
                                Question question = snapshot.toObject(Question.class);
                                questions.add(question);
                            }
                            setNextQuestion();
                        }
                    });
                } else {
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                        Question question = snapshot.toObject(Question.class);
                        questions.add(question);
                    }
                    setNextQuestion();
                }
            }
        });
        resetTimer();
    }

    void resetTimer() {
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextViewQuizActivity.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

            }
        };
    }

    void showAnswer() {
        if(question.getAnswer().equals(OptionOne.getText().toString()))
            OptionOne.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(OptionTwo.getText().toString()))
            OptionTwo.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(OptionThree.getText().toString()))
            OptionThree.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(OptionFour.getText().toString()))
            OptionFour.setBackground(getResources().getDrawable(R.drawable.option_right));
    }

    void setNextQuestion() {
        if(timer != null)
            timer.cancel();

        timer.start();
        if(index < questions.size()) {
            QuestionCounterTextView.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            QuestionTextView.setText(question.getQuestion());
            OptionOne.setText(question.getOption1());
            OptionTwo.setText(question.getOption2());
            OptionThree.setText(question.getOption3());
            OptionFour.setText(question.getOption4());
        }
    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())) {
            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }

    void reset() {
        OptionOne.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        OptionTwo.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        OptionThree.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        OptionFour.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if(timer!=null)
                    timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);

                break;
            case R.id.nextBtn:
                reset();
                if(index <= questions.size()) {
                    index++;
                    setNextQuestion();
                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    //Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}