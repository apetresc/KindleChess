package com.amazon.kindle.app.chess;

import com.codethesis.pgnparse.Color;
import com.codethesis.pgnparse.PGNMove;

/**
 * A representation of a particular state of a chessboard.
 * 
 * @author Adrian Petrescu
 * 
 */
public class ChessBoard {

  private byte[][] board;

  public static final byte BLANK = -1;
  public static final byte WHITE_PAWN = 1;
  public static final byte WHITE_ROOK = 2;
  public static final byte WHITE_KNIGHT = 3;
  public static final byte WHITE_BISHOP = 4;
  public static final byte WHITE_QUEEN = 5;
  public static final byte WHITE_KING = 6;
  public static final byte BLACK_PAWN = 11;
  public static final byte BLACK_ROOK = 12;
  public static final byte BLACK_KNIGHT = 13;
  public static final byte BLACK_BISHOP = 14;
  public static final byte BLACK_QUEEN = 15;
  public static final byte BLACK_KING = 16;

  public static final byte WHITE = 0;
  public static final byte BLACK = 10;

  public static final byte OFF_BOARD = -2;

  /** The size of the chess board being represented. */
  public static final int SIZE = 8;

  /** Creates a new ChessBoard with the default size (8x8) */
  public ChessBoard() {
    board = new byte[SIZE][SIZE];
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
  }

  /**
   * Converts a square in algebraic coordinates to numeric coordinates. For
   * example, the String "b3" would be mapped to the array <code>{1, 2}</code>.
   * 
   * @param coordinate
   *          An algebraic coordinate representing a square on the ChessBoard.
   * @return An integer array containing the <code>x</code> and <code>y</code>
   *         coordinates representing the indices of the specified square.
   */
  protected static int[] convertAlgebraicToCoordinate(String coordinate) {
    int[] coord = new int[2];

    coord[0] = coordinate.charAt(0) - 'a';
    coord[1] = Integer.parseInt(coordinate.substring(1, 2)) - 1;

    return coord;
  }

  /**
   * Converts a square in numeric coordinates to algebraic coordinates. For
   * example, the array <code>{1, 2}</code> would be mapped to the String "b3".
   * 
   * @param x
   *          The <code>x</code>-coordinate of a square on this ChessBoard.
   * @param y
   *          The <code>y</code>-coordinate of a square on this ChessBoard.
   * @return An algebraic coordinate representing the specified square.
   */
  public static String convertCoordinateToAlgebraic(int x, int y) {
    x += 'a';
    char xc = (char) x;
    String algebraicCoordinate = "" + xc;
    algebraicCoordinate += (y + 1);

    return algebraicCoordinate;
  }

  /**
   * Returns the occupant of the square represented by the given algebraic
   * coordinate.
   * 
   * @param coordinate
   *          The algebraic coordinates of a square on this ChessBoard.
   * @return <code>BLANK</code> if no piece occupies this square, or the piece
   *         code of the occupying piece (for example, <code>WHITE_PAWN</code>).
   */
  public byte getSquare(String coordinate) {
    int[] square = convertAlgebraicToCoordinate(coordinate);

    return getSquare(square[0], square[1]);
  }

  /**
   * Returns the occupant of the square represented by the given index
   * coordinate.
   * 
   * @param srcX
   *          The <code>x</code>-coordinate of a square on this ChessBoard.
   * @param srcY
   *          The <code>y</code>-coordinate of a square on this ChessBoard.
   * @return <code>BLANK</code> if no piece occupies this square, or the piece
   *         code of the occupying piece (for example, <code>WHITE_PAWN</code>).
   */
  public byte getSquare(int srcX, int srcY) {
    try {
      return board[srcX][srcY];
    } catch (ArrayIndexOutOfBoundsException aioobe) {
      return OFF_BOARD;
    }
  }

  /**
   * Moves the piece occupying <code>srcCoordinate</code> to the square
   * specified by <code>dstCoordinate</code>. <br/>
   * <br/>
   * 
   * <b>Note:</b> At the moment, there is no legality-checking of moves being
   * performed.
   * 
   * @param srcCoordinate
   *          The starting square of the piece being moved.
   * @param dstCoordinate
   *          The destination square of the piece being moved.
   */
  public void move(String srcCoordinate, String dstCoordinate) {
    int[] srcSquare = convertAlgebraicToCoordinate(srcCoordinate);
    int[] dstSquare = convertAlgebraicToCoordinate(dstCoordinate);

    move(srcSquare[0], srcSquare[1], dstSquare[0], dstSquare[1]);
  }

  /**
   * Moves the piece occupying <code>(srcX, srcY)</code> to the square specified
   * by <code>(dstX, dstY)</code>. <br/>
   * <br/>
   * 
   * <b>Note:</b> At the moment, there is no legality-checking of moves being
   * performed.
   * 
   * @param srcX
   *          The <code>x</code>-coordinate of the starting square of the piece
   *          being moved.
   * @param srcY
   *          The <code>y</code>-coordinate of the starting square of the piece
   *          being moved.
   * @param dstX
   *          The <code>x</code>-coordinate of the destination square of the
   *          piece being moved.
   * @param dstY
   *          The <code>y</code>-coordinate of the destination square of the
   *          piece being moved.
   */
  public void move(int srcX, int srcY, int dstX, int dstY) {
    board[dstX][dstY] = board[srcX][srcY];
    board[srcX][srcY] = BLANK;
  }

