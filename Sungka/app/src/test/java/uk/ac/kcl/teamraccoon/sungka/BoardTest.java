package uk.ac.kcl.teamraccoon.sungka;

import org.junit.Test;
import static org.junit.Assert.*;


import uk.ac.kcl.teamraccoon.sungka.Board;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class BoardTest {

    @Test
    public void playersOwnStore(){
        Board board = new Board();
        board.takeTurn(0);
        int[] finalBoard = board.getArrayOfTrays();
        int[] expectedBoard = {0, 8, 8, 8, 8, 8, 8, 1,
                        7, 7, 7, 7, 7, 7, 7, 0};
        assertArrayEquals("Not equal after lands in player's own store", expectedBoard, finalBoard);
    }

    @Test
    public void playersOwnStoreAdditionalTurn(){
        Board board = new Board();
        board.takeTurn(0);
        Enum currentPlayer = board.getCurrentPlayer();
        assertEquals(currentPlayer, Player.PLAYER_ONE);
    }

    @Test
    public void ownEmptyTray(){
        Board board = new Board();
        takeTurn(board, 0, 1, 8, 0);
        int[] expectedBoard = {0,0,9,9,9,9,9,11,0,9,8,8,8,0,8,1};
        int[] finalBoard = board.getArrayOfTrays();
        assertArrayEquals("Boards not equal after empty tray move", expectedBoard, finalBoard);


    }

    @Test
    public void checkTurnPossible(){
        Board board = new Board();
        board.setCurrentPlayer(Player.PLAYER_TWO);
        int startingBoard[] = {0,0,0,0,0,0,0,26,2,7,7,7,7,7,7,28};
        board.setArrayOfTrays(startingBoard);
        board.takeTurn(8);
        assertEquals("Should have switched to player two", Player.PLAYER_TWO, board.getCurrentPlayer());
    }

    @Test
    public void checkTurnPossibleOne(){
        //Scenario: P1 should take turn, where last tray is players own store
        //This should give player another turn, but since player has no more shells
        //Should switch to player two
        Board board = new Board();
        board.setCurrentPlayer(Player.PLAYER_ONE);
        int startingBoard[] = {0,0,0,0,0,0,1,26,1,7,7,7,7,7,7,28};
        board.setArrayOfTrays(startingBoard);
        board.takeTurn(6);
        assertEquals("Should have switched to player two",Player.PLAYER_TWO,board.getCurrentPlayer());
    }

    @Test
    public void skipEnemyStore(){
        //Scenario: When shells redistributed loop around entire board
        //Check if shells skipped enemies store
        Board board =  new Board();
        board.setCurrentPlayer(Player.PLAYER_ONE);
        int startingBoard[] = {0,0,0,69,0,0,0,0,0,0,0,0,0,0,0,29};
        int expectedBoard[] = {4,4,4,4,5,5,5,5,5,5,5,5,5,4,4,29};
        board.setArrayOfTrays(startingBoard);
        board.takeTurn(3);
        assertArrayEquals(board.getArrayOfTrays(),expectedBoard);
    }


    /**
     *
     * @param board - Board to take turn on
     * @param selectedTrayIndices - All tray indexes to take turn on
     */
    public void takeTurn(Board board,int...selectedTrayIndices){
        for (int index: selectedTrayIndices){
            board.takeTurn(index);
        }
    }
}