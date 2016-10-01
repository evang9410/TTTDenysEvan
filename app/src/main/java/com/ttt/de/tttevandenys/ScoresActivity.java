package com.ttt.de.tttevandenys;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ScoresActivity extends AppCompatActivity {
    private TextView tv_p1_wins, tv_p2_wins, tv_droid_wins, tv_draws;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        prefs = getSharedPreferences("scores",MODE_PRIVATE); // Get the SharedPreferences named "scores"

        load_textviews();
        load_scores();
    }

    /**
     * Loads the TextViews from the activity_scores.xml
     * to be manipulated through java.
     */
    private void load_textviews(){
        tv_p1_wins = (TextView)findViewById(R.id.scores_tv_player1_wins);
        tv_p2_wins = (TextView)findViewById(R.id.scores_tv_player2_wins);
        tv_droid_wins = (TextView)findViewById(R.id.scores_tv_droid_wins);
        tv_draws = (TextView)findViewById(R.id.scores_tv_draws);
    }

    /**
     * sets the TextViews to their scores that are saved on disk.
     */
    private void load_scores(){
        tv_p1_wins.setText(String.valueOf(prefs.getInt("player1_wins",0)));
        tv_p2_wins.setText(String.valueOf(prefs.getInt("player2_wins",0)));
        tv_droid_wins.setText(String.valueOf(prefs.getInt("droid_wins",0)));
        tv_draws.setText(String.valueOf(prefs.getInt("draw_count",0)));
    }


    /*
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("p1_score",prefs.getInt("player1_wins",0));
        outState.putInt("p2_score",prefs.getInt("player2_wins",0));
        outState.putInt("droid_score",prefs.getInt("droid_wins",0));
        outState.putInt("draws",prefs.getInt("draw_count",0));
    }*/
}
