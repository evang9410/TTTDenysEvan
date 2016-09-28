package com.ttt.de.tttevandenys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // Class variables for the View objects
    private ImageButton[] board = new ImageButton[9];

    // Class variables for the action buttons at the bottom of the screen.
    private Button btn_reset, btn_about, btn_zero, btn_scores, btn_play;

    // Text view to show who's turn it is.
    private TextView tv_playing;
    // Turn count, used to determine whose turn it is.
    private int turn = 0;
    // Check if you are playing vs the droid or not.
    private boolean isDroid = true;
    private boolean droidPlayed = false;
    private int player1_wins = 0;
    private int player2_wins = 0;
    private int droid_wins = 0;
    private int draw_count = 0;

    // SharedPreferences for saving to disk
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("scores",MODE_PRIVATE);
        //private method to instantiate the view objects in this activity.
        load_gui();
        // load board from SharedPreferences
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey("board_state"))
                load_board_state(savedInstanceState);
        }

        /**
         * Resets the Game Board to blank tiles.
         */
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_board();
            }
        });

        /**
         * Play Button, resets the board and changes who you are playing against.
         * isDroid == true -> Playing the droid
         * isDroid == false -> Playing player2
         */
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_board();

                if(isDroid) {
                    isDroid = false;
                    tv_playing.setText(R.string.playing_player1);
                }
                else {
                    isDroid = true;
                    tv_playing.setText(R.string.playing_droid);
                }
            }
        });

        btn_zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zero_scores();
            }
        });
        /**
         * OnClickListener for the "Scores" button
         * Fires new Intent displaying the scores.
         */
        btn_scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_scores();
            }
        });

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_about();
            }
        });


    }

    

    private void start_scores(){
        Intent scores_intent = new Intent(MainActivity.this,ScoresActivity.class);
        startActivity(scores_intent);
    }

    private void start_about(){
        Intent about_intent = new Intent(MainActivity.this,AboutActivity.class);
        startActivity(about_intent);
    }

    private void reset_board() {
        for(ImageButton ib : board){
            ib.setImageResource(R.drawable.tile);
            ib.setClickable(true);
            ib.setTag("");
        }
        turn = 0;
    }

    private void zero_scores(){
        player1_wins = 0;
        player2_wins = 0;
        droid_wins = 0;
        draw_count = 0;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("player1_wins", player1_wins);
        editor.putInt("player2_wins",player2_wins);
        editor.putInt("droid_wins",droid_wins);
        editor.putInt("draw_count",draw_count);
        editor.commit();

        Toast toast = Toast.makeText(MainActivity.this,getText(R.string.zero_toast),Toast.LENGTH_SHORT);
        toast.show();
    }


    public void tile_click(View v){

        SharedPreferences.Editor editor = prefs.edit();

        ImageButton ib = (ImageButton)v;
        ib.setClickable(false);
        if(check_win().equals("none")){
            if(turn % 2 == 0){
                // Player1's turn
                Log.d("Turn","player1 turn");
                if(isDroid)
                    tv_playing.setText(R.string.playing_droid);
                else
                    tv_playing.setText(R.string.playing_player1);
                ib.setTag("x");
                ib.setImageResource(R.drawable.tile_x);
                turn++;
                check_win();
                if(check_win().equals("none") & turn < 9) {
                    if (isDroid) {
                        droid_turn();
                    }
                }
            }
            else{
                Log.d("Turn", "player2 turn");
                if(!isDroid)
                    tv_playing.setText(R.string.playing_player1);

                ib.setTag("o");
                ib.setImageResource(R.drawable.tile_o);
                turn++;
                check_win();
            }

            if(check_win().equals("x")){
                Toast toast = Toast.makeText(MainActivity.this,getText(R.string.p1_win_toast),Toast.LENGTH_SHORT);
                toast.show();
                player1_wins++;
                editor.putInt("player1_wins", player1_wins);
                editor.commit();
            }
            else if(check_win().equals("o") & isDroid){
                Toast toast = Toast.makeText(MainActivity.this,getText(R.string.droid_wins_toast),Toast.LENGTH_SHORT);
                toast.show();
                droid_wins++;
                editor.putInt("droid_wins", droid_wins);
                editor.commit();
            }
            else if(check_win().equals("o") & isDroid == false){
                Toast toast = Toast.makeText(MainActivity.this,getText(R.string.p2_win_toast),Toast.LENGTH_SHORT);
                toast.show();
                player2_wins++;
                editor.putInt("player2_wins", player2_wins);
                editor.commit();
            }
            if(turn == 9 && check_win().equals("none")){
                Toast toast = Toast.makeText(MainActivity.this,getText(R.string.draw_toast),Toast.LENGTH_LONG);
                toast.show();
                draw_count++;
                editor.putInt("draw_count", draw_count);
                editor.commit();
            }
        }

        save_board_state();
    }

    /**
     * Loops through the board array (Array of tiles of the tic tac toe game)
     * and turns them into an array of strings, following the order of the tiles from 0-9
     * saves the string array to be loaded upon state change.
     * @return
     */
    private String[] save_board_state(){
        String[] board_tags = new String[board.length];
        for(int i = 0; i < board_tags.length; i++){
            if(board[i].getTag().equals("x")){
                board_tags[i] = "x";
            }
            else if(board[i].getTag().equals("o")){
                board_tags[i] = "o";
            }
            else
                board_tags[i]="";
        }
        return board_tags;
    }

    /**
     * Loads the string array that was saved when the state changed and applies the proper properties
     * to the image buttons.
     * @param b
     */
    private void load_board_state(Bundle b){
        String[] arr = b.getStringArray("board_state");

        for(int i = 0; i < board.length; i++){
            if(arr[i].equals("x")){
                board[i].setImageResource(R.drawable.tile_x);
                board[i].setTag("x");
                board[i].setClickable(false);
            }
            else if(arr[i].equals("o")){
                board[i].setImageResource(R.drawable.tile_o);
                board[i].setTag("o");
                board[i].setClickable(false);
            }
            else{
                board[i].setImageResource(R.drawable.tile);
                board[i].setClickable(true);
                board[i].setTag("");
            }
        }
    }

    private void droid_turn(){
        Log.d("Turn", "droid turn");
        boolean play = true;
        Random rnd = new Random();
        int rng = rnd.nextInt(9);
        /*if(check_win().equals("none")) {
            while (play) {
                if (!board[rng].getTag().equals("x") && !board[rng].getTag().equals("o")) {
                    Log.d("droid", "And he tries playing at" + board[rng].getTag().toString());
                    play=false;
                    board[rng].setTag("o");
                    board[rng].setImageResource(R.drawable.tile_o);
                    tile_click(board[rng]);
                    turn++;
                } else
                    rng = rnd.nextInt(9);
            }
        }*/
        if(check_win().equals("none")) {
            while (true) {
                if (!board[rng].getTag().equals("x") & !board[rng].getTag().equals("o")) {
                    tile_click(board[rng]);
                    break;
                } else {
                    rng = rnd.nextInt(9);
                }
            }
        }

    }

    private String check_win(){

        // Check across

        // For X
        if(board[0].getTag().equals("x") & board[1].getTag().equals("x") & board[2].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
            return "x";
        }
        if(board[3].getTag().equals("x") & board[4].getTag().equals("x") & board[5].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
            return "x";
        }
        if(board[6].getTag().equals("x") & board[7].getTag().equals("x") & board[8].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
            return "x";
        }

        //For O
        if(board[0].getTag().equals("o") & board[1].getTag().equals("o") & board[2].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
            return "o";
        }
        if(board[3].getTag().equals("o") & board[4].getTag().equals("o") & board[5].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
            return "o";
        }
        if(board[6].getTag().equals("o") & board[7].getTag().equals("o") & board[8].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
            return "o";
        }

        // Check Vertical
        // For X
        if(board[0].getTag().equals("x") & board[3].getTag().equals("x") & board[6].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
            return "x";
        }
        if(board[1].getTag().equals("x") & board[4].getTag().equals("x") & board[7].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
            return "x";
        }
        if(board[2].getTag().equals("x") & board[5].getTag().equals("x") & board[8].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
            return "x";
        }

        // For O
        if(board[0].getTag().equals("o") & board[3].getTag().equals("o") & board[6].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
            return "o";
        }
        if(board[1].getTag().equals("o") & board[4].getTag().equals("o") & board[7].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
            return "o";
        }
        if(board[2].getTag().equals("o") & board[5].getTag().equals("o") & board[8].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
            return "o";
        }

        // Check Diagonal
        // For X
        if(board[6].getTag().equals("x") & board[4].getTag().equals("x") & board[2].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
            return "x";
        }
        if(board[0].getTag().equals("x") & board[4].getTag().equals("x") & board[8].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
            return "x";
        }

        // For O
        if(board[6].getTag().equals("o") & board[4].getTag().equals("o") & board[2].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
            return "o";
        }
        if(board[0].getTag().equals("o") & board[4].getTag().equals("o") & board[8].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
            return "o";
        }

        return "none";


    }

    /*private String check_win(){

        for(int i =0; i<3; i++)
        {
            //Check horizontal lines
            if(board[i*3].getTag().equals(board[1+i*3]) && board[i*3].getTag().equals(board[2+i*3]))
            {
                return board[i*3].getTag().toString();
            }
            //Check vertical lines
            else if(board[i].getTag().equals(board[3+i].getTag()) && board[i].getTag().equals(board[6+i].getTag()))
            {
                return board[i].getTag().toString();
            }
            //Check diagonals
            else if(i < 2)
            {
                if(board[4].getTag().equals(board[i*2]) && board[4].getTag().equals(board[i*-2 + 8]))
                {
                    return board[4].getTag().toString();
                }
            }
        }
        return "none";
    }*/

    /**
     * Private method to load the view objects into memory to be manipulated.
     * Loads the ImageButtons into an array.
     * Loads the Game play buttons.
     */
    private void load_gui(){
        // Load the ImageButtons
        board[0] = (ImageButton)findViewById(R.id.tile_1);
        board[1] = (ImageButton)findViewById(R.id.tile_2);
        board[2] = (ImageButton)findViewById(R.id.tile_3);
        board[3] = (ImageButton)findViewById(R.id.tile_4);
        board[4] = (ImageButton)findViewById(R.id.tile_5);
        board[5] = (ImageButton)findViewById(R.id.tile_6);
        board[6] = (ImageButton)findViewById(R.id.tile_7);
        board[7] = (ImageButton)findViewById(R.id.tile_8);
        board[8] = (ImageButton)findViewById(R.id.tile_9);

        // Load the Buttons
        btn_reset = (Button)findViewById(R.id.btn_reset);
        btn_about = (Button)findViewById(R.id.btn_about);
        btn_play = (Button)findViewById(R.id.btn_play);
        btn_zero = (Button)findViewById(R.id.btn_zero);
        btn_scores = (Button)findViewById(R.id.btn_scores);

        // Load the textview
        tv_playing = (TextView)findViewById(R.id.tv_playing);
        if(isDroid){
            tv_playing.setText(R.string.playing_droid);
        }else{
            tv_playing.setText(R.string.playing_droid);
        }
    }
    // INSTANCE STATE STUFF
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putStringArray("board_state", save_board_state());

    }
}
