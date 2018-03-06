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

public class SinglePlayerActivity extends AppCompatActivity implements View.OnClickListener {

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
    private ImageView tvrefresh;
    private boolean isAlreadyWon;
    private int twistColor = 0;
    private boolean isPlayerPlayed = false;


    private boolean isPlayerTurn;
    private boolean isComputerTurn;
    private String player = "X", computer = "O";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ACTIVITY", ""+"SinglePlayer");
        setContentView(R.layout.activity_main);
        iniview();
        iniDataSet(positionArr);


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
        switch (view.getId()){
            case R.id.tile1:
                Move bestMove = findBestMove(positionArr);
                System.out.println("The Optimal Move is :");
                System.out.println("ROW:"+bestMove.row+"Col:"+bestMove.col);

            case R.id.tile2:
                Move bestMove1 = findBestMove(positionArr);
                System.out.println("The Optimal Move is :");
                System.out.println("ROW:"+bestMove1.row+"Col:"+bestMove1.col);
        }
    }

    boolean isMovesLeft(TextView arr[][])
    {
        for (int i = 0; i<3; i++)
            for (int j = 0; j<3; j++)
                if (arr[i][j].getText().toString() == "")
                    return true;
        return false;
    }

    int evaluate(TextView arr[][]){
            //check row
            for  (int row = 0; row < 3 ; row++){
                    if(arr[row][0] == arr[row][1] && arr[row][1] == arr[row][2]){
                        if(arr[row][0].getText().toString().equalsIgnoreCase(player) )
                        return +10;
                    else if(arr[row][0].getText().toString().equalsIgnoreCase(computer))
                        return -10;
                }
            }
            //check column
            for  (int col = 0; col < 3 ; col++){
                if(arr[0][col] == arr[1][col] && arr[1][col] == arr[2][col]){
                    if(arr[0][col].getText().toString().equalsIgnoreCase(player) )
                        return +10;
                    else if(arr[0][col].getText().toString().equalsIgnoreCase(computer))
                        return -10;
                }
            }


        // check diagonal
            if(arr[0][0] == arr[1][1] && arr[1][1] == arr[2][2]) {
                if (arr[0][0].getText().toString().equalsIgnoreCase(player))
                    return +10;
                else if (arr[row][0].getText().toString().equalsIgnoreCase(computer))
                    return -10;
            }


            if(arr[0][2] == arr[1][1] && arr[1][1] == arr[2][0]) {
                if (arr[0][2].getText().toString().equalsIgnoreCase(player))
                    return +10;
                else if (arr[row][0].getText().toString().equalsIgnoreCase(computer))
                    return -10;
            }
        return 0;
    }

    private int minimax(TextView arr[][], int depth, boolean isMax){
        int score = evaluate(arr);

        if(score == 10)
            return score;

        if(score == -10)
            return score;

        if(!isMovesLeft(arr))
            return 0;

        if(isMax){
            int best = -1000;
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(arr[i][j].getText().toString().equalsIgnoreCase("")){
                        arr[i][j].setText(player);
                        best = minimax(arr,depth+1,!isMax);
                        arr[i][j].setText("");
                    }
                }
            }
            return best;
        }else{
            int best = 1000;
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(arr[i][j].getText().toString().equalsIgnoreCase("")){
                        arr[i][j].setText(computer);
                        best = minimax(arr,depth+1,!isMax);
                        arr[i][j].setText("");
                    }
                }
            }
            return best;
        }
    }

    public class Move {
        public int row;
        public int col;

        public Move(int row, int col){
            this.row = row;
            this.col = col;
        }
        public int getRow() { return row; }
        public int getCol() { return col; }
        // setter

        public void setRow(int row) { this.row = row; }
        public void setCol(int col) { this.col = col; }
    }

    // This will return the best possible move for the player
    Move findBestMove(TextView array[][])
    {
        int bestVal = -1000;
        Move bestMove = new Move(-1,-1);

        // Traverse all cells, evalutae minimax function for
        // all empty cells. And return the cell with optimal
        // value.
        for (int i = 0; i<3; i++)
        {
            for (int j = 0; j<3; j++)
            {
                // Check if celll is empty
                if (array[i][j].getText().toString().equalsIgnoreCase(""))
                {
                    // Make the move
                    array[i][j].setText(player);

                    // compute evaluation function for this
                    // move.
                    int moveVal = minimax(array, 0, false);

                    // Undo the move
                    array[i][j].setText("");

                    // If the value of the current move is
                    // more than the best value, then update
                    // best/
                    if (moveVal > bestVal)
                    {
                        bestMove.setRow(i);
                        bestMove.setCol(j);
                        bestVal = moveVal;
                    }
                }
            }
        }

        System.out.println("The value of the best Move is "+"ROW:"+bestMove.getRow() + "COL:"+ bestMove.getCol());

        return bestMove;
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


