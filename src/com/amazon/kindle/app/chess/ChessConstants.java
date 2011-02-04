package com.amazon.kindle.app.chess;

import java.util.HashMap;
import java.util.Map;

public class ChessConstants {
  /** A mapping from piece codes to their corresponding Unicode glyphs. */
  public static final Map pieceTextIconMap = new HashMap();

  /** A mapping from piece Strings to their corresponding piece code */
  public static final Map pieceTextMap = new HashMap();

  static {
    pieceTextIconMap.put(new Integer(ChessBoard.WHITE_PAWN), "\u2659");
    pieceTextIconMap.put(new Integer(ChessBoard.WHITE_ROOK), "\u2656");
    pieceTextIconMap.put(new Integer(ChessBoard.WHITE_KNIGHT), "\u2658");
    pieceTextIconMap.put(new Integer(ChessBoard.WHITE_BISHOP), "\u2657");
    pieceTextIconMap.put(new Integer(ChessBoard.WHITE_QUEEN), "\u2655");
    pieceTextIconMap.put(new Integer(ChessBoard.WHITE_KING), "\u2654");

    pieceTextIconMap.put(new Integer(ChessBoard.BLACK_PAWN), "\u265F");
    pieceTextIconMap.put(new Integer(ChessBoard.BLACK_ROOK), "\u265C");
    pieceTextIconMap.put(new Integer(ChessBoard.BLACK_KNIGHT), "\u265E");
    pieceTextIconMap.put(new Integer(ChessBoard.BLACK_BISHOP), "\u265D");
    pieceTextIconMap.put(new Integer(ChessBoard.BLACK_QUEEN), "\u265B");
    pieceTextIconMap.put(new Integer(ChessBoard.BLACK_KING), "\u265A");

    pieceTextMap.put("R", new Integer(ChessBoard.WHITE_ROOK));
    pieceTextMap.put("N", new Integer(ChessBoard.WHITE_KNIGHT));
    pieceTextMap.put("B", new Integer(ChessBoard.WHITE_BISHOP));
    pieceTextMap.put("Q", new Integer(ChessBoard.WHITE_QUEEN));
    pieceTextMap.put("K", new Integer(ChessBoard.WHITE_KING));
  }
}
