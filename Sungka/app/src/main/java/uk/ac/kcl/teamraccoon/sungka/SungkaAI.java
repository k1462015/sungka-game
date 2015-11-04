package uk.ac.kcl.teamraccoon.sungka;

import java.util.Arrays;

// SKYNET v0.1
public class SungkaAI {

	private static final int NORMAL = 1; // constants for difficulty
	private static final int HARD = 2;

	public static int takeTurn(Board board){ // wrapper if no difficulty is inserted, NORMAL is default
		return takeTurn(board,1);
	}
	
	public static int takeTurn(Board board, int difficulty) {
		// Create a copy of the original board
		Board boardCopy = new Board();
		boardCopy.setArrayOfTrays(Arrays.copyOf(board.getArrayOfTrays(),
				board.getArrayOfTrays().length));
		boardCopy.setCurrentPlayer(board.getCurrentPlayer());

		// which tray should the AI play
		int bestValueAtIndex = (int) evaluateNextTurn(boardCopy, difficulty)[1];

		// return it
		return bestValueAtIndex;
	}
	
	// algorithm for evaluating the board using the int array
	private static double evaluateBoard(int[] shellsArray, int difficulty) {
		double score = shellsArray[15] - shellsArray[7]; // own store is good,
															// enemy store is
															// bad
		for (int i = 0; i <= 6; i++) {
			score -= (double) shellsArray[i] / 2; // shells on enemy side is bad
		}

		for (int i = 8; i <= 14; i++) {
			score += (double) shellsArray[i] / 2; // shells on own side is good
		}
		if (difficulty == HARD) { // extra checks for hard difficulty
			for (int i = 0; i <= 6; i++) { // possible steals from enemy are bad
				if (shellsArray[i] == 0) {
					for (int j = 0; j <= 6; j++) {
						if (j == i)
							continue;
						if ((shellsArray[j] + j) % 16 == i) {
							score -= shellsArray[14 - i] + 1;
						}
					}
				}
			}
		}

		// return calculated board value / score
		return score;
	}

	// which tray should the AI play?
	// [boardscore, index to play]
	private static double[] evaluateNextTurn(Board b, int difficulty) {

		double bestValue = -98;
		double bestValueAtIndex = 0;
		// loop through your trays to check which one is best
		for (int i = 8; i <= 14; i++) {
			double currentValue = -98;
			double currentValueAtIndex = 0;
			Board tempBoard = new Board(); // copy of board
			tempBoard.setCurrentPlayer(b.getCurrentPlayer());
			tempBoard.setArrayOfTrays(Arrays.copyOf(b.getArrayOfTrays(),
					b.getArrayOfTrays().length));
			if (tempBoard.getArrayOfTrays()[i] == 0) { // cant play tray with 0
														// shells
				continue;
			}
			tempBoard.takeTurn(i);
			if (tempBoard.getCurrentPlayer() == Player.PLAYER_TWO) { // recursion
																		// for
																		// making
																		// double/triple/etc
																		// turns
				currentValue = evaluateNextTurn(tempBoard, difficulty)[0] + 1
						+ tempBoard.getArrayOfTrays()[i] / 16;
				currentValueAtIndex = i;
			} else { // evaluate board
				currentValue = evaluateBoard(tempBoard.getArrayOfTrays(), difficulty);
				currentValueAtIndex = i;
			}

			if (currentValue >= bestValue) { // check which tray is best to play
				bestValue = currentValue;
				bestValueAtIndex = currentValueAtIndex;
			}

		}
		// return value of best tray and index of best tray
		return new double[] { bestValue, bestValueAtIndex };
	}

}
