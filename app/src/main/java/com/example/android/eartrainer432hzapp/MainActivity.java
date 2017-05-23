package com.example.android.eartrainer432hzapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends AppCompatActivity {

    int number_of_problems = 12;

    int problem_counter = 1;
    int correct_counter = 0;
    int incorrect_counter = 0;
    int hint_counter = 0;

    /**
     * Declarations & initializations of flags for the "Hint" button and the
     * "Show Multiple Choice" button
     */
    int hint_flag = 0;
    int show_multiple_choice_flag = 0;

    /**
     * Declaration for placeholder for current hint message
     */
    String hint;

    /**
     * Declares and the sound button placeholder
     */
    ImageButton soundButton;

    /**
     * Declare and initializes violin sound placeholder
     */
    MediaPlayer violin_sound;

    /**
     * Declarations of four multiple choice value placeholders
     */
    String v1, v2, v3, v4;

    public static final String correct_msg = "com.example.android.eartrainer432hzapp.correct_msg";
    public static final String incorrect_msg = "com.example.android.eartrainer432hzapp.incorrect_msg";
    public static final String hints_msg = "com.example.android.eartrainer432hzapp.hints_msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayProblemNumber();

        // Sets the current global values for the current problem
        setMCValuesAndHints();

        soundButton = (ImageButton) this.findViewById(R.id.sound_button);
        setSound();
        playSoundOnClick(soundButton);
        playSoundNew();
    }


    /**
     * Checks if the user's answer is correct and updates the problem counter and the
     * correct_counter or incorrect_counter appropriately.
     *
     * @param view
     */

    public void submit_button(View view) {
        // Determines the user's answer
        EditText answerField = (EditText) findViewById(R.id.user_input);
        String user_answer = answerField.getText().toString();

        /**
         * Input the user's answer, user_answer as an argument into update_scores in order to
         * determine if the user's answer is correct, and proceed to update scores accordingly
         */
        update_scores(user_answer);

        increment_problem_number();
        hint_flag = 0;

        if (problem_counter > number_of_problems) {
            gameOver();
        } else {
            nextProblem();
            playSoundNew();
        }
    }

    private void nextProblem() {
        setMCValuesAndHints();
        displayProblemNumber();
        hide_multiple_choice();
        show_multiple_choice_flag = 0;
        resetEditText();
        setSound();
    }

    /**
     * Checks if the answer is correct for current problem number
     */
    private void update_scores(String user_answer) {
        boolean evaluation = isAnswerCorrect(user_answer);

        if (evaluation) {
            increment_number_correct();
        } else {
            increment_number_incorrect();
        }
    }

    /**
     * Increments the question/problem number counter, which is then displayed to the user
     *
     * @param view
     */
    private void increment_problem_number() {
        problem_counter += 1;
    }

    /**
     * Increments the number of questions answered correctly
     *
     * @param view
     */
    private void increment_number_correct() {
        correct_counter += 1;
        displayCorrectNumber();
    }

    /**
     * Increments the scorecounter for the number of questions answered incorrectly.
     *
     * @param view
     */
    private void increment_number_incorrect() {
        incorrect_counter += 1;
        displayIncorrectNumber();
    }

    /**
     * Increments the counter regarding the number of hints shown to the user.
     *
     * @param view
     */
    public void increment_hints_shown(View view) {
        if (hint_flag == 0) {
            callHintMsg();
            hint_counter += 1;
            displayHintsShown();
            hint_flag = 1;
        } else {
            Toast.makeText(getApplicationContext(), R.string.hint_button_string,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays the question/problem number to user.
     */
    private void displayProblemNumber() {
        TextView problemNumberTextView = (TextView) findViewById(R.id.problem_number_tv);
        problemNumberTextView.setText(problem_counter + ".)");
    }

    /**
     * Displays the number of questions answered correctly by the user
     */
    private void displayCorrectNumber() {
        TextView numberCorrectTextView = (TextView) findViewById(R.id.number_correct);
        numberCorrectTextView.setText("Correct: " + correct_counter);
    }

    /**
     * Displays the number of questions answered incorrectly by the user
     */
    private void displayIncorrectNumber() {
        TextView numberIncorrectTextView = (TextView) findViewById(R.id.number_incorrect);
        numberIncorrectTextView.setText("Incorrect: " + incorrect_counter);
    }

    /**
     * Displays the number of hints displayed to the user.
     */
    private void displayHintsShown() {
        TextView numberHintsShown = (TextView) findViewById(R.id.number_hints_shown);
        numberHintsShown.setText("Hints: " + hint_counter);
    }

    /**
     * Resets the EditText field
     */
    private void resetEditText() {
        EditText editTextField = (EditText) findViewById(R.id.user_input);
        editTextField.setText("");
    }

    /**
     * "Hides" the multiple choice values at the onset of new question
     */
    private void hide_multiple_choice() {
        apply_show_mc_values("", "", "", "");
    }

    /**
     * Displays the multiple-choice values for the current question
     */
    public void show_multiple_choice(View view) {

        if (show_multiple_choice_flag == 0) {
            // Sets those values to the text for the four multiple choice radio buttons
            apply_show_mc_values(v1, v2, v3, v4);
            show_multiple_choice_flag = 1;
        } else {
            Toast.makeText(getApplicationContext(), "Multiple choice values already shown",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sets the user_input EditText view to the text of the multiple choice value if
     * multiple choice radio button is clicked and multiple choice values are currently shown
     *
     * Pseudocode:
     * if (show_multiple_choice_flag = 1) {
     *     if (radiobutton1.isChecked()) {
     *         set text of user_input EditText view to v1
     *     }
     *     else if (radiobutton2.isChecked()) {
     *         set text of user_input EditText view to v2
     *     }
     *     else if (radiobutton3.isChecked()) {
     *         set text of user_input EditText view to v3
     *     }
     *     else if (radiobutton4.isChecked()) {
     *         set text of user_input EditText view to v4
     *     }
     * }
     *
     */
    public void set_rb_value1(View view) {
        if (show_multiple_choice_flag == 1) {
            EditText editTextField = (EditText) findViewById(R.id.user_input);
            editTextField.setText(v1);
        } else {
            Toast.makeText(getApplicationContext(), R.string.mc_values_msg,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void set_rb_value2(View view) {
        if (show_multiple_choice_flag == 1) {
            EditText editTextField = (EditText) findViewById(R.id.user_input);
            editTextField.setText(v2);
        } else {
            Toast.makeText(getApplicationContext(), R.string.mc_values_msg,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void set_rb_value3(View view) {
        if (show_multiple_choice_flag == 1) {
            EditText editTextField = (EditText) findViewById(R.id.user_input);
            editTextField.setText(v3);
        } else {
            Toast.makeText(getApplicationContext(), R.string.mc_values_msg,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void set_rb_value4(View view) {
        if (show_multiple_choice_flag == 1) {
            EditText editTextField = (EditText) findViewById(R.id.user_input);
            editTextField.setText(v4);
        } else {
            Toast.makeText(getApplicationContext(), R.string.mc_values_msg,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * IDEA TO REPLACE ABOVE 4 set_rb_value METHODS with a SINGLE METHOD
     *
     * PSEUDOCODE:
     * public void set_rb_values(radio_button_number) {
     *     String choice;
     *
     *     if (radio_button_number == 1) {
     *         choice = v1;
     *     }
     *     else if (radio_button_number == 2) {
     *         choice = v2;
     *     }
     *     else if (radio_button_number == 3) {
     *         choice = v3;
     *     }
     *     else if (radio_button_number == 4) {
     *         choice = v4;
     *     }
     *
     *     EditText editTextField = (EditText) findViewById(R.id.user_input);
            editTextField.setText(choice);
     * }
     *
     */

    /**
     * Has the four multiple choice values as input parameters, applies them to the four
     * radio button text values, and displays them
     */
    private void apply_show_mc_values(String value1, String value2, String value3, String value4) {
        RadioButton multiple_choice_values1 = (RadioButton) findViewById(R.id.radio_button_1);
        multiple_choice_values1.setText(value1);
        RadioButton multiple_choice_values2 = (RadioButton) findViewById(R.id.radio_button_2);
        multiple_choice_values2.setText(value2);
        RadioButton multiple_choice_values3 = (RadioButton) findViewById(R.id.radio_button_3);
        multiple_choice_values3.setText(value3);
        RadioButton multiple_choice_values4 = (RadioButton) findViewById(R.id.radio_button_4);
        multiple_choice_values4.setText(value4);
    }


    /**
     * Displays the total correct, incorrect, and hint scores to the user, and then proceeds
     * to calculate the total score and prompt the user on whether they want to play again
     */
    private void gameOver() {
        Intent gameEnd = new Intent(this, GameOverActivity.class);
        gameEnd.putExtra(correct_msg, "Correct: " + correct_counter);
        gameEnd.putExtra(incorrect_msg, "Incorrect: " + incorrect_counter);
        gameEnd.putExtra(hints_msg, "Hints: " + hint_counter);
        // calculate total score
        // gameEnd.putExtra(TOTAL_SCORE, "Total Score: " + total_score);
        startActivity(gameEnd);
    }


    private void setMCValuesAndHints() {
        if (problem_counter == 1) {
            v1 = "C";
            v2 = "G";
            v3 = "Ab";
            v4 = "F#";

            hint = getString(R.string.hint1);
        }
        else if (problem_counter == 2) {
            v1 = "F";
            v2 = "Gb";
            v3 = "Bb";
            v4 = "F#";

            hint = getString(R.string.hint2);
        }
        else if (problem_counter == 3) {
            v1 = "B";
            v2 = "Gb";
            v3 = "C";
            v4 = "G#";

            hint = getString(R.string.hint3);
        }
        else if (problem_counter == 4) {
            v1 = "D#";
            v2 = "Ab";
            v3 = "A";
            v4 = "E";

            hint = getString(R.string.hint4);
        }
        else if (problem_counter == 5) {
            v1 = "C#";
            v2 = "Eb";
            v3 = "Ab";
            v4 = "E";

            hint = getString(R.string.hint5);
        }
        else if (problem_counter == 6) {
            v1 = "C";
            v2 = "G";
            v3 = "D";
            v4 = "F#";

            hint = getString(R.string.hint6);
        }
        else if (problem_counter == 7) {
            v1 = "C";
            v2 = "D";
            v3 = "F";
            v4 = "F#";

            hint = getString(R.string.hint7);
        }
        else if (problem_counter == 8) {
            v1 = "Ab";
            v2 = "Bb";
            v3 = "C";
            v4 = "A#";

            hint = getString(R.string.hint8);
        }
        else if (problem_counter == 9) {
            v1 = "E";
            v2 = "G#";
            v3 = "Eb";
            v4 = "D";

            hint = getString(R.string.hint9);
        }
        else if (problem_counter == 10) {
            v1 = "D#";
            v2 = "B";
            v3 = "Ab";
            v4 = "G#";

            hint = getString(R.string.hint10);
        }
        else if (problem_counter == 11) {
            v1 = "B";
            v2 = "A";
            v3 = "E";
            v4 = "F#";

            hint = getString(R.string.hint11);
        }
        else if (problem_counter == 12) {
            v1 = "Gb";
            v2 = "Ab";
            v3 = "G";
            v4 = "C";

            hint = getString(R.string.hint12);
        }
    }

//    Button one = (Button) this.findViewById(R.id.button1);
//    final MediaPlayer mp = MediaPlayer.create(this, R.raw.soho);
//one.setOnClickListener(new OnClickListener(){
//
//        public void onClick(View v) {
//            mp.start();
//        }
//    });


    public void playSoundOnClick(View view) {
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                violin_sound.start();
            }
        });
    }

    private void playSoundNew() {
        violin_sound.start();
    }

    private void setSound() {
        if (problem_counter == 1) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_g_432hz);
        } else if (problem_counter == 2) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_f_sharp_432hz);
        } else if (problem_counter == 3) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_b_432hz);
        } else if (problem_counter == 4) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_e_432hz);
        } else if (problem_counter == 5) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_c_sharp_432hz);
        } else if (problem_counter == 6) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_d_432hz);
        } else if (problem_counter == 7) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_f_432hz);
        } else if (problem_counter == 8) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_a_sharp_432hz);
        } else if (problem_counter == 9) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_g_sharp_432hz);
        } else if (problem_counter == 10) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_d_sharp_432hz);
        } else if (problem_counter == 11) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_a_432hz);
        } else if (problem_counter == 12) {
            violin_sound = MediaPlayer.create(this, R.raw.violin_c_432hz);
        }
    }

    private boolean isAnswerCorrect(String user_answer) {
        if (problem_counter == 1) {
            return user_answer.equals("G");
        } else if (problem_counter == 2) {
            return (user_answer.equals("F#") || user_answer.equals("Gb"));
        } else if (problem_counter == 3) {
            return user_answer.equals("B");
        } else if (problem_counter == 4) {
            return user_answer.equals("E");
        } else if (problem_counter == 5) {
            return (user_answer.equals("C#") || user_answer.equals("Db"));
        } else if (problem_counter == 6) {
            return user_answer.equals("D");
        } else if (problem_counter == 7) {
            return user_answer.equals("F");
        } else if (problem_counter == 8) {
            return (user_answer.equals("A#") || user_answer.equals("Bb"));
        } else if (problem_counter == 9) {
            return (user_answer.equals("G#") || user_answer.equals("Ab"));
        } else if (problem_counter == 10) {
            return (user_answer.equals("D#") || user_answer.equals("Eb"));
        } else if (problem_counter == 11) {
            return user_answer.equals("A");
        } else if (problem_counter == 12) {
            return user_answer.equals("C");
        } else {
            return false;
        }
    }

    private void callHintMsg() {
        Toast.makeText(getApplicationContext(), hint,
                Toast.LENGTH_LONG).show();
    }


}
