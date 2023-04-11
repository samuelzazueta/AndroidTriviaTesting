package com.vicentearmenta.androidtriviatesting.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vicentearmenta.androidtriviatesting.R;
import com.vicentearmenta.androidtriviatesting.database.DatabaseOperations;
import com.vicentearmenta.androidtriviatesting.databinding.ActivityQuestion2Binding;
import com.vicentearmenta.androidtriviatesting.databinding.ActivityQuestion3Binding;
import com.vicentearmenta.androidtriviatesting.models.Answer;
import com.vicentearmenta.androidtriviatesting.models.Question;

import java.util.List;

public class Question3Activity extends AppCompatActivity {

    ActivityQuestion3Binding binding;

    DatabaseOperations mDBOperations;

    String userId;

    String questionsAlreadyAsked;

    int finalCorrectAnswerRdBtn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestion3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        userId = intent.getStringExtra("USERID");
        questionsAlreadyAsked = intent.getStringExtra("QUESTIONS");

        mDBOperations = new DatabaseOperations(Question3Activity.this);
        binding.backButton.setVisibility(View.INVISIBLE);

        binding.nextButton.setEnabled(false);

        Question question = mDBOperations.getNextQuestion(questionsAlreadyAsked); // Traer lógica

        binding.questionText.setText(question.getQuestionText());

        String drawableName = "image" + question.getQuestionId();

        binding.imagePlaceholder.setImageResource(getResources().getIdentifier(drawableName,
                "drawable", getPackageName()));

        questionsAlreadyAsked = questionsAlreadyAsked + "," + question.getQuestionId();

        List<Answer> answers = question.getAllAnswers();

        for (int i = 0; i < 4; i++) {
            RadioButton tempRadioButton = (RadioButton) binding.rgAnswers.getChildAt(i);
            Answer tempAnswer = answers.get(i);

            if (question.getCorrectAnswer().equals(tempAnswer.getAnswerId())) {
                finalCorrectAnswerRdBtn = tempRadioButton.getId();
            }

            tempRadioButton.setText(tempAnswer.getAnswerText());
        }

        binding.rgAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                binding.nextButton.setEnabled(true); // Button of next
                int scoreUpgrade = evaluateAnswerSelection(group, checkedId);
                if (scoreUpgrade == 1){
                    mDBOperations.updateScore2(userId);
                };

            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Question3Activity.this, Question4Activity.class);
                intent.putExtra("USERID", userId);
                intent.putExtra("QUESTIONS", questionsAlreadyAsked);
                startActivity(intent);
            }
        });

    }

    public int evaluateAnswerSelection(RadioGroup radioGroup, int selectedAnswer){
        int score = 0;

        RadioButton tempRdButton = findViewById(finalCorrectAnswerRdBtn);
        tempRdButton.setButtonDrawable(R.drawable.ic_correct);
        tempRdButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#9AD680")));

        if(selectedAnswer == finalCorrectAnswerRdBtn){
            score++;
        } else {
            RadioButton tempRdButton2 = findViewById(selectedAnswer);
            tempRdButton2.setButtonDrawable(R.drawable.ic_wrong);
            tempRdButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#93000A")));
        }

        for(int i =0; i<radioGroup.getChildCount(); i++){
            radioGroup.getChildAt(i).setClickable(false);
        }

        return score;
    }
}