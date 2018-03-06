package com.grapickel.soyuz.tictactoe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grapickel.soyuz.tictactoe.preferences.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int row =3, column = 3;
    private TextView positionArr[][] = new TextView[row][column];
    private TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;
    private boolean player_1_turn;
    private TextView tvTurns;
    private LinearLayout layout;
    private int count = 1;
    private TextView tvScore1;
    private TextView tvScore2;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private Handler handler;
    private boolean isAlreadyWon;
    private int twistColor = 0;
    private ImageView tvrefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ACTIVITY", ""+"Main");
        setContentView(R.layout.activity_main);
        iniview();
        iniDataSet(positionArr);
        initializeTurns();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("You sure Want to Exit ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yeah",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void iniview() {
        t1 = (TextView) findViewById(R.id.tile1);
        t2 = (TextView) findViewById(R.id.tile2);
        t3 = (TextView) findViewById(R.id.tile3);
        t4 = (TextView) findViewById(R.id.tile4);
        t5 = (TextView) findViewById(R.id.tile5);
        t6 = (TextView) findViewById(R.id.tile6);
        t7 = (TextView) findViewById(R.id.tile7);
        t8 = (TextView) findViewById(R.id.tile8);
        t9 = (TextView) findViewById(R.id.tile9);
        tvrefresh = (ImageView) findViewById(R.id.tv_refresh);
        tvTurns = (TextView) findViewById(R.id.tv_turn);
        layout = (LinearLayout) findViewById(R.id.mainLayout);
        tvScore1 = (TextView) findViewById(R.id.tv_player_1_score);
        tvScore2 = (TextView) findViewById(R.id.tv_player_2_score);

        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        t8.setOnClickListener(this);
        t9.setOnClickListener(this);
        tvrefresh.setOnClickListener(this);
    }

    private void iniDataSet(TextView arr[][]) {
        arr[0][0] = t1;
        arr[0][1] = t2;
        arr[0][2] = t3;
        arr[1][0] = t4;
        arr[1][1] = t5;
        arr[1][2] = t6;
        arr[2][0] = t7;
        arr[2][1] = t8;
        arr[2][2] = t9;
        setColorCombination(this, 1);

    }

    @Override
    public void onClick(View view) {
        switchTurns();
        switch (view.getId()) {
            case R.id.tile1:
                gamePlay(t1);
                break;
            case R.id.tile2:
                gamePlay(t2);
                break;
            case R.id.tile3:
               gamePlay(t3);
                break;
            case R.id.tile4:
                gamePlay(t4);
                break;
            case R.id.tile5:
                gamePlay(t5);
                break;
            case R.id.tile6:
                gamePlay(t6);
                break;
            case R.id.tile7:
                gamePlay(t7);
                break;
            case R.id.tile8:
               gamePlay(t8);
                break;
            case R.id.tile9:
                gamePlay(t9);
                break;
            case R.id.tv_refresh:
                refresh();
                break;
        }
    }

    private void initializeTurns() {
        /**
         * PLAYER 1  turns
         */
        player_1_turn = true;
        tvTurns.setText("Player 1 Turn");

        /**
         * PLAYER 2  turns
         */
    }

    private void switchTurns() {
        if (player_1_turn) {
            tvTurns.setText("Player 2 Turn");
        } else {
            tvTurns.setText("Player 1 Turn");
        }
    }

    private void checkWin(TextView array[][], TextView textView) {
        if (textView.getText() == "X") {
            gameLogic(array, "PLAYER 1 WINS");
        }else if (textView.getText() == "O"){
            gameLogic(array, "PLAYER 2 WINS");
        }
    }

    private void player_1_turn(TextView textView){
        textView.setText("X");
        textView.setEnabled(false);
        player_1_turn = false;
        onTapColorChange(this);
    }

    private void player_2_turn(TextView textView){
        textView.setText("O");
        textView.setEnabled(false);
        player_1_turn = true;
        onTapColorChange(this);
    }

    private void gamePlay(TextView textView){
        if (player_1_turn) {
            player_1_turn(textView);
        } else {
            player_2_turn(textView);
        }
        checkWin(positionArr, textView);
    }

    private void gameLogic(TextView array[][] ,String winner){
        //check rows
        for (int i = 0; i < row; i++) {
            if (! array[i][i].getText().toString().equalsIgnoreCase("") && array[i][0].getText().toString() == array[i][1].getText().toString() && array[i][1].getText().toString() == array[i][2].getText().toString()) {
                tvTurns.setText(winner);
                stopGame(winner);
                isAlreadyWon = true;
            }
        }
        //check columns
        for (int i = 0; i < column; i++){
            if ( ! array[i][i].getText().toString().equalsIgnoreCase("") && array[0][i].getText().toString() == array[1][i].getText().toString() && array[2][i].getText().toString() == array[1][i].getText().toString()) {
                tvTurns.setText(winner);
                stopGame(winner);
                isAlreadyWon = true;
            }
        }
        //check diagonals
        if (!array[0][0].getText().toString().equalsIgnoreCase("") && array[0][0].getText().toString() == array[1][1].getText().toString() && array[1][1].getText().toString() == array[2][2].getText().toString()) {
            tvTurns.setText(winner);
            stopGame(winner);
            isAlreadyWon = true;
        }
        if (!array[0][2].getText().toString().equalsIgnoreCase("") && array[0][2].getText().toString() == array[1][1].getText().toString() && array[1][1].getText().toString() == array[2][0].getText().toString()) {
            tvTurns.setText(winner);
            stopGame(winner);
            isAlreadyWon = true;
        }

        //check draw
        if(!isAlreadyWon && !array[0][0].getText().toString().equalsIgnoreCase("")
                && !array[0][1].getText().toString().equalsIgnoreCase("")
                && !array[0][2].getText().toString().equalsIgnoreCase("")
                && !array[1][0].getText().toString().equalsIgnoreCase("")
                && !array[1][1].getText().toString().equalsIgnoreCase("")
                && !array[1][2].getText().toString().equalsIgnoreCase("")
                && !array[2][0].getText().toString().equalsIgnoreCase("")
                && !array[2][1].getText().toString().equalsIgnoreCase("")
                && !array[2][2].getText().toString().equalsIgnoreCase("")) {
            tvTurns.setText("IT'S A DRAW");
            stopGame("IT'S A DRAW");
        }
    }

    private void onTapColorChange(Context context){
        count = count + 4;
        if(count == 21)
            count = 1;
        setColorCombination(this, count);
    }

    private void setColorCombination(Context context, int count){
        int[] comination = context.getResources().getIntArray(R.array.combination);
        for (int i = 0; i < comination.length ; i++) {
            Log.d("ResourceID" , ""+String.valueOf(comination[i]));
            Log.d("ResourceIDAccent" , ""+R.color.colorAccent);
            for(int j = 0; j < comination.length; j+=2) {
                for (int k = 1; k < comination.length; k += 2) {
                    if(k == j + 1 && j + k == count) {
                        switch (j + k) {
                            case 1:
                                setBackgroundColor(comination,j,k);
                                break;

                            case 5:
                                setBackgroundColor(comination,j,k);
                                break;

                            case 9:
                                setBackgroundColor(comination,j,k);
                                break;

                            case 13:
                                setBackgroundColor(comination,j,k);
                                break;

                            case 17:
                                setBackgroundColor(comination,j,k);
                                break;

                            case 21:
                                setBackgroundColor(comination,j,k);
                                break;
                        }
                    }
                }
            }
        }
    }

    private void setBackgroundColor(int [] comination,int j, int k){
        Log.d("VALUE OF j", ""+j);
        Log.d("VALUE OF k", ""+k);
        Random r = new Random();
        int Low = j;
        int High = k;
        int result = r.nextInt(High-Low + 1) + Low;
        Log.d("VALUE OF result", ""+result);
        backgroundColorChangeLayoutWithAnimation(this, comination, j, k);
        switch (twistColor) {
            //left-bottom
            case 0:
            t1.setBackgroundColor(comination[j]);
            t2.setBackgroundColor(comination[k]);
            t3.setBackgroundColor(comination[k]);
            t4.setBackgroundColor(comination[j]);
            t5.setBackgroundColor(comination[j]);
            t6.setBackgroundColor(comination[k]);
            t7.setBackgroundColor(comination[k]);
            t8.setBackgroundColor(comination[j]);
            t9.setBackgroundColor(comination[j]);
                break;

            //right- bottom
            case 1:
            t1.setBackgroundColor(comination[j]);
            t2.setBackgroundColor(comination[j]);
            t3.setBackgroundColor(comination[k]);
            t4.setBackgroundColor(comination[j]);
            t5.setBackgroundColor(comination[k]);
            t6.setBackgroundColor(comination[k]);
            t7.setBackgroundColor(comination[k]);
            t8.setBackgroundColor(comination[k]);
            t9.setBackgroundColor(comination[j]);
                break;

            //left -top
            case 2:
            t1.setBackgroundColor(comination[j]);
            t2.setBackgroundColor(comination[k]);
            t3.setBackgroundColor(comination[k]);
            t4.setBackgroundColor(comination[k]);
            t5.setBackgroundColor(comination[k]);
            t6.setBackgroundColor(comination[j]);
            t7.setBackgroundColor(comination[k]);
            t8.setBackgroundColor(comination[j]);
            t9.setBackgroundColor(comination[j]);
                break;

            //right - top
            case 3:
            t1.setBackgroundColor(comination[k]);
            t2.setBackgroundColor(comination[k]);
            t3.setBackgroundColor(comination[j]);
            t4.setBackgroundColor(comination[j]);
            t5.setBackgroundColor(comination[k]);
            t6.setBackgroundColor(comination[k]);
            t7.setBackgroundColor(comination[j]);
            t8.setBackgroundColor(comination[j]);
            t9.setBackgroundColor(comination[k]);
                break;

            //top-3
            case 4:
                t1.setBackgroundColor(comination[j]);
                t2.setBackgroundColor(comination[j]);
                t3.setBackgroundColor(comination[j]);
                t4.setBackgroundColor(comination[k]);
                t5.setBackgroundColor(comination[k]);
                t6.setBackgroundColor(comination[k]);
                t7.setBackgroundColor(comination[k]);
                t8.setBackgroundColor(comination[j]);
                t9.setBackgroundColor(comination[k]);
                break;

            //bottom-3
            case 5:
                t1.setBackgroundColor(comination[k]);
                t2.setBackgroundColor(comination[j]);
                t3.setBackgroundColor(comination[k]);
                t4.setBackgroundColor(comination[k]);
                t5.setBackgroundColor(comination[k]);
                t6.setBackgroundColor(comination[k]);
                t7.setBackgroundColor(comination[j]);
                t8.setBackgroundColor(comination[j]);
                t9.setBackgroundColor(comination[j]);
                break;

            //left-3
            case 6:
                t1.setBackgroundColor(comination[j]);
                t2.setBackgroundColor(comination[k]);
                t3.setBackgroundColor(comination[k]);
                t4.setBackgroundColor(comination[j]);
                t5.setBackgroundColor(comination[k]);
                t6.setBackgroundColor(comination[j]);
                t7.setBackgroundColor(comination[j]);
                t8.setBackgroundColor(comination[k]);
                t9.setBackgroundColor(comination[k]);
                break;

            //right-3
            case 7:
                t1.setBackgroundColor(comination[k]);
                t2.setBackgroundColor(comination[k]);
                t3.setBackgroundColor(comination[j]);
                t4.setBackgroundColor(comination[j]);
                t5.setBackgroundColor(comination[k]);
                t6.setBackgroundColor(comination[j]);
                t7.setBackgroundColor(comination[k]);
                t8.setBackgroundColor(comination[k]);
                t9.setBackgroundColor(comination[j]);
                break;

            //right-top-square
            case 8:
                t1.setBackgroundColor(comination[k]);
                t2.setBackgroundColor(comination[j]);
                t3.setBackgroundColor(comination[j]);
                t4.setBackgroundColor(comination[k]);
                t5.setBackgroundColor(comination[j]);
                t6.setBackgroundColor(comination[j]);
                t7.setBackgroundColor(comination[k]);
                t8.setBackgroundColor(comination[k]);
                t9.setBackgroundColor(comination[k]);
                break;

            //left-top-square
            case 9:
                t1.setBackgroundColor(comination[j]);
                t2.setBackgroundColor(comination[j]);
                t3.setBackgroundColor(comination[k]);
                t4.setBackgroundColor(comination[j]);
                t5.setBackgroundColor(comination[j]);
                t6.setBackgroundColor(comination[k]);
                t7.setBackgroundColor(comination[k]);
                t8.setBackgroundColor(comination[k]);
                t9.setBackgroundColor(comination[k]);
                break;

            //right-bottom-square
            case 10:
                t1.setBackgroundColor(comination[k]);
                t2.setBackgroundColor(comination[k]);
                t3.setBackgroundColor(comination[k]);
                t4.setBackgroundColor(comination[k]);
                t5.setBackgroundColor(comination[j]);
                t6.setBackgroundColor(comination[j]);
                t7.setBackgroundColor(comination[k]);
                t8.setBackgroundColor(comination[j]);
                t9.setBackgroundColor(comination[j]);
                break;

            //left-bottom-square
            case 11:
                t1.setBackgroundColor(comination[k]);
                t2.setBackgroundColor(comination[k]);
                t3.setBackgroundColor(comination[k]);
                t4.setBackgroundColor(comination[j]);
                t5.setBackgroundColor(comination[j]);
                t6.setBackgroundColor(comination[k]);
                t7.setBackgroundColor(comination[j]);
                t8.setBackgroundColor(comination[j]);
                t9.setBackgroundColor(comination[k]);
                break;

        }
    }

    private void stopGame(String declare){
        t1.setEnabled(false);
        t2.setEnabled(false);
        t3.setEnabled(false);
        t4.setEnabled(false);
        t5.setEnabled(false);
        t6.setEnabled(false);
        t7.setEnabled(false);
        t8.setEnabled(false);
        t9.setEnabled(false);
        declareWinScore(declare);
    }

    private void declareWinScore(final String declare){
        handler = new Handler();
        if(declare.equalsIgnoreCase("PLAYER 1 WINS")){
            scorePlayer1 = scorePlayer1 + 1;
            tvScore1.setText(String.valueOf("Player 1\n" +scorePlayer1));
            SharedPreferenceManager.setScoreforPlayer1(this, scorePlayer1);
        }
        if (declare.equalsIgnoreCase("PLAYER 2 WINS")) {
            scorePlayer2 = scorePlayer2 + 1;
            tvScore2.setText(String.valueOf("Player 2\n" + scorePlayer2));
            SharedPreferenceManager.setScoreforPlayer2(this, scorePlayer2);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clearBoard();
                if(declare.equalsIgnoreCase("PLAYER 1 WINS")){
                    tvTurns.setText("Player 2 Turn");
                    twist();
                }
                else if(declare.equalsIgnoreCase("PLAYER 2 WINS")){
                    tvTurns.setText("Player 1 Turn");
                    twist();
                }else{
                    twist();
                    if (player_1_turn) {
                        tvTurns.setText("Player 1 Turn");
                    } else {
                        tvTurns.setText("Player 2 Turn");
                    }
                }
            }
        },2000);
    }

    private void twist(){
        if(twistColor < 11) {
            twistColor = twistColor + 1;
        }else{
            twistColor = 0;
        }
    }
    private void clearBoard(){
        isAlreadyWon = false;
        t1.setEnabled(true);
        t2.setEnabled(true);
        t3.setEnabled(true);
        t4.setEnabled(true);
        t5.setEnabled(true);
        t6.setEnabled(true);
        t7.setEnabled(true);
        t8.setEnabled(true);
        t9.setEnabled(true);

        t1.setText("");
        t2.setText("");
        t3.setText("");
        t4.setText("");
        t5.setText("");
        t6.setText("");
        t7.setText("");
        t8.setText("");
        t9.setText("");
    }

    private void refresh(){
        if (player_1_turn) {
            tvTurns.setText("Player 1 Turn");
        } else {
            tvTurns.setText("Player 2 Turn");
        }
        scorePlayer1 = 0;
        scorePlayer2 = 0;
        tvScore1.setText("Player 1\n"+String.valueOf(scorePlayer1));
        tvScore2.setText("Player 2\n"+String.valueOf(scorePlayer2));
        t1.setText("");
        t2.setText("");
        t3.setText("");
        t4.setText("");
        t5.setText("");
        t6.setText("");
        t7.setText("");
        t8.setText("");
        t9.setText("");
        t1.setEnabled(true);
        t2.setEnabled(true);
        t3.setEnabled(true);
        t4.setEnabled(true);
        t5.setEnabled(true);
        t6.setEnabled(true);
        t7.setEnabled(true);
        t8.setEnabled(true);
        t9.setEnabled(true);
    }

    private void backgroundColorChangeLayoutWithAnimation(Context context, int[] comination, int color_a, int color_b){
        int Low = color_a;
        int High = color_b;
        Random r = new Random();
        int result = r.nextInt(High-Low + 1) + Low;
        layout.setBackgroundColor(comination[result]);
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(100);
        anim.setRepeatCount(1);
        anim.setRepeatMode(Animation.REVERSE);
        layout.startAnimation(anim);
    }
}

