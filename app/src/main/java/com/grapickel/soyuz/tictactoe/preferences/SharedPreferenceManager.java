package com.grapickel.soyuz.tictactoe.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Soyuz on 26-09-2017.
 */

public class SharedPreferenceManager {

    private static String APP_SETTINGS = "ScoreSharedPref";
    private static final String SCORE_PLAYER_1 = "scoreCardPlayer_1";
    private static final String SCORE_PLAYER_2 = "scoreCardPlayer_2";

    private SharedPreferenceManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static int getScoreforPlayer1(Context context) {
        return getSharedPreferences(context).getInt(SCORE_PLAYER_1 , 0);
    }

    public static void setScoreforPlayer1(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(SCORE_PLAYER_1 , newValue);
        editor.commit();
    }

    public static int getScoreforPlayer2(Context context) {
        return getSharedPreferences(context).getInt(SCORE_PLAYER_2 , 0);
    }

    public static void setScoreforPlayer2(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(SCORE_PLAYER_2 , newValue);
        editor.commit();
    }

}
