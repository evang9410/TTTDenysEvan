package com.ttt.de.tttevandenys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    // Class variables for the View objects
    private ImageButton tile_1, tile_2, tile_3, tile_4, tile_5, tile_6, tile_7, tile_8, tile_9;
    private Button btn_reset, btn_about, btn_zero, btn_scores, btn_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //private method to instantiate the view objects in this activity.
        load_gui();
    }
    // onClick "listener" for tiles
    public void tile_click(View v){
        String tile_tag = v.getTag().toString();
        Log.d("Tile tag", tile_tag);


    }


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

        // Load the Buttons
        btn_reset = (Button)findViewById(R.id.btn_reset);
        btn_about = (Button)findViewById(R.id.btn_about);
        btn_play = (Button)findViewById(R.id.btn_play);
        btn_zero = (Button)findViewById(R.id.btn_zero);
        btn_scores = (Button)findViewById(R.id.btn_scores);
    }
}
