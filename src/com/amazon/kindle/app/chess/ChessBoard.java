package com.amazon.kindle.app.chess;

public class ChessBoard {

	private int [][] board;
	
	public static final int BLANK = 0;
	public static final int WHITE_PAWN = 1;
	public static final int WHITE_ROOK = 2;
	public static final int WHITE_KNIGHT = 3;
	public static final int WHITE_BISHOP = 4;
	public static final int WHITE_QUEEN = 5;
	public static final int WHITE_KING = 6;
	public static final int BLACK_PAWN = 11;
	public static final int BLACK_ROOK = 12;
	public static final int BLACK_KNIGHT = 13;
	public static final int BLACK_BISHOP = 14;
	public static final int BLACK_QUEEN = 15;
	public static final int BLACK_KING = 16;
	
	public static final int WHITE = 0;
	public static final int BLACK = 10;
	
	public static final int SIZE = 8;
	
	public ChessBoard() {
		board = new int[SIZE][SIZE];
	}
	
	/**
	 * Sets up an initial board position, with A1 at (0,0).
	 */
	public void init() {
		for (int i = 0; i < SIZE; i++) {
			board[i][1] = WHITE_PAWN;
			board[i][6] = BLACK_PAWN;
		}
		
		board[0][0] = board[7][0] = WHITE_ROOK;
		board[1][0] = board[6][0] = WHITE_KNIGHT;
		board[2][0] = board[5][0] = WHITE_BISHOP;
		board[3][0] = WHITE_QUEEN;
		board[4][0] = WHITE_KING;
		
		board[0][7] = board[7][7] = BLACK_ROOK;
		board[1][7] = board[6][7] = BLACK_KNIGHT;
		board[2][7] = board[5][7] = BLACK_BISHOP;
		board[3][7] = BLACK_QUEEN;
		board[4][7] = BLACK_KING;
	}
	
	private int[] convertAlgebraicToCoordinate(String coordinate) {
		int[] coord = new int[2];
		
		coord[0] = coordinate.charAt(0) - 'a';
		coord[1] = Integer.parseInt(coordinate.substring(1,2));
		
		return coord;
	}
	
	public int getSquare(String coordinate) {
		int[] square = convertAlgebraicToCoordinate(coordinate);
	
		return getSquare(square[0],square[1]);
	}
	
	public int getSquare(int srcX, int srcY) {
		return board[srcX][srcY];
	}
	
	public void move(String srcCoordinate, String dstCoordinate) {
		int[] srcSquare = convertAlgebraicToCoordinate(srcCoordinate);
		int[] dstSquare = convertAlgebraicToCoordinate(dstCoordinate);
		
		move(srcSquare[0], srcSquare[1], dstSquare[0], dstSquare[1]);
	}
	
	public void move(int srcX, int srcY, int dstX, int dstY) {
		board[dstX][dstY] = board[srcX][srcY];
		board[srcX][srcY] = BLANK;
	}
	
	public int getColor(String coordinate) {
		int[] square = convertAlgebraicToCoordinate(coordinate);
		
		return getColor(square[0], square[1]);
	}
	
	public int getColor(int srcX, int srcY) {
		return ((srcX + srcY) % 2) == 0 ? BLACK : WHITE;
	}
	
	public String toString() {
		String boardStr = "";
		for (int y = SIZE - 1; y >= 0; y--) {
			for (int x = 0; x < SIZE; x++) {
				boardStr += "|" + board[x][y] + "|";
			}
			boardStr += "\n";
		}
		return boardStr;
	}
}
