package com.missouristate.davis916.escapethecatcher;

/**
 * Laura Davis CIS 262-902
 * 19 April 2018
 * Creates the Player object
 */

public class Player {
    private int mRow;
    private int mCol;

    public void move(int[][] gameBoard, String button){
        if(button.equals("RIGHT")){
            if (gameBoard[mRow][mCol + 1] != BoardCodes.isOBSTACLE){
                mCol++;
            }
        }else if (button.equals("LEFT")){
            if (gameBoard[mRow][mCol - 1] != BoardCodes.isOBSTACLE) {
                mCol--;
            }
        }else if (button.equals("UP")){
            if (gameBoard[mRow - 1][mCol] != BoardCodes.isOBSTACLE) {
                mRow--;
            }
        }else if (button.equals("DOWN")){
            if (gameBoard[mRow + 1][mCol] != BoardCodes.isOBSTACLE) {
                mRow++;
            }
        }
    }//end move()

    public void setRow(int row){
        mRow = row;
    }

    public int getRow(){
        return mRow;
    }

    public void setCol(int col){
        mCol = col;
    }

    public int getCol(){
        return mCol;
    }

}//end Player class
