package com.missouristate.davis916.escapethecatcher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Laura Davis CIS 262-902
 * 19 April 2018
 * This app uses a logic board to create
 * a predator vs. prey single player game.
 */

public class MainActivity extends Activity implements
        GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener{

    private GestureDetector aGesture;

    //Board information
    final int SQUARE = 130;
    final int OFFSET = 70;
    final int COLUMNS = 8;
    final int ROWS = 8;
    final int gameBoard[][] = {
            {1,1,1,1,1,1,1,1},
            {1,2,2,1,2,1,2,1},
            {1,2,2,2,2,2,2,1},
            {1,2,1,2,2,2,2,1},
            {1,2,2,2,2,1,2,1},
            {1,2,2,2,2,2,2,3},
            {1,2,1,2,2,2,2,1},
            {1,1,1,1,1,1,1,1},
    };

    //Visual objects re organized in an ArrayList
    private ArrayList<ImageView> visualObjects;
    Player player;
    Enemy enemy;

    //Layout and interactive information
    private ConstraintLayout constraintLayout;
    private ImageView enemyIMG;
    private ImageView playerIMG;
    private ImageView obstacleIMG;
    private ImageView exitIMG;
    private int exitRow;
    private int exitCol;

    //Wins and losses
    private int wins;
    private int losses;
    private TextView winsTextView;
    private TextView lossesTextView;

    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the layout content for the activity
        setContentView(R.layout.activity_main);

        //Reference the activity layout and TextViews
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        winsTextView = (TextView) findViewById(R.id.winsTextView);
        lossesTextView = (TextView) findViewById(R.id.lossesTextView);

        //Instantiate the layout inflater
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Build the logic board and construct the game
        visualObjects = new ArrayList<ImageView>();
        buildLogicBoard();
        createEnemy();
        createPlayer();
        wins = 0;
        losses = 0;
        winsTextView.setText("Wins: " + wins);
        lossesTextView.setText("Losses: " + losses);

        //Instantiate a gesture detector
        aGesture = new GestureDetector(this,this);
        aGesture.setOnDoubleTapListener(this);
    }//end onCreate()

    private void buildLogicBoard(){
        for (int row = 0; row < ROWS; row++){
            for (int col = 0; col < COLUMNS; col++){
                if (gameBoard[row][col] == BoardCodes.isOBSTACLE){
                    obstacleIMG = (ImageView) layoutInflater.inflate(R.layout.obstacle_layout, null);
                    obstacleIMG.setX(col * SQUARE + OFFSET);
                    obstacleIMG.setY(row * SQUARE + OFFSET);
                    constraintLayout.addView(obstacleIMG, 0);
                    visualObjects.add(obstacleIMG);
                }
                else if (gameBoard[row][col] == BoardCodes.isHOME){
                    exitIMG = (ImageView) layoutInflater.inflate(R.layout.exit_layout, null);
                    exitIMG.setX(col * SQUARE + OFFSET);
                    exitIMG.setY(row * SQUARE + OFFSET);
                    constraintLayout.addView(exitIMG, 0);
                    visualObjects.add(exitIMG);
                    exitRow = 5;
                    exitCol = 7;
                }
            }
        }
    }//end buildLogicBoard()

    private void createEnemy(){
        int row = 2;
        int col = 4;

        enemyIMG = (ImageView) layoutInflater.inflate(R.layout.enemy_layout, null);
        enemyIMG.setX(col * SQUARE + OFFSET);
        enemyIMG.setY(row * SQUARE + OFFSET);
        constraintLayout.addView(enemyIMG, 0);
        enemy = new Enemy();
        enemy.setRow(row);
        enemy.setCol(col);
        visualObjects.add(enemyIMG);
    }//end createEnemy()

    private void createPlayer(){
        int row = 1;
        int col = 1;

        playerIMG = (ImageView) layoutInflater.inflate(R.layout.player_layout, null);
        playerIMG.setX(col * SQUARE + OFFSET);
        playerIMG.setY(row * SQUARE + OFFSET);
        constraintLayout.addView(playerIMG, 0);
        player = new Player();
        player.setRow(row);
        player.setCol(col);
        visualObjects.add(playerIMG);
    }//end createPlayer

    //Override methods for gestures
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return aGesture.onTouchEvent(event);
    }
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY){
        movePlayer(velocityX, velocityY);
        return true;
    }
    @Override
    public void onLongPress(MotionEvent event){}
    @Override
    public void onShowPress(MotionEvent event){}
    @Override
    public boolean onDown(MotionEvent event){return false;}
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY){
        return true;
    }
    @Override
    public boolean onSingleTapUp(MotionEvent event){
        return false;
    }
    @Override
    public boolean onDoubleTap(MotionEvent event){
        return false;
    }
    @Override
    public boolean onDoubleTapEvent(MotionEvent event){
        return true;
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent event){
        return true;
    }

    private void movePlayer(float velocityX, float velocityY){
        String direction = "undetectable";

        //Move the player in the fling direction
        if (velocityX > 2500){
            direction = "RIGHT";
        }else if (velocityX < -2500){
            direction = "LEFT";
        }else if (velocityY > 2500){
            direction = "DOWN";
        }else if (velocityY < -2500){
            direction = "UP";
        }

        if (!direction.contains("undetectable")){
            player.move(gameBoard, direction);
            playerIMG.setX(player.getCol() * SQUARE + OFFSET);
            playerIMG.setY(player.getRow() * SQUARE + OFFSET);
            Log.v("Player movement", "row=" + player.getCol() +
            "col=" + player.getRow());

            //Enemy's turn, move the enemy
            enemy.move(gameBoard, player.getCol(), player.getRow());
            enemyIMG.setX(enemy.getCol() * SQUARE + OFFSET);
            enemyIMG.setY(enemy.getRow() * SQUARE + OFFSET);
        }

        //Check if the game is over
        //Check if enemy catches player
        if (enemy.getCol() == player.getCol() && enemy.getRow() == player.getRow()){
            losses++;
            lossesTextView.setText("Losses: " + losses);
            startNewGame();
        }else if (exitRow == player.getRow() && exitCol == player.getCol()){
            wins++;
            winsTextView.setText("Wins: " + wins);
            startNewGame();
        }
    }//end movePlayer()

    private void startNewGame(){
        //Clear the board and remove players
        int howMany = visualObjects.size();
        for (int i = 0; i < howMany; i++){
            ImageView visualObj = visualObjects.get(i);
            constraintLayout.removeView(visualObj);
        }
        visualObjects.clear();

        //Rebuild the board
        buildLogicBoard();

        //Add the players
        createEnemy();
        createPlayer();
    }//end startNewGame()

    //Menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }//end createOptionsMenu()

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button,
        //as long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected()

}//end MainActivity class
