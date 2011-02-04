package com.amazon.kindle.app.chess;

import org.apache.log4j.Logger;


/**
 * A representation of a particular state of a chessboard.
 * 
 * @author Adrian Petrescu
 *
 */
public class ChessBoard {

	private int [][] board;
	private int colorToMove;
	
	public static final int BLANK = -1;
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
	
	public static final int OFF_BOARD = -2;
	
	/** The size of the chess board being represented. */
	public static final int SIZE = 8;
	
	private final Logger log = Logger.getLogger(Main.class);
	
	/** Creates a new ChessBoard with the default size (8x8) */
	public ChessBoard() {
		board = new int[SIZE][SIZE];
	}
	
	/**
	 * Sets up an initial board position, with A1 at (0,0).
	 */
	public void init() {
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				board[x][y] = BLANK;
			}
		}
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
		
		colorToMove = WHITE;
	}
	
	public int getColorToMove() {
		return colorToMove;
	}
	
	public void toggleColorToMove() {
		switch(colorToMove) {
		case WHITE:
			colorToMove = BLACK; break;
		case BLACK:
			colorToMove = WHITE; break;
		}
	}
	
	/**
	 * Converts a square in algebraic coordinates to numeric coordinates. For example,
	 * the String "b3" would be mapped to the array <code>{1, 2}</code>.
	 * 
	 * @param coordinate An algebraic coordinate representing a square on the ChessBoard.
	 * @return An integer array containing the <code>x</code> and <code>y</code> coordinates
	 * representing the indices of the specified square.
	 */
	protected static int[] convertAlgebraicToCoordinate(String coordinate) {
		int[] coord = new int[2];
		
		coord[0] = coordinate.charAt(0) - 'a';
		coord[1] = Integer.parseInt(coordinate.substring(1,2)) - 1;
		
		return coord;
	}
	
	/**
	 * Converts a square in numeric coordinates to algebraic coordinates. For example,
	 * the array <code>{1, 2}</code> would be mapped to the String "b3".
	 * @param x The <code>x</code>-coordinate of a square on this ChessBoard.
	 * @param y The <code>y</code>-coordinate of a square on this ChessBoard.
	 * @return An algebraic coordinate representing the specified square.
	 */
	protected static String convertCoordinateToAlgebraic(int x, int y) {
		x += 'a';
		char xc = (char) x;
		String algebraicCoordinate = "" + xc;
		algebraicCoordinate += (y + 1);
		
		return algebraicCoordinate;
	}
	
	/**
	 * Returns the occupant of the square represented by the given algebraic coordinate.
	 * @param coordinate The algebraic coordinates of a square on this ChessBoard.
	 * @return <code>BLANK</code> if no piece occupies this square, or the piece code of the
	 * occupying piece (for example, <code>WHITE_PAWN</code>).
	 */
	public int getSquare(String coordinate) {
		int[] square = convertAlgebraicToCoordinate(coordinate);
	
		return getSquare(square[0],square[1]);
	}
	
	/**
	 * Returns the occupant of the square represented by the given index coordinate.
	 * 
	 * @param srcX The <code>x</code>-coordinate of a square on this ChessBoard.
	 * @param srcY The <code>y</code>-coordinate of a square on this ChessBoard.
	 * @return <code>BLANK</code> if no piece occupies this square, or the piece code of the
	 * occupying piece (for example, <code>WHITE_PAWN</code>).
	 */
	public int getSquare(int srcX, int srcY) {
		try {
			return board[srcX][srcY];
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return OFF_BOARD;
		}
	}
	
	/**
	 * Moves the piece occupying <code>srcCoordinate</code> to the square specified by
	 * <code>dstCoordinate</code>. <br/> <br/>
	 * 
	 * <b>Note:</b> At the moment, there is no legality-checking of moves being performed.
	 * @param srcCoordinate The starting square of the piece being moved.
	 * @param dstCoordinate The destination square of the piece being moved.
	 */
	public void move(String srcCoordinate, String dstCoordinate) {
		int[] srcSquare = convertAlgebraicToCoordinate(srcCoordinate);
		int[] dstSquare = convertAlgebraicToCoordinate(dstCoordinate);
		
		move(srcSquare[0], srcSquare[1], dstSquare[0], dstSquare[1]);
	}
	
	/**
	 * Moves the piece occupying <code>(srcX, srcY)</code> to the square specified by
	 * <code>(dstX, dstY)</code>. <br/> <br/>
	 * 
	 * <b>Note:</b> At the moment, there is no legality-checking of moves being performed.
	 * @param srcX The <code>x</code>-coordinate of the starting square of the piece being moved.
	 * @param srcY The <code>y</code>-coordinate of the starting square of the piece being moved.
	 * @param dstX The <code>x</code>-coordinate of the destination square of the piece being moved.
	 * @param dstY The <code>y</code>-coordinate of the destination square of the piece being moved.
	 */
	public void move(int srcX, int srcY, int dstX, int dstY) {
		board[dstX][dstY] = board[srcX][srcY];
		board[srcX][srcY] = BLANK;
		
		log.info("Moving (" + srcX + "," + srcY + ") to (" + dstX + "," + dstY + ")");
	}
	
	/**
	 * Returns the color of the specified square. Note that ChessBoards conventionally
	 * have a <code>WHITE</code> square in the bottom-right corner <code>(7,0)</code>.
	 * @param coordinate The algebraic coordinates of a square on this ChessBoard.
	 * @return <code>WHITE</code> if the specified square is colored white, <code>BLACK</code>
	 * if the specified square is colored black.
	 */
	public int getSquareColor(String coordinate) {
		int[] square = convertAlgebraicToCoordinate(coordinate);
		
		return getSquareColor(square[0], square[1]);
	}
	
	/**
	 * Returns the color of the specified square. Note that ChessBoards conventionally
	 * have a <code>WHITE</code> square in the bottom-right corner <code>(7,0)</code>.
	 * @param srcX The <code>x</code>-coordinate of a square on this ChessBoard.
	 * @param srcY The <code>y</code>-coordinate of a square on this ChessBoard.
	 * @return <code>WHITE</code> if the specified square is colored white, <code>BLACK</code>
	 * if the specified square is colored black.
	 */
	public int getSquareColor(int srcX, int srcY) {
		if (srcX < 0 || srcY < 0 || srcX >= SIZE || srcY >= SIZE) {
			return OFF_BOARD;
		}
		return ((srcX + srcY) % 2) == 0 ? BLACK : WHITE;
	}
	
	/**
	 * Returns the color of the piece on the specified square
	 * @param coordinate The algebraic coordinates of a square on this ChessBoard.
	 * @return <code>WHITE</code> if the specified square is being occupied by a white piece,
	 * <code>BLACK</code> if the specified square is being occupied by a black piece.
	 */
	public int getPieceColor(String coordinate) {
		int[] square = convertAlgebraicToCoordinate(coordinate);
		
		return getSquareColor(square[0], square[1]);
	}
	
	/**
	 * Returns the color of the piece on the specified square.
	 * @param srcX The <code>x</code>-coordinate of a square on this ChessBoard.
	 * @param srcY The <code>y</code>-coordinate of a square on this ChessBoard.
	 * @return <code>WHITE</code> if the specified square is being occupied by a white piece,
	 * <code>BLACK</code> if the specified square is being occupied by a black piece.
	 */
	public int getPieceColor(int srcX, int srcY) {
		if (srcX < 0 || srcY < 0 || srcX >= SIZE || srcY >= SIZE) {
			return OFF_BOARD;
		}
		
		return getSquare(srcX, srcY) / 10;
	}
	
	/**
	 * Returns a textual representation of this ChessBoard.
	 * @return A textual representation of this ChessBoard.
	 */
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
