package com.example.android.eartrainer432hzapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent gameEnd = getIntent();
        String num_correct_msg = gameEnd.getStringExtra(MainActivity.correct_msg);
        String num_incorrect_msg = gameEnd.getStringExtra(MainActivity.incorrect_msg);
        String num_hints_msg = gameEnd.getStringExtra(MainActivity.hints_msg);

        TextView scoresCorrectTV = (TextView) findViewById(R.id.scores_correct_tv);
        scoresCorrectTV.setText(num_correct_msg);

        TextView scoresIncorrectTV = (TextView) findViewById(R.id.scores_incorrect_tv);
        scoresIncorrectTV.setText(num_incorrect_msg);

        TextView scoresHintsTV = (TextView) findViewById(R.id.scores_hints_tv);
        scoresHintsTV.setText(num_hints_msg);
    }

    public void playAgain(View view) {
        Intent MainActivity = new Intent(this, MainActivity.class);
        startActivity(MainActivity);
    }
}
