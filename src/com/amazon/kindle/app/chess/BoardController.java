package com.amazon.kindle.app.chess;

public class BoardController {

	private ChessBoard board;
	
	public BoardController(ChessBoard board) {
		this.board = board;
	}
	
	public void applyMove(String move) throws IllegalMoveException {
		int[][] moveCoords = null;
		switch(move.length()) {
		case 2:
			moveCoords = pawnSimpleMove(move);
		}
		
		if (moveCoords != null) {
			board.move(moveCoords[0][0], moveCoords[0][1], moveCoords[1][0], moveCoords[1][1]);
			board.setColorToMove((board.getColorToMove() + 1) % 2);
		}
	}
	
	private int[][] pawnSimpleMove(String move) throws IllegalMoveException {
		int colorToMove = board.getColorToMove();
		int[] moveCoords = ChessBoard.convertAlgebraicToCoordinate(move);
		int x = moveCoords[0] ; int y = moveCoords[1];
		switch (colorToMove) {
		case ChessBoard.WHITE:
			switch (y) {
			case 3:
				// Might be a double pawn move
				if (board.getSquare(x, 1) == ChessBoard.WHITE_PAWN)
					return new int[][] {new int[]{x, 1}, new int[]{x, y}};
			default:
				if (board.getSquare(x, y - 1) == ChessBoard.WHITE_PAWN)
					return new int[][] {new int[]{x, y-1}, new int[]{x, y}};
			}
			break;
		case ChessBoard.BLACK:
			switch (y) {
			case 4:
				// Might be a double pawn move
				if (board.getSquare(x, 6) == ChessBoard.BLACK_PAWN)
					return new int[][] {new int[]{x, 6}, new int[]{x, y}};
			default:
				if (board.getSquare(x, y+1) == ChessBoard.BLACK_PAWN)
					return new int[][] {new int[]{x, y+1}, new int[]{x, y}};
			}
		}
		throw new IllegalMoveException(move, "There's no pawn to move there!");
	}
}
