package com.example.myfirstapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    private Random random = new Random();
    private int userScore = 0;
    private int cpuScore = 0;
    private int userTurnScore = 0;
    private int cpuTurnScore = 0;
    private int[] userDiceImages = new int[6];
    private int[] cpuDiceImages = new int[6];

    private long startTime = 0;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable(){
        @Override
        public void run() {
            computerTurn();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reset(null);

        userDiceImages[0] = R.drawable.dice1;
        userDiceImages[1] = R.drawable.dice2;
        userDiceImages[2] = R.drawable.dice3;
        userDiceImages[3] = R.drawable.dice4;
        userDiceImages[4] = R.drawable.dice5;
        userDiceImages[5] = R.drawable.dice6;

        cpuDiceImages[0] = R.drawable.dice_cpu1;
        cpuDiceImages[1] = R.drawable.dice_cpu2;
        cpuDiceImages[2] = R.drawable.dice_cpu3;
        cpuDiceImages[3] = R.drawable.dice_cpu4;
        cpuDiceImages[4] = R.drawable.dice_cpu5;
        cpuDiceImages[5] = R.drawable.dice_cpu6;
    }

    /** Called when the user clicks the HoldScore button */
    public void hold(View view) {
        userScore += userTurnScore;
        TextView userScoreText = (TextView) findViewById(R.id.user_score);
        userScoreText.setText("" + userScore);

        TextView turnScoreText = (TextView) findViewById(R.id.turn_score);

        TextView gameStatusText = (TextView) findViewById(R.id.game_status);
        gameStatusText.setText("");

        if(userScore < 100) {
            userTurnScore = 0;
            turnScoreText.setText("0");
            userScoreText.setText("" + userScore);
            gameStatusText.setText("You hold.");
            enableButtons(false, false, false);
            timerHandler.postDelayed(timerRunnable, 500);
        } else {
            userScoreText.setText("100");
            enableButtons(false, false, true);
            displayVictoryMessage("Congratulations! Victory is yours! :)");
        }
    }

    /** Called when the user clicks the Roll button */
    public void roll(View view) {
        int rollVal = random.nextInt(6) + 1;

        // update imageview drawable
        ImageView diceImage = (ImageView) findViewById(R.id.dice_image);
        diceImage.setImageResource(userDiceImages[rollVal - 1]);
        TextView turnScoreText = (TextView) findViewById(R.id.turn_score);

        TextView gameStatusText = (TextView) findViewById(R.id.game_status);
        gameStatusText.setText("");

        // update turn score
        if(rollVal == 1) {
            userTurnScore = 0;
            turnScoreText.setText("0");
            gameStatusText.setText("Your turn has ended.");
            enableButtons(false, false, false);
            timerHandler.postDelayed(timerRunnable, 500);
        } else {
            userTurnScore += rollVal;
            turnScoreText.setText("" + userTurnScore);
        }
    }

    /** Called when the user clicks the Reset button */
    public void reset(View view) {
        // enable buttons
        enableButtons(true, true, true);

        // reset game scores
        userScore = 0;
        cpuScore = 0;
        userTurnScore = 0;
        cpuTurnScore = 0;

        // reset labels
        TextView userScoreText = (TextView) findViewById(R.id.user_score);
        userScoreText.setText("0");
        TextView cpuScoreText = (TextView) findViewById(R.id.cpu_score);
        cpuScoreText.setText("0");
        TextView turnScoreText = (TextView) findViewById(R.id.turn_score);
        turnScoreText.setText("X");
        TextView gameStatusText = (TextView) findViewById(R.id.game_status);
        gameStatusText.setText("");
        displayVictoryMessage("");
    }

    public void computerTurn() {
        TextView cpuScoreText = (TextView) findViewById(R.id.cpu_score);
        TextView turnScoreText = (TextView) findViewById(R.id.turn_score);
        TextView gameStatusText = (TextView) findViewById(R.id.game_status);

        gameStatusText.setText("");
        int rollVal = random.nextInt(6) + 1;

        cpuTurnScore += rollVal;
        boolean hold = cpuTurnScore < 20 && (cpuScore + cpuTurnScore) < 100 ? false : true;

        ImageView diceImage = (ImageView) findViewById(R.id.dice_image);
        diceImage.setImageResource(cpuDiceImages[rollVal - 1]);

        if(rollVal != 1) {
            turnScoreText.setText("" + cpuTurnScore);

            if(hold) {
                cpuScore += cpuTurnScore;
                cpuTurnScore = 0;

                if(cpuScore >= 100) {
                    cpuScoreText.setText("100");
                    enableButtons(false, false, true);
                    displayVictoryMessage("Unlucky! Computer has won! :(");
                } else {
                    cpuScoreText.setText("" + cpuScore);
                    turnScoreText.setText("0");
                    gameStatusText.setText("Computer holds.");
                    enableButtons(true, true, true);
                }
            } else {
                timerHandler.postDelayed(timerRunnable, 500);
            }
        } else {
            cpuTurnScore = 0;
            turnScoreText.setText("0");
            gameStatusText.setText("Computer's turn has ended.");
            enableButtons(true, true, true);
        }
    }

    public void enableButtons(boolean roll, boolean hold, boolean reset) {
        Button rollBtn = (Button) findViewById(R.id.btn_roll);
        Button holdBtn = (Button) findViewById(R.id.btn_hold);
        Button resetBtn = (Button) findViewById(R.id.btn_reset);

        rollBtn.setEnabled(roll);
        holdBtn.setEnabled(hold);
        resetBtn.setEnabled(reset);
    }

    public void displayVictoryMessage(String msg) {
        TextView victorMsgText = (TextView) findViewById(R.id.victorMsg);
        victorMsgText.setText(msg);
    }
}
