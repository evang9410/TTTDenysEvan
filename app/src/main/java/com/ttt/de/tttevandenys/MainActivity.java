package com.ttt.de.tttevandenys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // Class variables for the View objects
    private ImageButton[] board = new ImageButton[9];
    private Button btn_reset, btn_about, btn_zero, btn_scores, btn_play;
    private int turn = 0;
    private boolean isDroid = true;
    private boolean droidPlayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //private method to instantiate the view objects in this activity.
        load_gui();

        /**
         * Resets the Game Board to blank tiles.
         */
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_board();
            }
        });
    }

    private void reset_board() {
        for(ImageButton ib : board){
            ib.setImageResource(R.drawable.tile);
            ib.setClickable(true);
        }
    }

    /**
     * onClick Listener for the tiles in the board.
     * Changes them to either X's or O's depending on the turn.
     * @param v
     */
    public void tile_click(View v){

        ImageButton ib = (ImageButton)v;
        ib.setClickable(false);
        Log.d("chk", check_win());
        if(check_win().equals("none")) {
            if (turn % 2 == 0 && droidPlayed == false) {
                Log.d("playerInfo", "Player turn: " + turn);
                ib.setTag("x");
                ib.setImageResource(R.drawable.tile_x);
                turn++;

            }
            if (isDroid && turn%2 != 0){
            Log.d("", "Droid: " + turn);
            turn++;
            droidPlayed=true;
            droid_turn();
            }
            else if(droidPlayed==false) {
                Log.d("", "Why the fuck am i even here?");
                ib.setTag("o");
                ib.setImageResource(R.drawable.tile_o);
                turn++;
            }
        }
            Log.d("", check_win());
    }

    private void droid_turn(){
        Log.d("droid", "droid turn");
        boolean play = true;
        Random rnd = new Random();
        int rng = rnd.nextInt(9);
        if(check_win().equals("none")) {
            while (play) {
                if (!board[rng].getTag().equals("x") && !board[rng].getTag().equals("o")) {
                    Log.d("droid", "And he tries playing at" + board[rng].getTag().toString());
                    play=false;
                    board[rng].setTag("o");
                    board[rng].setImageResource(R.drawable.tile_o);
                    tile_click(board[rng]);
                } else
                    rng = rnd.nextInt(9);
            }
        }

    }

   private String check_win(){

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
    }

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
    }
}
