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
    private ImageButton tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8, tile_9;
    private ImageButton[] board = new ImageButton[9];
    private Button btn_reset, btn_about, btn_zero, btn_scores, btn_play;
    private int turn = 0;
    private boolean isDroid = true;

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
        if(turn % 2 == 0){
            ib.setTag("x");
            ib.setImageResource(R.drawable.tile_x);
            turn++;
            if(isDroid)
                droid_turn();
        }
        else{
            ib.setTag("o");
            ib.setImageResource(R.drawable.tile_o);
            turn++;
        }
        check_win();
    }

    private void droid_turn(){
        Log.d("droid", "droid turn");
        Random rnd = new Random();
        int rng = rnd.nextInt(9);

        while(true){
            if(board[rng].getTag().equals("")) {
                tile_click(board[rng]);
                break;
            }
            else
                rng = rnd.nextInt(9);
        }

    }

    private void check_win(){

        // Check across

        // For X
        if(board[0].getTag().equals("x") & board[1].getTag().equals("x") & board[2].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
        }
        if(board[3].getTag().equals("x") & board[4].getTag().equals("x") & board[5].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
        }
        if(board[6].getTag().equals("x") & board[7].getTag().equals("x") & board[8].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
        }

        //For O
        if(board[0].getTag().equals("o") & board[1].getTag().equals("o") & board[2].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
        }
        if(board[3].getTag().equals("o") & board[4].getTag().equals("o") & board[5].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
        }
        if(board[6].getTag().equals("o") & board[7].getTag().equals("o") & board[8].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
        }

        // Check Vertical
        // For X
        if(board[0].getTag().equals("x") & board[3].getTag().equals("x") & board[6].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
        }
        if(board[1].getTag().equals("x") & board[4].getTag().equals("x") & board[7].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
        }
        if(board[2].getTag().equals("x") & board[5].getTag().equals("x") & board[8].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
        }

        // For O
        if(board[0].getTag().equals("o") & board[3].getTag().equals("o") & board[6].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
        }
        if(board[1].getTag().equals("o") & board[4].getTag().equals("o") & board[7].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
        }
        if(board[2].getTag().equals("o") & board[5].getTag().equals("o") & board[8].getTag().equals("o")){
            // player 1 wins
            Log.d("winner", "o wins");
        }

        // Check Diagonal
        // For X
        if(board[6].getTag().equals("x") & board[4].getTag().equals("x") & board[2].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
        }
        if(board[0].getTag().equals("x") & board[4].getTag().equals("x") & board[8].getTag().equals("x")){
            // player 1 wins
            Log.d("winner", "x wins");
        }

        // For O
        if(board[6].getTag().equals("o") & board[4].getTag().equals("o") & board[2].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
        }
        if(board[0].getTag().equals("o") & board[4].getTag().equals("o") & board[8].getTag().equals("o")){
            // player 2 wins
            Log.d("winner", "o wins");
        }



    }

    /**
     * Private method to load the view objects into memory to be manipulated.
     * Loads the ImageButtons into an array.
     * Loads the Game play buttons.
     */
    private void load_gui(){
        // Load the ImageButtons
        tile_1 = (ImageButton)findViewById(R.id.tile_1);
        tile_2 = (ImageButton)findViewById(R.id.tile_2);
        tile_3 = (ImageButton)findViewById(R.id.tile_3);
        tile_4 = (ImageButton)findViewById(R.id.tile_4);
        tile_5 = (ImageButton)findViewById(R.id.tile_5);
        tile_6 = (ImageButton)findViewById(R.id.tile_6);
        tile_7 = (ImageButton)findViewById(R.id.tile_7);
        tile_8 = (ImageButton)findViewById(R.id.tile_8);
        tile_9 = (ImageButton)findViewById(R.id.tile_9);

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
