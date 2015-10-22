package uk.ac.kcl.teamraccoon.sungka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {


    Board gameBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialises the game board with trays = 7, store = 0
        gameBoard = new Board();
        setupBoardLayout();

    }

    public void updateBoard(){


    }

    public void setupBoardLayout(){
        LinearLayout layout_p2_store = (LinearLayout) findViewById(R.id.layout_p2_store);
        LinearLayout layout_p1_store = (LinearLayout) findViewById(R.id.layout_p1_store);
        LinearLayout layout_p2_trays = (LinearLayout) findViewById(R.id.layout_p2_trays);
        LinearLayout layout_p1_trays = (LinearLayout) findViewById(R.id.layout_p1_trays);


        Button storeButtonp1 = (Button) getLayoutInflater().inflate(R.layout.stores, layout_p1_store, true);
        Button storeButtonp2 = (Button) getLayoutInflater().inflate(R.layout.stores, layout_p2_store, true);
    }
}