  /**
   * Applies the specified move to the board.
   *
   * @param move A legal PGNMove for the current board context.
   */
  public void move(PGNMove move) {
    if (move.isQueenSideCastle()) {
      switch (move.getColor()) {
      case Color.WHITE:
        move("e1", "c1");
        move("a1", "d1");
        break;
      case Color.BLACK:
        move("e8", "c8");
        move("a8", "d8");
        break;
      }
    } else if (move.isKingSideCastle()) {
      switch (move.getColor()) {
      case Color.WHITE:
        move("e1", "g1");
        move("h1", "f1");
        break;
      case Color.BLACK:
        move("e8", "g8");
        move("h8", "f8");
        break;
      }
    } else if (move.isEndGameMarked()) {
      // Do nothing
    } else {
      // Just a regular move
      move(move.getFromSquare(), move.getToSquare());
    }
  }

  /**
   * Undoes the specified move on the board.
   *
   * @param move The last PGNMove made on the board
   */
  public void undoMove(PGNMove move) {
    if (move.isQueenSideCastle()) {
      switch (move.getColor()) {
      case Color.WHITE:
        move("d1", "a1");
        move("c1", "e1");
        break;
      case Color.BLACK:
        move("d8", "a8");
        move("c8", "e8");
        break;
      }
    } else if (move.isKingSideCastle()) {
      switch(move.getColor()) {
      case Color.WHITE:
        move("f1", "h1");
        move("g1", "e1");
        break;
      case Color.BLACK:
        move("f8", "h8");
        move("g8", "e8");
        break;
      }
    } else if (move.isEndGameMarked()) {
      // Do nothing
    } else {
      // Just a regular move
      int fromSquareColor = getPieceColor(move.getToSquare()) == WHITE ? BLACK : WHITE;
      move(move.getToSquare(), move.getFromSquare());
      if (move.isCaptured()) {
        char capturedPieceChar = move.getCapturedPiece().charAt(0);
        byte capturedPiece;
        switch (capturedPieceChar) {
        case 'P':
          capturedPiece = WHITE_PAWN;
          break;
        case 'N':
          capturedPiece = WHITE_KNIGHT;
          break;
        case 'B':
          capturedPiece = WHITE_BISHOP;
          break;
        case 'R':
          capturedPiece = WHITE_ROOK;
          break;
        case 'Q':
          capturedPiece = WHITE_QUEEN;
          break;
        default:
          return;  
        }
        capturedPiece += fromSquareColor;
        int[] toSquareCoords = convertAlgebraicToCoordinate(move.getToSquare());
        board[toSquareCoords[0]][toSquareCoords[1]] = capturedPiece;
      }
    }
    
  }

  /**
   * Returns the color of the specified square. Note that ChessBoards
   * conventionally have a <code>WHITE</code> square in the bottom-right corner
   * <code>(7,0)</code>.
   * 
   * @param coordinate
   *          The algebraic coordinates of a square on this ChessBoard.
   * @return <code>WHITE</code> if the specified square is colored white,
   *         <code>BLACK</code> if the specified square is colored black.
   */
  public int getSquareColor(String coordinate) {
    int[] square = convertAlgebraicToCoordinate(coordinate);

    return getSquareColor(square[0], square[1]);
  }

  /**
   * Returns the color of the specified square. Note that ChessBoards
   * conventionally have a <code>WHITE</code> square in the bottom-right corner
   * <code>(7,0)</code>.
   * 
   * @param srcX
   *          The <code>x</code>-coordinate of a square on this ChessBoard.
   * @param srcY
   *          The <code>y</code>-coordinate of a square on this ChessBoard.
   * @return <code>WHITE</code> if the specified square is colored white,
   *         <code>BLACK</code> if the specified square is colored black.
   */
  public byte getSquareColor(int srcX, int srcY) {
    if (srcX < 0 || srcY < 0 || srcX >= SIZE || srcY >= SIZE) {
      return OFF_BOARD;
    }
    return ((srcX + srcY) % 2) == 0 ? BLACK : WHITE;
  }

  /**
   * Returns the color of the piece on the specified square
   * 
   * @param coordinate
   *          The algebraic coordinates of a square on this ChessBoard.
   * @return <code>WHITE</code> if the specified square is being occupied by a
   *         white piece, <code>BLACK</code> if the specified square is being
   *         occupied by a black piece.
   */
  public byte getPieceColor(String coordinate) {
    int[] square = convertAlgebraicToCoordinate(coordinate);

    return (byte) ((board[square[0]][square[1]] / 10) * 10);
  }

  /**
   * Returns the color of the piece on the specified square.
   * 
   * @param srcX
   *          The <code>x</code>-coordinate of a square on this ChessBoard.
   * @param srcY
   *          The <code>y</code>-coordinate of a square on this ChessBoard.
   * @return <code>WHITE</code> if the specified square is being occupied by a
   *         white piece, <code>BLACK</code> if the specified square is being
   *         occupied by a black piece.
   */
  public byte getPieceColor(int srcX, int srcY) {
    if (srcX < 0 || srcY < 0 || srcX >= SIZE || srcY >= SIZE) {
      return OFF_BOARD;
    }

    return (byte) (getSquare(srcX, srcY) / 10);
  }
}
