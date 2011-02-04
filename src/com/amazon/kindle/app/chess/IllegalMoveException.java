package com.amazon.kindle.app.chess;

public class IllegalMoveException extends Exception {
  private static final long serialVersionUID = -8275239031004025884L;
  private String move;
  private String reason;

  /**
   * Constructs a new instance of IllegalMoveException.
   * 
   * @param move
   *          The illegal move.
   * @param reason
   *          The reason why this move is not legal.
   */
  public IllegalMoveException(String move, String reason) {
    this.move = move;
    this.reason = reason;
  }

  public String getMessage() {
    return "Move " + move + " is illegal: " + reason;
  }
}
