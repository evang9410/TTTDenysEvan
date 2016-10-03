package com.ttt.de.tttevandenys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("scores", MODE_PRIVATE);
        //private method to instantiate the view objects in this activity.
        load_gui();
        // load board from SharedPreferences
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("board_state"))
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

                if (isDroid) {
                    isDroid = false;
                    tv_playing.setText(R.string.playing_player1);
                } else {
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

        /**
         * onClickListener for the "About" button.
         * Fires the AboutActivity Intent which displays the about game and credits.
         */

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_about();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Private method used to fire the score activity
     */
    private void start_scores() {
        Intent scores_intent = new Intent(MainActivity.this, ScoresActivity.class);
        startActivity(scores_intent);
    }

    /**
     * Private method used to fire the about activity.
     */
    private void start_about() {
        Intent about_intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(about_intent);
    }

    /**
     * Private method used to reset the board
     * Sets the tiles back to their original image
     * Makes the tiles clickable again
     * Sets the tags back to their numbered order [1-9]
     */
    private void reset_board() {
        for (int i = 0; i < 9; i++) {
            board[i].setImageResource(R.drawable.tile);
            board[i].setClickable(true);
            board[i].setTag(i + 1 + "");
        }
        turn = 0;
    }

    /**
     * Private method to set the scores saved to disk to zero.
     */
    private void zero_scores() {
        player1_wins = 0;
        player2_wins = 0;
        droid_wins = 0;
        draw_count = 0;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("player1_wins", player1_wins);
        editor.putInt("player2_wins", player2_wins);
        editor.putInt("droid_wins", droid_wins);
        editor.putInt("draw_count", draw_count);
        editor.commit();

        Toast toast = Toast.makeText(MainActivity.this, getText(R.string.zero_toast), Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Main bread and butter of the app.
     * onClickListener for every ImageButton in the MainActivity.
     * Handles the changes of ImageButtons depending on who's pressing them
     * Displays the winning messages and draw message incrementing the appropriate counters.
     *
     * @param v
     */
    public void tile_click(View v) {

        SharedPreferences.Editor editor = prefs.edit();

        ImageButton ib = (ImageButton) v;
        ib.setClickable(false);
        if (check_win().equals("none")) {
            if (turn % 2 == 0) {
                // Player1's turn
                Log.d("Turn", "player1 turn");
                if (isDroid)
                    tv_playing.setText(R.string.playing_droid);
                else
                    tv_playing.setText(R.string.playing_player1);

                ib.setTag("x");
                ib.setImageResource(R.drawable.tile_x);
                turn++;
                //check_win();
                if (check_win().equals("none") & turn < 9) {
                    if (isDroid) {
                        droid_turn();
                        return;
                    }
                }
            } else {
                Log.d("Turn", "player2 turn");
                if (!isDroid)
                    tv_playing.setText(R.string.playing_player1);
                if (check_win().equals("none")) {
                    ib.setTag("o");
                    ib.setImageResource(R.drawable.tile_o);
                    turn++;
                    //check_win();
                }
            }
        }

        if (check_win().equals("x")) {
            alert_user(R.string.alert_message_p1);

            player1_wins++;
            editor.putInt("player1_wins", player1_wins);
            editor.commit();
            disable_tiles();
        } else if (check_win().equals("o") & isDroid) {
            alert_user(R.string.alert_message_droid);
            droid_wins++;
            editor.putInt("droid_wins", droid_wins);
            editor.commit();
            disable_tiles();
        } else if (check_win().equals("o") & isDroid == false) {
            alert_user(R.string.alert_message_p2);
            player2_wins++;
            editor.putInt("player2_wins", player2_wins);
            editor.commit();
            disable_tiles();
        }
        if (turn == 9 && check_win().equals("none")) {
            Toast toast = Toast.makeText(MainActivity.this, getText(R.string.draw_toast), Toast.LENGTH_LONG);
            toast.show();
            draw_count++;
            editor.putInt("draw_count", draw_count);
            editor.commit();
            disable_tiles();
        }

        //save_board_state();
    }

    private void disable_tiles(){
        for(ImageButton i : board)
            i.setClickable(false);
    }

    private void alert_user(int winner) {

        // Build the alert dialog box to be showed when a player wins.
        AlertDialog.Builder alert_dialog = new AlertDialog.Builder(MainActivity.this);
        // set title
        alert_dialog.setTitle(getString(R.string.alert_title));
        // set message
        switch (winner){
            case R.string.alert_message_p1:
                alert_dialog.setMessage(getString(R.string.alert_message_p1));
                break;
            case R.string.alert_message_p2:
                alert_dialog.setMessage(getString(R.string.alert_message_p2));
                break;
            default:
                alert_dialog.setMessage(getString(R.string.alert_message_droid));
        }
        alert_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        // create alert dialog box with the builder.
        AlertDialog alert = alert_dialog.create();
        // show the message
        alert.show();

    }

    /**
     * Loops through the board array (Array of tiles of the tic tac toe game)
     * and turns them into an array of strings, following the order of the tiles from 0-9
     * saves the string array to be loaded upon state change.
     *
     * @return
     */
    private String[] save_board_state() {
        String[] board_tags = new String[board.length];
        for (int i = 0; i < board_tags.length; i++) {
            if (board[i].getTag().equals("x")) {
                board_tags[i] = "x";
            } else if (board[i].getTag().equals("o")) {
                board_tags[i] = "o";
            } else
                board_tags[i] = i + 1 + "";
        }
        return board_tags;
    }

    /**
     * Loads the string array that was saved when the state changed and applies the proper properties
     * to the image buttons.
     *
     * @param b
     */
    private void load_board_state(Bundle b) {
        String[] arr = b.getStringArray("board_state");

        for (int i = 0; i < board.length; i++) {
            if (arr[i].equals("x")) {
                board[i].setImageResource(R.drawable.tile_x);
                board[i].setTag("x");
                board[i].setClickable(false);
            } else if (arr[i].equals("o")) {
                board[i].setImageResource(R.drawable.tile_o);
                board[i].setTag("o");
                board[i].setClickable(false);
            } else {
                board[i].setImageResource(R.drawable.tile);
                board[i].setClickable(true);
                board[i].setTag(i + 1 + "");
            }
        }
    }

    /**
     * Randoms a number from 1-9 and checks if the tile coresponding to that number is a valid
     * ImageButton that can be played. If not - it re rolls.
     */
    private void droid_turn() {
        Log.d("Turn", "droid turn");
        boolean play = true;
        Random rnd = new Random();
        int rng = rnd.nextInt(9);
        if (check_win().equals("none")) {
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

    /**
     * Checks if the game is over and returns a string of the tile that won.
     * "none" means no one has won yet.
     *
     * @return String "x" or "o" or "none"
     */
    private String check_win() {

        for (int i = 0; i < 3; i++) {
            //Check horizontal lines
            if (board[i * 3].getTag().equals(board[1 + i * 3].getTag()) && board[i * 3].getTag().equals(board[2 + i * 3].getTag())) {
                return board[i * 3].getTag().toString();
            }


            //Check vertical lines
            else if (board[i].getTag().equals(board[3 + i].getTag()) && board[i].getTag().equals(board[6 + i].getTag())) {
                return board[i].getTag().toString();
            }
            //Check diagonals
            else if (i < 2) {
                if (board[4].getTag().equals(board[i * 2].getTag()) && board[4].getTag().equals(board[i * -2 + 8].getTag())) {
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
    private void load_gui() {
        // Load the ImageButtons
        board[0] = (ImageButton) findViewById(R.id.tile_1);
        board[1] = (ImageButton) findViewById(R.id.tile_2);
        board[2] = (ImageButton) findViewById(R.id.tile_3);
        board[3] = (ImageButton) findViewById(R.id.tile_4);
        board[4] = (ImageButton) findViewById(R.id.tile_5);
        board[5] = (ImageButton) findViewById(R.id.tile_6);
        board[6] = (ImageButton) findViewById(R.id.tile_7);
        board[7] = (ImageButton) findViewById(R.id.tile_8);
        board[8] = (ImageButton) findViewById(R.id.tile_9);

        // Load the Buttons
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_about = (Button) findViewById(R.id.btn_about);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_zero = (Button) findViewById(R.id.btn_zero);
        btn_scores = (Button) findViewById(R.id.btn_scores);

        // Load the textview
        tv_playing = (TextView) findViewById(R.id.tv_playing);
        if (isDroid) {
            tv_playing.setText(R.string.playing_droid);
        } else {
            tv_playing.setText(R.string.playing_droid);
        }
    }
    // INSTANCE STATE STUFF

    /**
     * Saves the state of the board (which tiles have been changed) to the bundle to be loaded if the
     * activity's state changes.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("board_state", save_board_state());

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.ttt.de.tttevandenys/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.ttt.de.tttevandenys/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

