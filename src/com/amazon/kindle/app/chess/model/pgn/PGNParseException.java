package com.amazon.kindle.app.chess.model.pgn;

public class PGNParseException extends Exception {
  private static final long serialVersionUID = 8014989081590526944L;

  private String message;

  public PGNParseException(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
